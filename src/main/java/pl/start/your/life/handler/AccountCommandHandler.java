package pl.start.your.life.handler;


import static java.util.Optional.ofNullable;
import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import pl.start.your.life.command.AccountCreateCommand;
import pl.start.your.life.command.MoneyTransferCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.exception.EntityNotExist;
import pl.start.your.life.repository.AccountRepository;

@Component
@NoArgsConstructor
public class AccountCommandHandler {
    private AccountRepository accountRepository;

    @Autowired
    private CommandBus commandBus;

    @CommandHandler
    public void handle(AccountCreateCommand command) {
        final Account account = new Account(command.getAccountId(), command.getBalance());
        Account loadedAccount = ofNullable(accountRepository.findOne(command.getAccountId())).orElseGet(() -> accountRepository.save(account));
        loadedAccount.applyCreatedAccountEvent(command);
    }

    @CommandHandler
    public void handle(MoneyTransferCommand command) throws Exception {
        System.out.println("on command MoneyTransferCommand");
        try {
            makeTransferForAccount(command);
        } catch (EntityNotExist e) {
            createAccount(command.getAccountId(), 0);
            makeTransferForAccount(command);
        }
    }

    private void makeTransferForAccount(MoneyTransferCommand command) {
        Account account = ofNullable(accountRepository.findOne(command.getAccountId())).orElseThrow(EntityNotExist::new);
        account.applyIncreasedBalanceEvent(command);
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private void createAccount(Integer accountId, Integer balance) {
        commandBus.dispatch(asCommandMessage(new AccountCreateCommand(accountId, balance)), LoggingCallback.INSTANCE);
    }
}
