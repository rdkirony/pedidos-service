package br.com.gerenciador.pedidos.controller;

import br.com.gerenciador.pedidos.core.records.input.OrderInput;
import br.com.gerenciador.pedidos.core.records.output.OrderOutput;
import br.com.gerenciador.pedidos.core.records.output.PageResultOutput;
import br.com.gerenciador.pedidos.core.utils.Util;
import br.com.gerenciador.pedidos.service.OrderService;
import br.com.gerenciador.pedidos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
@Tag(
        name = "OrderController",
        description = "API de gerenciamento de pedidos"
)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final UserService userService;

    @Operation(
            summary = "register",
            description = "Registra um pedido",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedido cadastrado com sucesso")
            }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderOutput> register(@RequestBody @Valid OrderInput request, @AuthenticationPrincipal UserDetails userDetails) {
        final var user = userService.findByUsername(userDetails.getUsername());
        final var resp = service.createOrder(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @Operation(
            summary = "cancelIfPending",
            description = "Cancela um pedido",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Pedido cancelado com sucesso")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<OrderOutput> cancelIfPending(@PathVariable String id) {
        final var response = service.cancelIfPending(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<PageResultOutput<OrderOutput>> findProducts(
            @Parameter(description = "Número da página (inicia em 0)", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Quantidade de registros por página", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "nome")
            @RequestParam(value = "sortField", defaultValue = "id") String sortField,
            @Parameter(description = "Direção da ordenação (ASC ou DESC)", example = "ASC")
            @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        final var pageable = Util.getPageable(page, size, sortField, sortOrder);
        final var user = userService.findByUsername(userDetails.getUsername());
        final var listProducts = service.findByUserId(user, pageable);
        final var pageProducts = new PageImpl<>(
                listProducts.stream()
                        .map(service::buildOrderOutput)
                        .collect(Collectors.toList()),
                pageable,
                listProducts.getTotalElements()
        );
        return ResponseEntity.ok(PageResultOutput.from(pageProducts));
    }
}
