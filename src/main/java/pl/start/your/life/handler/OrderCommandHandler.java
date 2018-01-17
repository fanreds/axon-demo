package pl.start.your.life.handler;

import static java.util.Optional.ofNullable;
import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import java.util.Optional;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.command.PaymentCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.domain.Order;
import pl.start.your.life.event.OrderCreatedEvent;
import pl.start.your.life.exception.EntityNotExist;
import pl.start.your.life.exception.LimitCashExceededException;
import pl.start.your.life.repository.AccountRepository;
import pl.start.your.life.repository.OrderRepository;

@Component
@NoArgsConstructor
public class OrderCommandHandler {

    private OrderRepository orderRepository;
    private AccountRepository accountRepository;
    @Autowired
    private CommandBus commandBus;

    @CommandHandler
    public void handle(OrderCreateCommand command) {
        System.out.println("on command create order");
        Order order = orderRepository.save(new Order(command.getOrderId(), command.getPrice(), command.getAccountId()));
        order.applyOrderCreatedEvent(command);
    }

    @CommandHandler
    public void handle(PaymentCommand command) {
        Optional<Account> accountOptional = ofNullable(accountRepository.findOne(command.getAccountId()));
        Account account = accountOptional.orElseThrow(EntityNotExist::new);
        Order order = orderRepository.findOne(command.getOrderId());
        if (command.getPrice() > account.getBalance()) {
            throw new LimitCashExceededException();
        }
        order.applyPaymentAcceptedEvent(command);
        account.applyDecreasedBalanceEvent(command.getAccountId(), command.getPrice());
    }


    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        commandBus.dispatch(asCommandMessage(new PaymentCommand(event.getOrderId(), event.getAccountId(), event.getPrice())), new CommandCallback<PaymentCommand, Object>() {
            @Override
            public void onSuccess(CommandMessage<? extends PaymentCommand> commandMessage, Object result) {
                System.out.println("Payment command successful");
                PaymentCommand payload = commandMessage.getPayload();
                Order order = orderRepository.findOne(payload.getOrderId());
                order.applyOrderApproveEvent(payload);
            }

            @Override
            public void onFailure(CommandMessage<? extends PaymentCommand> commandMessage, Throwable cause) {
                PaymentCommand payload = commandMessage.getPayload();
                Order order = orderRepository.findOne(payload.getOrderId());
                order.applyOrderCanceledEvent(payload);
            }
        });
    }
}
