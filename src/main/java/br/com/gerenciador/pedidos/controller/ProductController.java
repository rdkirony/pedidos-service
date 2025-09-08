package br.com.gerenciador.pedidos.controller;

import br.com.gerenciador.pedidos.core.records.input.ProductInput;
import br.com.gerenciador.pedidos.core.records.output.PageResultOutput;
import br.com.gerenciador.pedidos.core.records.output.ProductOutput;
import br.com.gerenciador.pedidos.core.utils.Util;
import br.com.gerenciador.pedidos.entitys.Product;
import br.com.gerenciador.pedidos.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/product")
@Tag(
        name = "ProductController",
        description = "API de gerenciamento de produtos"
)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @Operation(
            summary = "register",
            description = "Registra um Produto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso")
            }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductOutput> register(@RequestBody @Valid ProductInput request) {
        final var product = service.register(request);
        return ResponseEntity.ok(ProductOutput.convertEntity(product));
    }

    @Operation(
            summary = "update",
            description = "Atualiza um Produto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso")
            }
    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductOutput> update(final @PathVariable String id, @RequestBody @Valid ProductInput request) {
        final Product productDb = service.findById(id);
        final var product = service.update(productDb, request);
        return ResponseEntity.ok(ProductOutput.convertEntity(product));
    }

    @Operation(
            summary = "findById",
            description = "Consulta um Produto pelo id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductOutput> findById(final @PathVariable String id) {
        final var product = service.findById(id);
        return ResponseEntity.ok(ProductOutput.convertEntity(product));
    }

    @Operation(
            summary = "removeById",
            description = "Excluír um Produto pelo id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto excluído com sucesso")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(final @PathVariable String id) {
        service.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "findProducts",
            description = "Consulta paginada de Produtos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso")
            }
    )
    @GetMapping
    public ResponseEntity<PageResultOutput<ProductOutput>> findProducts(
            @Parameter(description = "Nome do produto para filtro", example = "Livro")
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @Parameter(description = "Número da página (inicia em 0)", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Quantidade de registros por página", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "nome")
            @RequestParam(value = "sortField", defaultValue = "id") String sortField,
            @Parameter(description = "Direção da ordenação (ASC ou DESC)", example = "ASC")
            @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder
    ) {
        final var pageable = Util.getPageable(page, size, sortField, sortOrder);
        final var listProducts = service.findByNameContainingIgnoreCase(name, pageable);
        final var pageProducts = new PageImpl<>(
                listProducts.stream()
                        .map(ProductOutput::convertEntity)
                        .collect(Collectors.toList()),
                pageable,
                listProducts.getTotalElements()
        );
        return ResponseEntity.ok(PageResultOutput.from(pageProducts));
    }

}
