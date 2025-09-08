package br.com.gerenciador.pedidos.core.converters;

import br.com.gerenciador.pedidos.enums.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role attribute) {
        if (attribute == null)
            return null;
        return attribute.getId();
    }

    @Override
    public Role convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }
        return Role.getById(id);
    }

}
