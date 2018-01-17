package pl.start.your.life.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.command.PaymentCommand;
import pl.start.your.life.domain.Account;
import pl.start.your.life.domain.Order;
import pl.start.your.life.event.DecreasedBalanceAccountEvent;
import pl.start.your.life.event.OrderCreatedEvent;
import pl.start.your.life.event.PaymentAcceptedEvent;
import pl.start.your.life.exception.LimitCashExceededException;
import pl.start.your.life.handler.OrderCommandHandler;
import pl.start.your.life.repository.AccountRepository;
import pl.start.your.life.repository.OrderRepository;

public class OrderTest {
    private FixtureConfiguration<Order> fixture;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private OrderRepository jpaOrderRepository;

    @Before
    public void setup() {
        initMocks(this);
        fixture = new AggregateTestFixture<>(Order.class);
        OrderCommandHandler orderHandler = new OrderCommandHandler();
        orderHandler.setOrderRepository(jpaOrderRepository);
        orderHandler.setAccountRepository(accountRepository);
        fixture.registerAnnotatedCommandHandler(orderHandler);
        when(jpaOrderRepository.save(any(Order.class))).thenReturn(new Order());
        when(accountRepository.findOne(anyInt())).thenReturn(new Account(1, 200));
    }

    @Test
    public void orderCreateTest() {
        fixture.givenNoPriorActivity()
                .when(new OrderCreateCommand(1, 200, 1))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new OrderCreatedEvent(1, 200, 1));
    }

    @Test
    public void paymentSuccessTest() {
        fixture.givenCommands(new OrderCreateCommand(1, 200, 1))
                .andGiven(new OrderCreatedEvent(1, 200, 1))
                .when(new PaymentCommand(1, 1, 100))
                .expectEvents(new PaymentAcceptedEvent(1, 1), new DecreasedBalanceAccountEvent(1, 100));
    }

    @Test
    public void paymentFailTest() {
        fixture.givenCommands(new OrderCreateCommand(1, 200, 1))
                .andGiven(new OrderCreatedEvent(1, 200, 1))
                .when(new PaymentCommand(1, 1, 201))
                .expectException(LimitCashExceededException.class);
    }

}
