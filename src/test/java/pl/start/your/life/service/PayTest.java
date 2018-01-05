package pl.start.your.life.service;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.start.your.life.aggregate.Pay;
import pl.start.your.life.command.PaymentCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.event.OrderCanceledEvent;
import pl.start.your.life.event.PaymentAcceptedEvent;
import pl.start.your.life.exception.LimitCashExceededException;
import pl.start.your.life.handler.PayHandler;
import pl.start.your.life.repository.AccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class PayTest {
    private FixtureConfiguration<Pay> fixture;

    @Mock
    private AccountRepository accountRepository;

    @Before
    public void setup() {
        initMocks(this);
        fixture = new AggregateTestFixture<>(Pay.class);
        fixture.registerAnnotatedCommandHandler(new PayHandler(accountRepository));
        when(accountRepository.findOne(anyInt())).thenReturn(new Account(1, 200));
    }


    @Test
    public void paymentDefaultTest() {
        fixture.givenNoPriorActivity()
                .when(new PaymentCommand(1, 1, 100))
                .expectEvents(new PaymentAcceptedEvent(1, 1));
    }

    @Test
    public void decreaseBalanceAccount() {
        fixture.givenNoPriorActivity()
                .when(new PaymentCommand(1, 1, 100))
                .expectException(LimitCashExceededException.class)
                .expectEvents(new OrderCanceledEvent(1));
    }

}
