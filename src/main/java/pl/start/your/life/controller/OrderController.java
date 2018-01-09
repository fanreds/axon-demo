package pl.start.your.life.controller;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import javax.annotation.PostConstruct;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.dto.OrderDto;
import pl.start.your.life.repository.AccountRepository;

@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CommandBus commandBus;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity newOrder(@RequestBody OrderDto orderDto) {
        commandBus.dispatch(asCommandMessage(new OrderCreateCommand(orderDto.getOrderId(), orderDto.getPrice(), orderDto.getAccountId())), LoggingCallback.INSTANCE);
        return ResponseEntity.ok().build();
    }

    @PostConstruct
    public void onInit() {
        accountRepository.save(new Account(1, 200));
    }
}
