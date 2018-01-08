package pl.start.your.life.handler;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.domain.Order;
import pl.start.your.life.repository.OrderJpaRepository;

@Component
@NoArgsConstructor
public class OrderHandler {

    private OrderJpaRepository jpaOrderRepository;
    private Repository<Order> repository;

    @CommandHandler
    public void handle(OrderCreateCommand command) throws Exception {
        System.out.println("on command create order");
        Aggregate<Order> order = repository.newInstance(() -> new Order(command.getOrderId(), command.getPrice(), command.getAccountId()));
        jpaOrderRepository.save(new Order(command.getOrderId(), command.getPrice(), command.getAccountId()));
        order.execute(e -> e.orderCreated(command));
    }

    @Autowired
    @Qualifier(value = "orderRepository")
    public void setRepository(Repository<Order> orderRepository) {
        this.repository = orderRepository;
    }

    @Autowired
    public void setJpaOrderRepository(OrderJpaRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }
}
