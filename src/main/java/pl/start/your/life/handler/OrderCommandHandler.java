package pl.start.your.life.handler;

import static java.util.Optional.ofNullable;
import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

import java.util.Optional;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.command.PaymentCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.event.DecreasedBalanceAccountEvent;
import pl.start.your.life.event.OrderCreatedEvent;
import pl.start.your.life.event.PaymentAcceptedEvent;
import pl.start.your.life.exception.EntityNotExist;
import pl.start.your.life.exception.LimitCashExceededException;
import pl.start.your.life.repository.AccountJpaRepository;

@Component
@NoArgsConstructor
public class OrderCommandHandler {

    private AccountJpaRepository accountRepository;
    @Autowired
    private EventBus eventBus;

    @CommandHandler
    public void handle(OrderCreateCommand command) {
        System.out.println("on command create order");
        eventBus.publish(asEventMessage(new OrderCreatedEvent(command.getPrice(), command.getAccountId())));
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
    public void setAccountRepository(AccountJpaRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

}
