package br.com.gerenciador.pedidos.controller;

import br.com.gerenciador.pedidos.core.records.output.AvgTicketOutput;
import br.com.gerenciador.pedidos.core.records.output.MonthlyRevenueOutput;
import br.com.gerenciador.pedidos.core.records.output.TopBuyerOutput;
import br.com.gerenciador.pedidos.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@Tag(
        name = "AnalyticsController",
        description = "API para consulta de análises"
)
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService service;

    @Operation(
            summary = "topBuyers",
            description = "Top 5 usuários que mais compraram",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso")
            }
    )
    @GetMapping("/top-buyers")
    public ResponseEntity<List<TopBuyerOutput>> topBuyers(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(service.topBuyers(limit));
    }


    @Operation(
            summary = "avgTicketByUser",
            description = "Ticket médio dos pedidos de cada usuário",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso")
            }
    )
    @GetMapping("/avg-ticket-by-user")
    public ResponseEntity<List<AvgTicketOutput>> avgTicketByUser() {
        return ResponseEntity.ok(service.avgTicketPerUser());
    }

    @Operation(
            summary = "monthlyRevenue",
            description = "Valor total faturado no mês",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso")
            }
    )
    @GetMapping("/monthly-revenue")
    public ResponseEntity<MonthlyRevenueOutput> monthlyRevenue(@RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(service.monthRevenue(year, month));
    }
}
