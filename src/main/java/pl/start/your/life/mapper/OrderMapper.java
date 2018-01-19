package pl.start.your.life.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import pl.start.your.life.aggregate.OrderAggregate;
import pl.start.your.life.domain.Order;

@Mapper
public interface OrderMapper extends DataModelMapper<OrderAggregate, Order> {
    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    @Override
    @Mapping(target = "id", source = "orderId")
    Order toEntity(OrderAggregate orderAggregate);

    @Override
    @Mappings({
            @Mapping(target = "id", source = "uuid"),
            @Mapping(target = "orderId", expression = "java(workaroundMapstructBug(order))"),
    })
    OrderAggregate toAggregate(Order order, String uuid);

    default Integer workaroundMapstructBug(Order order) {
        return order.getId();
    }
}
