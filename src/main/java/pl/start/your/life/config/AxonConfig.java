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

import pl.start.your.life.domain.Account;
import pl.start.your.life.domain.Order;
import pl.start.your.life.handler.AccountCommandHandler;
import pl.start.your.life.handler.OrderCommandHandler;

@EnableAxon
@Configuration
public class AxonConfig extends JpaConfig {

    @Bean
    public Repository<Order> orderRepository(EntityManagerProvider entityManagerProvider) {
        return new GenericJpaRepository<>(entityManagerProvider, Order.class, eventBus());
    }

    @Bean
    public Repository<Account> accountRepository(EntityManagerProvider entityManagerProvider) {
        return new GenericJpaRepository<>(entityManagerProvider, Account.class, eventBus());
    }

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public OrderCommandHandler orderHandler() {
        return new OrderCommandHandler();
    }

    @Bean
    public AccountCommandHandler accountHandler() {
        return new AccountCommandHandler();
    }

    @Bean
    public CommandBus commandBus() {
        return new SimpleCommandBus();
    }

}
