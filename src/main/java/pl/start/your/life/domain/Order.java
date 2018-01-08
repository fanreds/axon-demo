package pl.start.your.life.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventsourcing.EventSourcingHandler;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.command.PaymentCommand;
import pl.start.your.life.event.OrderApprovedEvent;
import pl.start.your.life.event.OrderCanceledEvent;
import pl.start.your.life.event.OrderCreatedEvent;
import pl.start.your.life.event.PaymentAcceptedEvent;

@Entity
@Table(name = "ORDERS")
@Data
@NoArgsConstructor
@AggregateRoot
@RequiredArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    @AggregateIdentifier
    @NonNull
    private Integer id;

    @NonNull
    private Integer price;
    @NonNull
    private Integer accountId;
    private Boolean payed;
    private Boolean canceled;
    private Boolean approved;

    public void applyOrderCreatedEvent(OrderCreateCommand command) {
        apply(new OrderCreatedEvent(command.getOrderId(), command.getPrice(), command.getAccountId()));
    }

    public void applyOrderCanceledEvent(PaymentCommand command) {
        apply(new OrderCanceledEvent(command.getOrderId(), command.getAccountId()));
    }

    public void applyPaymentAcceptedEvent(PaymentCommand command) {
        apply(new PaymentAcceptedEvent(command.getOrderId(), command.getAccountId()));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        System.out.println("on source OrderCreatedEvent");
        this.id = event.getOrderId();
        this.price = event.getPrice();
        this.accountId = event.getAccountId();
        this.payed = false;
        this.canceled = false;
        this.approved = false;
    }

    @EventSourcingHandler
    public void on(OrderCanceledEvent event) {
        System.out.println("on source OrderCanceledEvent");
        this.id = event.getOrderId();
        this.price = 0;
        this.accountId = event.getAccountId();
        this.canceled = true;
    }

    @EventSourcingHandler
    public void on(PaymentAcceptedEvent event) {
        System.out.println("on handler PaymentAcceptedEvent");
        this.id = event.getOrderId();
        this.accountId = event.getAccountId();
        this.payed = true;
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent event) {
        System.out.println("on handler PaymentAcceptedEvent");
        this.id = event.getOrderId();
        this.approved = true;
    }


    public void applyOrderApproveEvent(PaymentCommand command) {
        apply(new OrderApprovedEvent(command.getOrderId(), command.getAccountId()));
    }
}
