package pl.start.your.life.config;

import static java.util.Optional.ofNullable;
import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

import java.util.Optional;
import java.util.concurrent.Callable;

import org.axonframework.commandhandling.model.LockingRepository;
import org.axonframework.commandhandling.model.inspection.AnnotatedAggregate;
import org.axonframework.eventhandling.EventBus;
import org.springframework.data.jpa.repository.JpaRepository;

import pl.start.your.life.domain.JPAEntity;
import pl.start.your.life.event.AggregateCreatedEvent;
import pl.start.your.life.mapper.DataModelMapper;

public class CustomAxonRepository<T, E extends JPAEntity<Integer>, R extends JpaRepository<E, Integer>, C extends DataModelMapper<T, E>> extends LockingRepository<T, AnnotatedAggregate<T>> {

    private final EventBus eventBus;
    private R repository;
    private C converter;

    public CustomAxonRepository(Class<T> aggregateType, EventBus eventBus, R repository, C converter) {
        super(aggregateType);
        this.eventBus = eventBus;
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    protected AnnotatedAggregate<T> doCreateNewForLock(Callable<T> factoryMethod) throws Exception {
        return AnnotatedAggregate.initialize(factoryMethod.call(), aggregateModel(), eventBus);
    }

    @Override
    protected void doSaveWithLock(AnnotatedAggregate<T> aggregate) {
        E e = converter.toEntity(aggregate.getAggregateRoot());
        Optional<Integer> optionalId = ofNullable(e.getId());
        E saved = repository.save(e);
        if (!optionalId.isPresent()) {
            eventBus.publish(asEventMessage(new AggregateCreatedEvent(saved.getId(), aggregate.getAggregateRoot())));
        }
    }

    @Override
    protected void doDeleteWithLock(AnnotatedAggregate<T> aggregate) {
        repository.delete(Integer.valueOf((String) aggregateModel().getIdentifier(aggregate.getAggregateRoot())));
    }

    @Override
    protected AnnotatedAggregate<T> doLoadWithLock(String aggregateIdentifier, Long expectedVersion) {
        E entity = repository.findOne(Integer.valueOf(aggregateIdentifier));
        return AnnotatedAggregate.initialize(converter.toAggregate(entity, aggregateIdentifier), aggregateModel(), eventBus);
    }
}
