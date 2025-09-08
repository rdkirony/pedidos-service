package br.com.gerenciador.pedidos.enums;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum OrderStatus {
    PENDING(1),
    PAID(2),
    CANCELED(3);

    private final int id;

    OrderStatus(int id) {
        this.id = id;
    }

    public static OrderStatus getById(int id) {
        return Stream.of(OrderStatus.values())
                .filter(e -> id == e.getId()).findAny()
                .orElseThrow(() -> new EnumConstantNotPresentException(OrderStatus.class, String.valueOf(id)));
    }
}
