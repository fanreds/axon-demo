package pl.start.your.life.handler;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.NoArgsConstructor;
import pl.start.your.life.command.PaymentCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.event.PaymentAcceptedEvent;
import pl.start.your.life.exception.LimitCashExceededException;
import pl.start.your.life.repository.AccountRepository;

//@Component
@NoArgsConstructor
public class PayHandler {
    private AccountRepository accountRepository;

    @Autowired
    public PayHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @CommandHandler
    public void handle(PaymentCommand command) {
        Account account = accountRepository.findOne(command.getAccountId());
        if (command.getPrice() > account.getSaldo()) {
            throw new LimitCashExceededException();
        }
        account.setSaldo(account.getSaldo() - command.getPrice());
        apply(new PaymentAcceptedEvent(command.getOrderId(), command.getAccountId()));
    }

}
