package br.com.gerenciador.pedidos.core.records.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginIn", description = "Payload para login de um usuário")
@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginInput(
        @NotBlank
        @Schema(name = "username", description = "Login para acesso")
        @JsonProperty("username")
        String userName,
        @NotBlank
        @Schema(name = "password", description = "Senha do usuário")
        @JsonProperty("password")
        String password
) {
}
