package br.com.gerenciador.pedidos.core.records.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(name = "OrderInput", description = "Payload de entrada para o cadastro de um pedido")
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderInput(
        @Schema(name = "products", description = "Produtos do pedido")
        @JsonProperty("products")
        @NotEmpty
        List<ProductQuantityRelationInput> products
) {
}
