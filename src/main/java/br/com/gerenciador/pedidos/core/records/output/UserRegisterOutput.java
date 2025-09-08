package br.com.gerenciador.pedidos.core.records.output;

import br.com.gerenciador.pedidos.entitys.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Schema(name = "UserRegisterOut", description = "Response do cadastro de usuários")
public record UserRegisterOutput(

        @JsonProperty("id")
        @Schema(name = "id", description = "Id gerado no padrão UUID")
        String id,
        @JsonProperty("user_name")
        @Schema(name = "username", description = "Login do usuário cadastrado")
        String username
) {
    public static UserRegisterOutput convertEntity(User user) {
        return UserRegisterOutput.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
