package pl.start.your.life.controller;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.start.your.life.command.MoneyTransferCommand;
import pl.start.your.life.dto.MoneyTransferDto;

@RestController
@RequestMapping(value = "/api/moneyTransfer")
public class AccountController {

    @Autowired
    private CommandBus commandBus;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity newOrder(@RequestBody MoneyTransferDto moneyTransferDto) {
        commandBus.dispatch(asCommandMessage(new MoneyTransferCommand(moneyTransferDto.getAccountId(), moneyTransferDto.getAmount())), LoggingCallback.INSTANCE);
        return ResponseEntity.ok().build();
    }

}
