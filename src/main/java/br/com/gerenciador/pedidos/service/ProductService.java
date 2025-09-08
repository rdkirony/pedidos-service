package br.com.gerenciador.pedidos.service;

import br.com.gerenciador.pedidos.core.exception.ProductRegisterException;
import br.com.gerenciador.pedidos.core.records.input.ProductInput;
import br.com.gerenciador.pedidos.entitys.Product;
import br.com.gerenciador.pedidos.enums.OrderStatus;
import br.com.gerenciador.pedidos.repositories.OrderRepository;
import br.com.gerenciador.pedidos.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final OrderRepository orderRepository;


    @Transactional
    public Product register(ProductInput input) {
        validateData(input);
        return repository.save(new Product(input));
    }

    @Transactional
    public Product update(Product product, ProductInput input) {
        validateData(input);
        if (!Objects.equals(product.getQuantity(), input.quantity())) {
            final var orderPending = orderRepository.findByStatusAndProductId(OrderStatus.PENDING, product.getId());
            if (!CollectionUtils.isEmpty(orderPending)) {
                throw new ProductRegisterException("Não é possível atualizar o estoque pois o produto possui pedidos pendentes");
            }
        }
        product.setValuesFromInputRequest(product, input);
        return repository.save(product);
    }

    private static void validateData(ProductInput product) {
        if (StringUtils.isBlank(product.name())) {
            throw new ProductRegisterException("Deve ser informado o nome do produto");
        }
        if (product.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductRegisterException("Valor do produto deve ser maior que 0");
        }
        if (product.quantity() < 0) {
            throw new ProductRegisterException("Quantidade deve ser informada e não pode ser negativa");
        }
    }

    public Product findById(String id) {
        final var productOpt = repository.findById(id);
        if (productOpt.isEmpty()) {
            throw new EntityNotFoundException("Produto não encontrado");
        }
        return productOpt.get();
    }

    @Transactional
    public void removeById(String id) {
        repository.deleteById(id);
    }

    public Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }
}
