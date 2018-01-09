package pl.start.your.life.handler;


import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import pl.start.your.life.command.AccountCreateCommand;
import pl.start.your.life.command.MoneyTransferCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.repository.AccountJpaRepository;

@Component
@NoArgsConstructor
public class AccountHandler {
    private Repository<Account> repository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private CommandBus commandBus;

    @CommandHandler
    public void handle(AccountCreateCommand command) throws Exception {
        final Account account = new Account(command.getAccountId(), command.getBalance());
        Aggregate<Account> accountAggregate = repository.newInstance(() -> account);
        accountAggregate.execute(e -> e.applyCreatedAccountEvent(command));
        accountJpaRepository.save(account);
    }

    @CommandHandler
    public void handle(MoneyTransferCommand command) throws Exception {
        System.out.println("on command MoneyTransferCommand");
        try {
            makeTransferForAccount(command);
        } catch (AggregateNotFoundException e) {
            createAccount(command.getAccountId(), 0);
            makeTransferForAccount(command);
        }
    }

    private void makeTransferForAccount(MoneyTransferCommand command) {
        Aggregate<Account> account = repository.load(command.getAccountId().toString());
        account.execute(e -> e.applyIncreasedBalanceEvent(command));
    }

    @Autowired
    @Qualifier(value = "accountRepository")
    public void setRepository(Repository<Account> repository) {
        this.repository = repository;
    }

    private void createAccount(Integer accountId, Integer balance) {
        commandBus.dispatch(asCommandMessage(new AccountCreateCommand(accountId, balance)), LoggingCallback.INSTANCE);
    }
}
