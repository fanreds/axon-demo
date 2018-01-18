package pl.start.your.life.handler;

import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.start.your.life.domain.Account;
import pl.start.your.life.event.AccountCreatedEvent;
import pl.start.your.life.event.DecreasedBalanceAccountEvent;
import pl.start.your.life.event.IncreasedBalanceAccountEvent;
import pl.start.your.life.exception.EntityNotExist;
import pl.start.your.life.repository.AccountJpaRepository;

@Component
@Transactional
public class AccountEventHandler {

    @Autowired
    private AccountJpaRepository accountRepository;

    @EventHandler
    @Order(52)
    public void on2(IncreasedBalanceAccountEvent event) {
        Account account = ofNullable(accountRepository.findOne(event.getAccountId())).orElseThrow(EntityNotExist::new);
        account.setBalance(account.getBalance() + event.getAmount());
        account.setId(event.getAccountId());
        System.out.println("@EventHandler IncreasedBalanceAccountEvent, my balance is " + account.getBalance());
    }

    @EventHandler
    public void on(DecreasedBalanceAccountEvent event) {
        Account account = ofNullable(accountRepository.findOne(event.getAccountId())).orElseThrow(EntityNotExist::new);
        account.setBalance(account.getBalance() - event.getAmount());
        account.setId(event.getAccountId());
        System.out.println("@EventHandler DicreasedBalanceAccountEvent, my balance is " + account.getBalance());
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        Optional<Account> optionalAccount = ofNullable(accountRepository.findOne(event.getAccountId()));
        if (!optionalAccount.isPresent()) {
            Account account = new Account();
            account.setBalance(event.getBalance());
            account.setId(event.getAccountId());
            accountRepository.save(account);
        }
        System.out.println("@EventHandler AccountCreatedEvent, my balance is " + event.getBalance());
    }
}
