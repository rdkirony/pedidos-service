package br.com.gerenciador.pedidos.core.records.output;

import br.com.gerenciador.pedidos.entitys.Order;
import br.com.gerenciador.pedidos.entitys.OrderItem;
import br.com.gerenciador.pedidos.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
@Schema(name = "OrderOutput", description = "Payload de saída do cadastro de um pedido")
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderOutput(
        @Schema(name = "id", description = "Id do pedido")
        @JsonProperty("id")
        String id,
        @Schema(name = "id", description = "Status do pedido")
        @JsonProperty("status")
        String status,
        @Schema(name = "total", description = "Valor total do pedido")
        @JsonProperty("total")
        BigDecimal total,
        @Schema(name = "details", description = "Detalhes sobre o pedido")
        @JsonProperty("details")
        String details,
        @Schema(name = "products", description = "Produtos presentes no pedido")
        @JsonProperty("products")
        List<ProductOutput> products
) {
    public static OrderOutput buildOutput(final String id,
                                          final OrderStatus status,
                                          final BigDecimal total,
                                          final String details,
                                          final List<ProductOutput> products) {
        return OrderOutput.builder()
                .id(id)
                .details(details)
                .status(status.name())
                .total(total)
                .products(products)
                .build();
    }

    public static OrderOutput entityToOutput(final Order order) {
        BigDecimal total = BigDecimal.ZERO;
        final List<ProductOutput> productOutputs = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            final var unitPrice = Optional.ofNullable(item.getPrice()).orElse(BigDecimal.ZERO);
            final var line = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(line);
        }
        total = order.getStatus() == OrderStatus.CANCELED ? BigDecimal.ZERO : total;

        return OrderOutput.builder()
                .id(order.getId())
                .details(order.getDetails())
                .status(order.getStatus().name())
                .total(total)
                .build();
    }
}
