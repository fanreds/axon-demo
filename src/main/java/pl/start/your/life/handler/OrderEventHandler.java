package pl.start.your.life.handler;

import static java.util.Optional.ofNullable;
import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;
import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.start.your.life.command.PaymentCommand;
import pl.start.your.life.domain.Order;
import pl.start.your.life.event.DecreasedBalanceAccountEvent;
import pl.start.your.life.event.IncreasedBalanceAccountEvent;
import pl.start.your.life.event.OrderApprovedEvent;
import pl.start.your.life.event.OrderCanceledEvent;
import pl.start.your.life.event.OrderCreatedEvent;
import pl.start.your.life.event.PaymentAcceptedEvent;
import pl.start.your.life.exception.EntityNotExist;
import pl.start.your.life.repository.OrderJpaRepository;

@Component
@Transactional
public class OrderEventHandler {

    @Autowired
    private EventBus eventBus;
    @Autowired
    private CommandBus commandBus;
    @Autowired
    private OrderJpaRepository orderRepository;

    @EventHandler
    public void on(OrderCanceledEvent event) {
        System.out.println("@EventSourcingHandler OrderCanceledEvent");
        Order order = ofNullable(orderRepository.findOne(event.getOrderId())).orElseThrow(EntityNotExist::new);
        order.setId(event.getOrderId());
        order.setAccountId(event.getAccountId());
        order.setApproved(false);
        order.setCanceled(false);
        order.setPrice(0);
    }

    @EventHandler
    @org.springframework.core.annotation.Order(53)
    public void on(IncreasedBalanceAccountEvent event) {
        System.out.println("@EventHandler OrderEventHandler logged");
    }

    @EventHandler
    public void on(PaymentAcceptedEvent event) {
        System.out.println("@EventSourcingHandler PaymentAcceptedEvent");
        Order order = ofNullable(orderRepository.findOne(event.getOrderId())).orElseThrow(EntityNotExist::new);
        order.setId(event.getOrderId());
        order.setAccountId(event.getAccountId());
        order.setPayed(true);
    }

    @EventHandler
    public void on(OrderApprovedEvent event) {
        System.out.println("@EventSourcingHandler OrderApprovedEvent");
        Order order = ofNullable(orderRepository.findOne(event.getOrderId())).orElseThrow(EntityNotExist::new);
        order.setId(event.getOrderId());
        order.setApproved(true);
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        System.out.println("@EventSourcingHandler OrderCreatedEvent");
        Order order = new Order();
        order.setPrice(event.getPrice());
        order.setAccountId(event.getAccountId());
        order.setApproved(false);
        order.setCanceled(false);
        order.setPayed(false);
        Order savedOrder = orderRepository.saveAndFlush(order);
        commandBus.dispatch(asCommandMessage(new PaymentCommand(savedOrder.getId(), event.getAccountId(), event.getPrice())), new CommandCallback<PaymentCommand, Object>() {
            @Override
            public void onSuccess(CommandMessage<? extends PaymentCommand> commandMessage, Object result) {
                System.out.println("Payment command successful");
                PaymentCommand payload = commandMessage.getPayload();
                eventBus.publish(asEventMessage(new PaymentAcceptedEvent(payload.getOrderId(), payload.getAccountId())));
                eventBus.publish(asEventMessage(new DecreasedBalanceAccountEvent(payload.getAccountId(), payload.getPrice())));
                eventBus.publish(asEventMessage(new OrderApprovedEvent(payload.getOrderId(), payload.getAccountId())));
            }

            @Override
            public void onFailure(CommandMessage<? extends PaymentCommand> commandMessage, Throwable cause) {
                PaymentCommand payload = commandMessage.getPayload();
                eventBus.publish(asEventMessage(new OrderCanceledEvent(payload.getOrderId(), payload.getAccountId())));
            }
        });
    }

}
