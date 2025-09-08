package br.com.gerenciador.pedidos.core.records.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(name = "MonthlyRevenueOutput", description = "Response quee represente o total a ser faturado")
@JsonIgnoreProperties(ignoreUnknown = true)
public record MonthlyRevenueOutput(
        @Schema(name = "year", description = "Ano")
        @JsonProperty("year")
        int year,
        @Schema(name = "month", description = "Mês")
        @JsonProperty("month")
        int month,
        @Schema(name = "revenue", description = "Total a ser faturado")
        @JsonProperty("revenue")
        BigDecimal revenue
) {
}
