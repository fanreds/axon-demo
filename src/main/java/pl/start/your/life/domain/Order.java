package pl.start.your.life.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventhandling.EventHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.event.OrderCreatedEvent;

@Entity
@Table(name = "ORDERS")
@Data
@NoArgsConstructor
@AggregateRoot
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    @AggregateIdentifier
    private Integer id;

    private Integer price;
    private Integer accountId;

    public void orderCreated(OrderCreateCommand command) {
        apply(new OrderCreatedEvent(command.getOrderId(), command.getPrice(), command.getAccountId()));
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        System.out.println("on source OrderCreatedEvent");
        this.id = event.getOrderId();
        this.price = event.getPrice();
        this.accountId = event.getAccountId();
    }

}
