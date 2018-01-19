package pl.start.your.life.handler;


import static java.util.Optional.ofNullable;
import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;
import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

import java.util.Optional;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.NoArgsConstructor;
import pl.start.your.life.aggregate.AccountAggregate;
import pl.start.your.life.command.AccountCreateCommand;
import pl.start.your.life.command.MoneyTransferCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.event.AccountCreatedEvent;
import pl.start.your.life.event.IncreasedBalanceAccountEvent;
import pl.start.your.life.repository.AccountRepository;

@Component
@NoArgsConstructor
@Transactional
public class AccountCommandHandler {

    private Repository<AccountAggregate> accountAggregateRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EventBus eventBus;
    @Autowired
    private CommandBus commandBus;

    @CommandHandler
    public void handle(AccountCreateCommand command) throws Exception {
        accountAggregateRepository.newInstance(() -> new AccountAggregate(command.getAccountId().toString(), command.getBalance()));
        eventBus.publish(asEventMessage(new AccountCreatedEvent(command.getAccountId(), command.getBalance())));
    }

    @CommandHandler
    public void handle(MoneyTransferCommand command) {
        System.out.println("on command MoneyTransferCommand");
        Optional<Account> accountOptional = ofNullable(accountRepository.findOne(command.getAccountId()));
        if (!accountOptional.isPresent()) {
            commandBus.dispatch(asCommandMessage(new AccountCreateCommand(command.getAccountId(), 0)), new CommandCallback<Object, Object>() {
                @Override
                public void onSuccess(CommandMessage<?> commandMessage, Object result) {
                    eventBus.publish(asEventMessage(new IncreasedBalanceAccountEvent(command.getAccountId(), command.getAmount())));
                }

                @Override
                public void onFailure(CommandMessage<?> commandMessage, Throwable cause) {
                    cause.printStackTrace();
                }
            });
        } else {
            Aggregate<AccountAggregate> accountAggregate = accountAggregateRepository.load(command.getAccountId().toString());
            accountAggregate.execute(e -> {
                e.setBalance(e.getBalance() + command.getAmount());
            });
            eventBus.publish(asEventMessage(new IncreasedBalanceAccountEvent(command.getAccountId(), command.getAmount())));
        }
    }

    @Autowired
    public void setAccountAggregateRepository(Repository<AccountAggregate> accountAggregateRepository) {
        this.accountAggregateRepository = accountAggregateRepository;
    }
}
