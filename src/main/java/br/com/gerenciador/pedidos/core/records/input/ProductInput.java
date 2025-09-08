package br.com.gerenciador.pedidos.core.records.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "ProductInput", description = "Payload de entrada para o cadastro/atualizacao de um produto")
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductInput(
        @NotBlank
        @Schema(name = "name", description = "Nome do produto")
        @JsonProperty("name")
        String name,
        @Schema(name = "description", description = "Descrição do produto")
        @JsonProperty("description")
        String description,
        @NotNull
        @Schema(name = "price", description = "Preço por unidade")
        @JsonProperty("price")
        BigDecimal price,
        @NotBlank
        @Schema(name = "category", description = "Categoria do produto")
        @JsonProperty("category")
        String category,
        @NotNull
        @Schema(name = "quantity", description = "Quantidade total de produtos")
        @JsonProperty("quantity")
        Integer quantity
) {
}
