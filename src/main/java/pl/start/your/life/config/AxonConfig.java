package pl.start.your.life.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CurrentUnitOfWorkParameterResolverFactory;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.NoCache;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.lock.PessimisticLockFactory;
import org.axonframework.config.Configurer;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.NoSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.messaging.annotation.ParameterResolver;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.spring.config.CommandHandlerSubscriber;
import org.axonframework.spring.config.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.spring.config.annotation.SpringBeanParameterResolverFactory;
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.start.your.life.aggregate.Pay;
import pl.start.your.life.domain.Order;
import pl.start.your.life.domain.OrderHandler;
import pl.start.your.life.handler.PayHandler;

@Configuration
public class AxonConfig {

    @Bean
    public EntityManagerProvider entityManagerProvider() {
        return new ContainerManagedEntityManagerProvider();
    }

    @Bean
    public EventStore eventStore() {
        return new EmbeddedEventStore(eventStorageEngine());
    }

    @Bean
    public ParameterResolver springBeanParameterResolver() {
        return new CurrentUnitOfWorkParameterResolverFactory();
    }

    @Bean
    public EventStorageEngine eventStorageEngine() {
        return new InMemoryEventStorageEngine();
    }

    @Bean
    public Cache cache() {
        return NoCache.INSTANCE;
    }

    @Bean
    public ParameterResolverFactory parameterResolverFactory() {
        return new SpringBeanParameterResolverFactory();
    }

    @Bean
    public AggregateFactory<Order> orderAggregateFactory() {
        SpringPrototypeAggregateFactory<Order> factory = new SpringPrototypeAggregateFactory<>();
        factory.setPrototypeBeanName("order");
        return factory;
    }

    @Bean
    public AggregateFactory<Pay> payAggregateFactory() {
        SpringPrototypeAggregateFactory<Pay> factory = new SpringPrototypeAggregateFactory<>();
        factory.setPrototypeBeanName("pay");
        return factory;
    }

    @Bean
    public Configurer configurer(Configurer configurer) {
        configurer.registerCommandHandler(c -> orderHandler());
        configurer.registerCommandHandler(c -> new PayHandler());
        return configurer;
    }

    @Bean
    public OrderHandler orderHandler() {
        return new OrderHandler();
    }

//    @Bean
//    public AggregateAnnotationCommandHandler commandHandler() {
//        return new AggregateAnnotationCommandHandler<>(Order.class, jpaOrderRepository());
//    }

    @Bean
    public Repository<Order> jpaOrderRepository() {
        return new CachingEventSourcingRepository<>(orderAggregateFactory(), eventStore(), new PessimisticLockFactory(), cache(), parameterResolverFactory(), NoSnapshotTriggerDefinition.INSTANCE);
    }

    @Bean
    public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor() {
        return new AnnotationCommandHandlerBeanPostProcessor();
    }

    @Bean
    public CommandHandlerSubscriber commandHandlerSubscriber() {
        return new CommandHandlerSubscriber();
    }

    @Bean
    public Repository<Pay> payRepository() {
        return new CachingEventSourcingRepository<>(payAggregateFactory(), eventStore(), cache());
    }

    @Bean
    public CommandBus commandBus() {
        return new SimpleCommandBus();
    }

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }
}
