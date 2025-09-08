package br.com.gerenciador.pedidos.core.records.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "UserRegisterIn", description = "Payload de entrada para o cadastro de um usuário")
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserRegisterInput(
        @NotBlank
        @Schema(name = "username", description = "Login para acesso", example = "admin")
        @JsonProperty("username")
        String userName,
        @NotBlank
        @Schema(name = "first_name", description = "Nome do usuário", example = "Admin")
        @JsonProperty("first_name")
        String firstName,
        @Schema(name = "last_name", description = "Último nome do usuário", example = "Super usuario")
        @JsonProperty("last_name")
        String lastName,
        @NotBlank
        @Schema(name = "password", description = "Senha de registro", example = "admin")
        @JsonProperty("password")
        String password,
        @NotNull
        @Schema(name = "role", description = "1 - USER, 2 - ADMIN", example = "2")
        @JsonProperty("role")
        Integer role
){}
