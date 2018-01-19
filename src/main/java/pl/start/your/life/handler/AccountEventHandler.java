package pl.start.your.life.handler;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.start.your.life.aggregate.AccountAggregate;
import pl.start.your.life.event.AccountCreatedEvent;
import pl.start.your.life.event.DecreasedBalanceAccountEvent;
import pl.start.your.life.event.IncreasedBalanceAccountEvent;

@Component
@Transactional
public class AccountEventHandler {

    @Autowired
    private Repository<AccountAggregate> accountAggregateRepository;

    @EventHandler
    public void on(IncreasedBalanceAccountEvent event) {
        System.out.println("@EventHandler IncreasedBalanceAccountEvent " + event.toString());
        accountAggregateRepository.load(event.getAccountId().toString()).execute(e -> {
            e.setBalance(e.getBalance() + event.getAmount());
        });
    }

    @EventHandler
    public void on(DecreasedBalanceAccountEvent event) {
        System.out.println("@EventHandler DicreasedBalanceAccountEvent " + event.toString());
        accountAggregateRepository.load(event.getAccountId().toString()).execute(e -> {
            e.setBalance(e.getBalance() - event.getAmount());
        });
    }

    @EventHandler

    public void on(AccountCreatedEvent event) {
        System.out.println("@EventHandler AccountCreatedEvent " + event.toString());
    }
}
