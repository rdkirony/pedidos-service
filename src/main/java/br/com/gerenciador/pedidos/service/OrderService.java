package br.com.gerenciador.pedidos.service;

import br.com.gerenciador.pedidos.core.exception.ProductCancelException;
import br.com.gerenciador.pedidos.core.records.input.OrderInput;
import br.com.gerenciador.pedidos.core.records.input.ProductQuantityRelationInput;
import br.com.gerenciador.pedidos.core.records.output.OrderOutput;
import br.com.gerenciador.pedidos.core.records.output.ProductOutput;
import br.com.gerenciador.pedidos.entitys.Order;
import br.com.gerenciador.pedidos.entitys.OrderItem;
import br.com.gerenciador.pedidos.entitys.Product;
import br.com.gerenciador.pedidos.entitys.User;
import br.com.gerenciador.pedidos.enums.OrderStatus;
import br.com.gerenciador.pedidos.repositories.OrderItemRepository;
import br.com.gerenciador.pedidos.repositories.OrderRepository;
import br.com.gerenciador.pedidos.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository repository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderOutput buildOrderOutput(String id) {
        final var order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        final var items = orderItemRepository.findByOrderId(order.getId());
        final var productIds = items.stream().map(it -> it.getProduct().getId()).distinct().toList();
        final var productsRelation = productRepository.findAllById(productIds)
                .stream().collect(Collectors.toMap(Product::getId, p -> p));
        var total = BigDecimal.ZERO;
        final List<ProductOutput> productOutputs = new ArrayList<>();

        for (OrderItem item : items) {
            final var product = productsRelation.get(item.getProduct().getId());
            productOutputs.add(ProductOutput.convertEntity(product));
            final var unitPrice = Optional.ofNullable(item.getPrice()).orElse(BigDecimal.ZERO);
            final var line = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(line);
        }
        total = order.getStatus() == OrderStatus.CANCELED ? BigDecimal.ZERO : total;
        return OrderOutput.buildOutput(order.getId(), order.getStatus(), total, order.getDetails(), productOutputs);
    }


    @Transactional
    public OrderOutput createOrder(final OrderInput input, final User user) {
        if (input.products().isEmpty()) {
            throw new IllegalArgumentException("Pedido sem itens");
        }

        final var productIds = input.products().stream().map(ProductQuantityRelationInput::productId).collect(Collectors.toSet());
        final var lockedProducts = productRepository.lockAllByIds(productIds);
        final Map<String, Product> relationProducts = lockedProducts.stream().collect(Collectors.toMap(Product::getId, p -> p));

        for (ProductQuantityRelationInput it : input.products()) {
            if (!relationProducts.containsKey(it.productId())) {
                throw new EntityNotFoundException("Produto não encontrado: " + it.productId());
            }
            if (it.quantity() <= 0) {
                throw new IllegalArgumentException("Quantidade inválida para produto " + it.productId());
            }
        }

        final Set<String> outOfStock = getOutOfStockProducts(input.products(), lockedProducts);

        var order = Order.builder()
                .user(user)
                .build();

        if (!outOfStock.isEmpty()) {
            final var msg = "Estoque insuficiente para: " + String.join(", ", outOfStock);
            order.setCreationDate(LocalDateTime.now());
            order.cancel(msg);
        }

        return getOrderOutput(input, order, relationProducts);
    }

    public Set<String> getOutOfStockProducts(List<ProductQuantityRelationInput> products, List<Product> lockedProducts) {
        final var requestedByProduct = products.stream()
                .collect(Collectors.toMap(
                        ProductQuantityRelationInput::productId,
                        ProductQuantityRelationInput::quantity,
                        Integer::sum
                ));

        final var lockedById = lockedProducts.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        final Set<String> outOfStock = new HashSet<>();
        for (Map.Entry<String, Integer> e : requestedByProduct.entrySet()) {
            final var product = lockedById.get(e.getKey());
            final var availableQuantity = Optional.ofNullable(product.getQuantity()).orElse(0);
            if (availableQuantity < e.getValue()) {
                outOfStock.add(product.getName());
            }
        }
        return outOfStock;
    }


    private OrderOutput getOrderOutput(OrderInput input, Order order, Map<String, Product> products) {
        repository.save(order);
        saveItems(input, order, products);
        return buildOrderOutput(order.getId());
    }

    private void saveItems(OrderInput input, Order order, Map<String, Product> products) {
        final var items = input.products().stream()
                .map(i -> OrderItem.builder()
                        .order(order)
                        .product(products.get(i.productId()))
                        .price(products.get(i.productId()).getPrice())
                        .quantity(i.quantity())
                        .build())
                .toList();
        orderItemRepository.saveAll(items);
    }

    @Transactional
    public OrderOutput cancelIfPending(String orderId) {
        final var order = this.lockById(orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ProductCancelException("Não é possível cancelar um pedido diferente de pendente");
        }
        order.cancel("Cancelado pelo usuário enquanto pendente");
        repository.save(order);
        return buildOrderOutput(order.getId());
    }

    public Order lockById(String id) {
        return repository.lockById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    }

    public void save(Order order) {
        repository.save(order);
    }


    public Page<String> findByUserId(User user, Pageable pageable) {
        return repository.findByUserId(user.getId(), pageable);
    }


}
