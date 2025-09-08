package br.com.gerenciador.pedidos.controller;

import br.com.gerenciador.pedidos.core.records.input.UserRegisterInput;
import br.com.gerenciador.pedidos.core.records.output.UserOutput;
import br.com.gerenciador.pedidos.core.records.output.UserRegisterOutput;
import br.com.gerenciador.pedidos.entitys.User;
import br.com.gerenciador.pedidos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(
        name = "UserController",
        description = "API de gerenciamento de usuários"
)
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(
            summary = "register",
            description = "Registra um usuário",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso")
            }
    )
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRegisterOutput> register(@RequestBody @Valid UserRegisterInput request) {
        final var user = service.register(new User(request));
        return ResponseEntity.ok(UserRegisterOutput.convertEntity(user));
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserOutput> findById(final @PathVariable String id) {
        final var user = service.findById(id);
        return ResponseEntity.ok(UserOutput.convertEntity(user));

    }

}
