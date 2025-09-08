package br.com.gerenciador.pedidos.enums;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Role {
    USER(1),
    ADMIN(2);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    public static Role getById(int id) {
        return Stream.of(Role.values())
                .filter(e -> id == e.getId()).findAny()
                .orElseThrow(() -> new EnumConstantNotPresentException(Role.class, String.valueOf(id)));
    }
}
