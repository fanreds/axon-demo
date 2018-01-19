package pl.start.your.life.handler;

import static java.util.Optional.ofNullable;
import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

import java.util.Optional;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import pl.start.your.life.aggregate.OrderAggregate;
import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.command.PaymentCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.event.OrderCreatedEvent;
import pl.start.your.life.exception.EntityNotExist;
import pl.start.your.life.exception.LimitCashExceededException;
import pl.start.your.life.repository.AccountRepository;

@Component
@NoArgsConstructor
public class OrderCommandHandler {
    @Autowired
    private Repository<OrderAggregate> orderAggregateRepository;
    private AccountRepository accountRepository;
    @Autowired
    private EventBus eventBus;

    @CommandHandler
    public void handle(OrderCreateCommand command) throws Exception {
        System.out.println("on command create order");
        OrderAggregate order = new OrderAggregate();
        String uuid = UUID.randomUUID().toString();
        order.setId(uuid);
        order.setPrice(command.getPrice());
        order.setAccountId(command.getAccountId());
        order.setApproved(false);
        order.setCanceled(false);
        order.setPayed(false);

        orderAggregateRepository.newInstance(() -> order);

        OrderCreatedEvent event = new OrderCreatedEvent(uuid, command.getPrice(), command.getAccountId());
        eventBus.publish(asEventMessage(event));
    }

    @CommandHandler
    public void handle(PaymentCommand command) {
        Optional<Account> accountOptional = ofNullable(accountRepository.findOne(command.getAccountId()));
        Account account = accountOptional.orElseThrow(EntityNotExist::new);
        if (command.getPrice() > account.getBalance()) {
            throw new LimitCashExceededException();
        }
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

}
