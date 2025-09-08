package br.com.gerenciador.pedidos.core.records.output;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorOutput {
    private String message;
    private String path;
    private Integer status;
    private String error;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
