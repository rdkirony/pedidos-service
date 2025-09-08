package br.com.gerenciador.pedidos.core.records.output;

import br.com.gerenciador.pedidos.entitys.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "UserOutput", description = "Response da entidade de usuario")
public record UserOutput(
        @JsonProperty("id")
        @Schema(name = "id", description = "Id gerado no padrão UUID")
        String id,
        @JsonProperty("user_name")
        @Schema(name = "username", description = "Login do usuário cadastrado")
        String username,
        @JsonProperty("first_name")
        @Schema(name = "first_name", description = "Nome do usuário")
        String firstName,
        @JsonProperty("last_name")
        @Schema(name = "last_name", description = "Sobrenome do usuário")
        String lastName,
        @JsonProperty("role")
        @Schema(name = "role", description = "Perfil do usuário")
        String role
) {

    public static UserOutput convertEntity(User user) {
        return UserOutput.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }

}
