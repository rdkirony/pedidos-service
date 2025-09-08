package br.com.gerenciador.pedidos.core.records.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(name = "TopBuyerOutput", description = "Response de usuarios que mais compraram")
@JsonIgnoreProperties(ignoreUnknown = true)
public record TopBuyerOutput(
        @Schema(name = "user_id", description = "Id do usuario")
        @JsonProperty("user_id")
        String userId,
        @Schema(name = "total_spent", description = "Total gasto")
        @JsonProperty("total_spent")
        BigDecimal totalSpent
) {
}
