package pl.start.your.life.handler;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.domain.Order;
import pl.start.your.life.repository.OrderRepository;

@Component
public class OrderHandler {

    private OrderRepository jpaOrderRepository;
    private Repository<Order> repository;

    @Autowired
    public OrderHandler(OrderRepository jpaOrderRepository, Repository<Order> repository) {
        this.jpaOrderRepository = jpaOrderRepository;
        this.repository = repository;
    }

    @CommandHandler
    public void handle(OrderCreateCommand command) throws Exception {
        Aggregate<Order> order = repository.newInstance(() -> new Order(command.getOrderId(), command.getPrice(), command.getAccountId()));
        jpaOrderRepository.save(new Order(command.getOrderId(), command.getPrice(), command.getAccountId()));
        order.execute(e -> e.orderCreated(command));
    }

}
