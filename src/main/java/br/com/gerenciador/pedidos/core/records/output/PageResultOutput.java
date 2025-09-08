package br.com.gerenciador.pedidos.core.records.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Resposta paginada")
public record PageResultOutput<T>(

        @Schema(name = "items", description = "Lista de itens da página")
        @JsonProperty("items")
        List<T> items,

        @Schema(name = "total", description = "Total de registros")
        @JsonProperty("total")
        long total,

        @Schema(name = "page", description = "Número da página atual")
        @JsonProperty("page")
        int page,

        @Schema(name = "size", description = "Tamanho da página")
        @JsonProperty("size")
        int size
) {
    public static <T> PageResultOutput<T> from(Page<T> page) {
        return new PageResultOutput<>(
                page.getContent(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }
}