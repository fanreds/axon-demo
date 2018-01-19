package pl.start.your.life.handler;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;
import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.start.your.life.aggregate.OrderAggregate;
import pl.start.your.life.command.PaymentCommand;
import pl.start.your.life.event.AggregateCreatedEvent;
import pl.start.your.life.event.DecreasedBalanceAccountEvent;
import pl.start.your.life.event.OrderApprovedEvent;
import pl.start.your.life.event.OrderCanceledEvent;
import pl.start.your.life.event.OrderCreatedEvent;
import pl.start.your.life.event.PaymentAcceptedEvent;

@Component
@Transactional
public class OrderEventHandler {

    @Autowired
    private EventBus eventBus;
    @Autowired
    private CommandBus commandBus;
    @Autowired
    private Repository<OrderAggregate> orderAggregateRepository;

    @EventHandler
    public void on(AggregateCreatedEvent event) {
        event.getAggregate().setEntityId(event.getId());
    }

    @EventHandler
    public void on(OrderCanceledEvent event) {
        System.out.println("@EventSourcingHandler OrderCanceledEvent");
        Aggregate<OrderAggregate> aggregate = orderAggregateRepository.load(event.getAggIdentifier());
        aggregate.execute(order -> {
            order.setApproved(false);
            order.setCanceled(true);
        });
    }

    @EventHandler
    public void on(PaymentAcceptedEvent event) {
        System.out.println("@EventSourcingHandler PaymentAcceptedEvent");
        Aggregate<OrderAggregate> aggregate = orderAggregateRepository.load(event.getAggIdentifier());
        aggregate.execute(order -> {
            order.setAccountId(event.getAccountId());
            order.setPayed(true);
        });
    }

    @EventHandler
    public void on(OrderApprovedEvent event) {
        System.out.println("@EventSourcingHandler OrderApprovedEvent");
        Aggregate<OrderAggregate> aggregate = orderAggregateRepository.load(event.getAggIdentifier());
        aggregate.execute(order -> {
            order.setApproved(true);
        });
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        System.out.println("@EventSourcingHandler OrderCreatedEvent");
        commandBus.dispatch(asCommandMessage(new PaymentCommand(event.getAggIdentifier(), event.getAccountId(), event.getPrice())), new CommandCallback<PaymentCommand, Object>() {
            @Override
            public void onSuccess(CommandMessage<? extends PaymentCommand> commandMessage, Object result) {
                System.out.println("Payment command successful");
                PaymentCommand payload = commandMessage.getPayload();
                eventBus.publish(asEventMessage(new PaymentAcceptedEvent(event.getAggIdentifier(), payload.getAccountId())));
                eventBus.publish(asEventMessage(new DecreasedBalanceAccountEvent(payload.getAccountId(), payload.getPrice())));
                eventBus.publish(asEventMessage(new OrderApprovedEvent(event.getAggIdentifier(), payload.getAccountId())));
            }

            @Override
            public void onFailure(CommandMessage<? extends PaymentCommand> commandMessage, Throwable cause) {
                eventBus.publish(asEventMessage(new OrderCanceledEvent(event.getAggIdentifier())));
            }
        });
    }

}
