package pl.start.your.life.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CurrentUnitOfWorkParameterResolverFactory;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.NoCache;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.NoSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.messaging.annotation.ParameterResolver;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.spring.config.annotation.SpringBeanParameterResolverFactory;
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import pl.start.your.life.domain.Account;
import pl.start.your.life.domain.Order;
import pl.start.your.life.handler.AccountHandler;
import pl.start.your.life.handler.OrderHandler;

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
    @Scope("prototype")
    public Order order() {
        return new Order();
    }

    @Bean
    @Scope("prototype")
    public Account account() {
        return new Account();
    }

    @Bean
    public AggregateFactory<Order> orderAggregateFactory() {
        SpringPrototypeAggregateFactory<Order> factory = new SpringPrototypeAggregateFactory<>();
        factory.setPrototypeBeanName("order");
        return factory;
    }

    @Bean
    public AggregateFactory<Account> accountAggregateFactory() {
        SpringPrototypeAggregateFactory<Account> factory = new SpringPrototypeAggregateFactory<>();
        factory.setPrototypeBeanName("account");
        return factory;
    }

    @Bean
    public Repository<Order> orderRepository() {
        return new CachingEventSourcingRepository<>(orderAggregateFactory(), eventStore(), cache(), NoSnapshotTriggerDefinition.INSTANCE);
    }

    @Bean
    public Repository<Account> accountRepository() {
        return new CachingEventSourcingRepository<>(accountAggregateFactory(), eventStore(), cache(), NoSnapshotTriggerDefinition.INSTANCE);
    }

    @Bean
    public OrderHandler orderHandler() {
        OrderHandler orderHandler = new OrderHandler();
        orderHandler.setOrderRepository(orderRepository());
        return orderHandler;
    }

    @Bean
    public AccountHandler accountHandler() {
        AccountHandler accountHandler = new AccountHandler();
        accountHandler.setRepository(accountRepository());
        return accountHandler;
    }

    @Bean
    public CommandBus commandBus() {
        return new SimpleCommandBus();
    }

}
