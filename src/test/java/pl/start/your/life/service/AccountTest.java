package pl.start.your.life.service;

import static org.mockito.MockitoAnnotations.initMocks;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import pl.start.your.life.command.AccountCreateCommand;
import pl.start.your.life.command.MoneyTransferCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.event.AccountCreatedEvent;
import pl.start.your.life.event.IncreasedBalanceAccountEvent;
import pl.start.your.life.handler.AccountHandler;

public class AccountTest {
    private FixtureConfiguration<Account> fixture;

    @Before
    public void setup() {
        initMocks(this);
        fixture = new AggregateTestFixture<>(Account.class);
        AccountHandler orderHandler = new AccountHandler();
        orderHandler.setRepository(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(orderHandler);
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
