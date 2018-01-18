package pl.start.your.life.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import pl.start.your.life.command.AccountCreateCommand;
import pl.start.your.life.command.MoneyTransferCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.event.AccountCreatedEvent;
import pl.start.your.life.event.IncreasedBalanceAccountEvent;
import pl.start.your.life.handler.AccountCommandHandler;
import pl.start.your.life.repository.AccountRepository;

public class AccountTest {
    private FixtureConfiguration<Account> fixture;

    @Mock
    private AccountRepository accountRepository;

    @Before
    public void setup() {
        initMocks(this);
        fixture = new AggregateTestFixture<>(Account.class);
        AccountCommandHandler accountHandler = new AccountCommandHandler();
        fixture.registerAnnotatedCommandHandler(accountHandler);
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());
    }

    @Test
    public void moneyTransferCreateTest() {
        fixture.given(new AccountCreatedEvent(1, 0))
                .when(new MoneyTransferCommand(1, 200))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new IncreasedBalanceAccountEvent(1, 200));
    }

    @Test
    public void accountCreateTest() {
        fixture.givenNoPriorActivity()
                .when(new AccountCreateCommand(1, 0))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new AccountCreatedEvent(1, 0));
    }


}
