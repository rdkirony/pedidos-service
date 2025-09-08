package br.com.gerenciador.pedidos.core.records.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "PaymentResultOutput", description = "Response ao realizar o pagamento de um pedido")
@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentResultOutput(
        @Schema(name = "mensagem", description = "Mensagem referente ao pagamento do pedido")
        @JsonProperty("mensagem")
        String mensagem,
        @Schema(name = "sucesso", description = "Indica se o pedido foi processado corretamente")
        @JsonProperty("sucesso")
        boolean sucesso
) {
}
