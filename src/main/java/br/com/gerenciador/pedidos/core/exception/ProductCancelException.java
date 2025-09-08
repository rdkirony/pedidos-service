package br.com.gerenciador.pedidos.core.exception;

public class ProductCancelException extends RuntimeException {
    public ProductCancelException(String message) {
        super(message);
    }
}
