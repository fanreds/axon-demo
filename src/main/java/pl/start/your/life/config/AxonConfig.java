package pl.start.your.life.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.model.GenericJpaRepository;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.spring.config.EnableAxon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.start.your.life.aggregate.AccountAggregate;
import pl.start.your.life.aggregate.OrderAggregate;

@EnableAxon
@Configuration
public class AxonConfig extends JpaConfig {

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public CommandBus commandBus() {
        return new SimpleCommandBus();
    }

    @Bean
    public Repository<OrderAggregate> orderAggregateRepository(EntityManagerProvider entityManagerProvider) {
        return new GenericJpaRepository<>(entityManagerProvider, OrderAggregate.class, eventBus());
    }

    @Bean
    public Repository<AccountAggregate> accountAggregateRepository(EntityManagerProvider entityManagerProvider) {
        return new GenericJpaRepository<>(entityManagerProvider, AccountAggregate.class, eventBus());
    }
}
