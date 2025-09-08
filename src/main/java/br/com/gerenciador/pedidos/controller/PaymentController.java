package br.com.gerenciador.pedidos.controller;

import br.com.gerenciador.pedidos.core.records.output.PaymentResultOutput;
import br.com.gerenciador.pedidos.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@Tag(
        name = "PaymentController",
        description = "API de gerenciamento de pagamentos de pedidos"
)
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @Operation(
            summary = "pay",
            description = "Reliza o pagamento de um pedido",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Pedido debitado com sucesso")
            }
    )
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<PaymentResultOutput> pay(@PathVariable String orderId) {
        final var resp = service.pay(orderId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(resp);
    }
}
