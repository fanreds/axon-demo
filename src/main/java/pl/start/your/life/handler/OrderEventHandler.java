package pl.start.your.life.handler;

import static java.util.Optional.ofNullable;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.start.your.life.domain.Order;
import pl.start.your.life.event.OrderApprovedEvent;
import pl.start.your.life.event.OrderCanceledEvent;
import pl.start.your.life.event.OrderCreatedEvent;
import pl.start.your.life.event.PaymentAcceptedEvent;
import pl.start.your.life.exception.EntityNotExist;
import pl.start.your.life.repository.OrderRepository;

@Component
public class OrderEventHandler {

    @Autowired
    private OrderRepository orderRepository;

    @EventHandler
    public void on(OrderCreatedEvent event) {
        System.out.println("@EventSourcingHandler OrderCreatedEvent");
        Order order = ofNullable(orderRepository.findOne(event.getOrderId())).orElseThrow(EntityNotExist::new);
        order.setId(event.getOrderId());
        order.setPrice(event.getPrice());
        order.setAccountId(event.getAccountId());
        order.setApproved(false);
        order.setCanceled(false);
        order.setPayed(false);
    }

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

}
