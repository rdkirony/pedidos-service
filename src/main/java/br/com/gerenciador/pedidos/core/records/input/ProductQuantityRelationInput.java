package br.com.gerenciador.pedidos.core.records.input;

import br.com.gerenciador.pedidos.entitys.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record ProductQuantityRelationInput(
        @NotBlank
        @Schema(name = "product_id", description = "Id do produto")
        @JsonProperty("product_id")
        @NotNull
        String productId,
        @NotNull
        @Min(1)
        @Schema(name = "quantity", description = "Quantidade de itens para o produto")
        @JsonProperty("quantity")
        Integer quantity
) {
    public static ProductQuantityRelationInput buildFromProduct(String productId, Integer quantity) {
        return ProductQuantityRelationInput.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }

}
