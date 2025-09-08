package br.com.gerenciador.pedidos.core.records.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserLoginOut", description = "Response do login")
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserLoginOutput(

        @JsonProperty("access_token")
        @Schema(name = "access_token", description = "Token do usuario")
        String accessToken,
        @JsonProperty("refresh_token")
        @Schema(name = "refresh_token", description = "Refresh token")
        String refreshToken

) { }
