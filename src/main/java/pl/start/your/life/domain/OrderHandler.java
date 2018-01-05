package pl.start.your.life.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.event.OrderCreatedEvent;
import pl.start.your.life.repository.OrderRepository;

@Component
@NoArgsConstructor
public class OrderHandler {

    private OrderRepository orderRepository;

    @Autowired
    public OrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @CommandHandler
    public void handle(OrderCreateCommand command) {
//        orderRepository.save(new Order(command.getOrderId(), command.getPrice(), command.getAccountId()));
        apply(new OrderCreatedEvent(command.getOrderId(), command.getPrice(), command.getAccountId()));
    }

}
