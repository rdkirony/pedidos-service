package br.com.gerenciador.pedidos.core.records.output;

import br.com.gerenciador.pedidos.entitys.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(name = "ProductRegisterOut", description = "Response de produtos")
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductOutput(
        @Schema(name = "id", description = "Id do produto")
        @JsonProperty("id")
        String id,
        @Schema(name = "name", description = "Nome do produto")
        @JsonProperty("name")
        String name,
        @Schema(name = "description", description = "Descrição do produto")
        @JsonProperty("description")
        String description,
        @Schema(name = "price", description = "Preço por unidade")
        @JsonProperty("price")
        BigDecimal price,
        @Schema(name = "category", description = "Categoria do produto")
        @JsonProperty("category")
        String category,
        @Schema(name = "quantity", description = "Quantidade total de produtos")
        @JsonProperty("quantity")
        Integer quantity
) {
    public static ProductOutput convertEntity(Product product) {
        return ProductOutput.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .quantity(product.getQuantity())
                .build();
    }
}
