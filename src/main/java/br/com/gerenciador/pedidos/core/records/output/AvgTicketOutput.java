package br.com.gerenciador.pedidos.core.records.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(name = "AvgTicketOutput", description = "Response  do ticket médio de cada usuário")
@JsonIgnoreProperties(ignoreUnknown = true)
public record AvgTicketOutput(
        @Schema(name = "user_id", description = "Id do usuario")
        @JsonProperty("user_id")
        String userId,
        @Schema(name = "avg_ticket", description = "Ticket médio")
        @JsonProperty("avg_ticket")
        BigDecimal avgTicket
) {
}
