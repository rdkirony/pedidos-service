package br.com.gerenciador.pedidos.controller;

import br.com.gerenciador.pedidos.core.records.input.LoginInput;
import br.com.gerenciador.pedidos.core.records.output.UserLoginOutput;
import br.com.gerenciador.pedidos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "AuthController",
        description = "API de controle autenticações"
)
public class AuthControler {

    private final AuthService authService;

    public AuthControler(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "login",
            description = "Login de um usuário",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
            }
    )
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginOutput> login(@RequestBody LoginInput request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

}
