package pl.start.your.life.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.spring.config.EnableAxon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.start.your.life.aggregate.AccountAggregate;
import pl.start.your.life.aggregate.OrderAggregate;
import pl.start.your.life.mapper.AccountMapper;
import pl.start.your.life.mapper.OrderMapper;
import pl.start.your.life.repository.AccountRepository;
import pl.start.your.life.repository.OrderRepository;

@EnableAxon
@Configuration
public class AxonConfig extends JpaConfig {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public CommandBus commandBus() {
        return new SimpleCommandBus();
    }

    @Bean
    public Repository<OrderAggregate> orderAggregateRepository() {
        return new CustomAxonRepository<>(OrderAggregate.class, eventBus(), orderRepository, OrderMapper.MAPPER);
    }

    @Bean
    public Repository<AccountAggregate> accountAggregateRepository() {
        return new CustomAxonRepository<>(AccountAggregate.class, eventBus(), accountRepository, AccountMapper.MAPPER);
    }
}
