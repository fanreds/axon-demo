package pl.start.your.life.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.event.OrderCreatedEvent;

@Entity
@Table(name = "ORDERS")
@Data
@NoArgsConstructor
@Aggregate(repository = "jpaOrderRepository")
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    @AggregateIdentifier
    private Integer id;

    private Integer price;
    private Integer accountId;

//    @CommandHandler
//    public Order(OrderCreateCommand command) {
//        apply(new OrderCreatedEvent(command.getOrderId(), command.getPrice(), command.getAccountId()));
//    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        System.out.println("on source OrderCreatedEvent");
        this.id = event.getOrderId();
    }

}
