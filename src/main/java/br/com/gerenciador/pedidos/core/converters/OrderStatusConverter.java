package br.com.gerenciador.pedidos.core.converters;

import br.com.gerenciador.pedidos.enums.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OrderStatus attribute) {
        if (attribute == null)
            return null;
        return attribute.getId();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer id) {
        if (id == null)
            return null;
        return OrderStatus.getById(id);
    }
}
