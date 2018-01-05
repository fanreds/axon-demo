package pl.start.your.life.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import pl.start.your.life.domain.Order;
import pl.start.your.life.dto.OrderDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "orderId", source = "id")
    OrderDto toDto(Order product);

    @InheritInverseConfiguration
    Order toEntity(OrderDto product);
}
