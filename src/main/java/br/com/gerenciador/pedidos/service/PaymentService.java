package br.com.gerenciador.pedidos.service;

import br.com.gerenciador.pedidos.core.exception.PaymentException;
import br.com.gerenciador.pedidos.core.records.input.ProductQuantityRelationInput;
import br.com.gerenciador.pedidos.core.records.output.PaymentResultOutput;
import br.com.gerenciador.pedidos.entitys.OrderItem;
import br.com.gerenciador.pedidos.entitys.PaymentIdempotency;
import br.com.gerenciador.pedidos.entitys.Product;
import br.com.gerenciador.pedidos.enums.OrderStatus;
import br.com.gerenciador.pedidos.repositories.OrderItemRepository;
import br.com.gerenciador.pedidos.repositories.PaymentIdempotencyRepository;
import br.com.gerenciador.pedidos.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderService orderService;
    private final PaymentIdempotencyRepository paymentIdempotencyRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public PaymentResultOutput pay(String orderId) {
        final var order = orderService.lockById(orderId);

        if (order.getStatus() != OrderStatus.PENDING)
            throw new PaymentException("Pedido já processado");

        order.ensurePaymentIdemKey();
        final var idemKey = order.getPaymentIdemKey();

        try {
            paymentIdempotencyRepository.save(PaymentIdempotency.builder()
                    .order(order)
                    .idemKey(idemKey)
                    .build());
        } catch (DataIntegrityViolationException e) {
            return getPaymentResultOutput(order.getDetails(), false);
        }

        final var items = orderItemRepository.findByOrderId(order.getId());
        if (items.isEmpty()) {
            final String msg = "Sem itens no pedido";
            order.cancel(msg);
            orderService.save(order);
            return getPaymentResultOutput(msg, false);
        }

        final var products = items.stream()
                .map(orderItem -> ProductQuantityRelationInput.buildFromProduct(orderItem.getProduct().getId(), orderItem.getQuantity()))
                .toList();
        final var productIds = products.stream().map(ProductQuantityRelationInput::productId).collect(Collectors.toSet());
        final var lockedProducts = productRepository.lockAllByIds(productIds);

        final Set<String> outOfStock = orderService.getOutOfStockProducts(products, lockedProducts);

        if (!outOfStock.isEmpty()) {
            final String msg = "Estoque insuficiente para: " + String.join(", ", outOfStock);
            order.cancel(msg);
            orderService.save(order);
            return getPaymentResultOutput(msg, false);
        }

        final var lockedById = lockedProducts.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        for (OrderItem it : items) {
            final Product p = lockedById.get(it.getProduct().getId());
            p.setQuantity(p.getQuantity() - it.getQuantity());
            productRepository.save(p);
        }

        order.markPaid();
        orderService.save(order);
        return getPaymentResultOutput(order.getDetails(), true);
    }

    private static PaymentResultOutput getPaymentResultOutput(String order, boolean sucesso) {
        return PaymentResultOutput.builder()
                .mensagem(order)
                .sucesso(sucesso)
                .build();
    }


}
