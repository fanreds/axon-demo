package pl.start.your.life.mapper;

import org.mapstruct.InheritInverseConfiguration;

public interface DataModelMapper<A, E> {
    A toAggregate(E e, String uuid);

    @InheritInverseConfiguration
    E toEntity(A a);
}
