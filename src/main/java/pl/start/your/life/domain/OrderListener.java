package pl.start.your.life.domain;


import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.start.your.life.command.PaymentCommand;
import pl.start.your.life.event.OrderCreatedEvent;

@Component
public class OrderListener {

    @Autowired
    private CommandBus commandBus;

    @EventHandler
    public void on(OrderCreatedEvent event) {
        System.out.println("on handler OrderCreatedEvent");
        commandBus.dispatch(asCommandMessage(new PaymentCommand(event.getOrderId(), event.getAccountId(), event.getPrice())));
    }
}
