package pl.start.your.life.handler;

import static java.util.Optional.ofNullable;
import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import java.util.Optional;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.command.PaymentCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.domain.Order;
import pl.start.your.life.event.OrderCreatedEvent;
import pl.start.your.life.exception.AccountNotExist;
import pl.start.your.life.exception.LimitCashExceededException;
import pl.start.your.life.repository.AccountJpaRepository;
import pl.start.your.life.repository.OrderJpaRepository;

@Component
@NoArgsConstructor
public class OrderHandler {

    private OrderJpaRepository jpaOrderRepository;
    private Repository<Order> orderRepository;
    private AccountJpaRepository accountJpaRepository;
    @Autowired
    private CommandBus commandBus;

    @CommandHandler
    public void handle(OrderCreateCommand command) throws Exception {
        System.out.println("on command create order");
        Aggregate<Order> order = orderRepository.newInstance(() -> new Order(command.getOrderId(), command.getPrice(), command.getAccountId()));
        jpaOrderRepository.save(new Order(command.getOrderId(), command.getPrice(), command.getAccountId()));
        order.execute(e -> e.applyOrderCreatedEvent(command));
    }

    @CommandHandler
    public void handle(PaymentCommand command) throws Exception {
        Optional<Account> accountOptional = ofNullable(accountJpaRepository.findOne(command.getAccountId()));
        Account account = accountOptional.orElseThrow(AccountNotExist::new);
        Aggregate<Order> aggregate = orderRepository.load(command.getOrderId().toString());
        if (command.getPrice() > account.getBalance()) {
            throw new LimitCashExceededException();
        }
        aggregate.execute(e -> e.applyPaymentAcceptedEvent(command));
        account.applyDecreasedBalanceEvent(command.getAccountId(), command.getPrice());
    }

    @Autowired
    @Qualifier(value = "orderRepository")
    public void setOrderRepository(Repository<Order> orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setJpaOrderRepository(OrderJpaRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Autowired
    public void setAccountJpaRepository(AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        commandBus.dispatch(asCommandMessage(new PaymentCommand(event.getOrderId(), event.getAccountId(), event.getPrice())), new CommandCallback<PaymentCommand, Object>() {
            @Override
            public void onSuccess(CommandMessage<? extends PaymentCommand> commandMessage, Object result) {
                System.out.println("Payment command successful");
                PaymentCommand payload = commandMessage.getPayload();
                Aggregate<Order> orderAggregate = orderRepository.load(payload.getOrderId().toString());
                orderAggregate.execute(e -> e.applyOrderApproveEvent(payload));
            }

            @Override
            public void onFailure(CommandMessage<? extends PaymentCommand> commandMessage, Throwable cause) {
                PaymentCommand payload = commandMessage.getPayload();
                Aggregate<Order> orderAggregate = orderRepository.load(payload.getOrderId().toString());
                orderAggregate.execute(e -> e.applyOrderCanceledEvent(payload));
            }
        });
    }
}
