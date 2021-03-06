package pl.start.your.life.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventsourcing.EventSourcingHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.start.your.life.command.AccountCreateCommand;
import pl.start.your.life.command.MoneyTransferCommand;
import pl.start.your.life.event.AccountCreatedEvent;
import pl.start.your.life.event.DecreasedBalanceAccountEvent;
import pl.start.your.life.event.IncreasedBalanceAccountEvent;

@Entity
@Table(name = "ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@AggregateRoot
public class Account {
    @Id
    @AggregateIdentifier
    @GeneratedValue
    private Integer id;

    private Integer balance;

    @EventSourcingHandler
    public void on(IncreasedBalanceAccountEvent event) {
        this.id = event.getAccountId();
        this.balance += event.getAmount();
        System.out.println("@EventSourcingHandler IncreasedBalanceAccountEvent, my balance is " + this.balance);
    }

    @EventSourcingHandler
    public void on(DecreasedBalanceAccountEvent event) {
        this.id = event.getAccountId();
        this.balance -= event.getAmount();
        System.out.println("@EventSourcingHandler DicreasedBalanceAccountEvent, my balance is " + this.balance);
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.id = event.getAccountId();
        this.balance = event.getBalance();
        System.out.println("@EventSourcingHandler AccountCreatedEvent, my balance is " + event.getBalance());
    }

    public void applyCreatedAccountEvent(AccountCreateCommand command) {
        apply(new AccountCreatedEvent(command.getAccountId(), command.getBalance()));
    }

    public void applyIncreasedBalanceEvent(MoneyTransferCommand command) {
        apply(new IncreasedBalanceAccountEvent(command.getAccountId(), command.getAmount()));
    }

    public void applyDecreasedBalanceEvent(Integer accountId, Integer amount) {
        apply(new DecreasedBalanceAccountEvent(accountId, amount));
    }
}
