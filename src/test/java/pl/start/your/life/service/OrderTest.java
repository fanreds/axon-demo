package pl.start.your.life.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import pl.start.your.life.command.OrderCreateCommand;
import pl.start.your.life.domain.Order;
import pl.start.your.life.event.OrderCreatedEvent;
import pl.start.your.life.handler.OrderHandler;
import pl.start.your.life.repository.OrderJpaRepository;

public class OrderTest {
    private FixtureConfiguration<Order> fixture;

    @Mock
    private OrderJpaRepository jpaOrderRepository;

    @Before
    public void setup() {
        initMocks(this);
        fixture = new AggregateTestFixture<>(Order.class);
        OrderHandler orderHandler = new OrderHandler();
        orderHandler.setRepository(fixture.getRepository());
        orderHandler.setJpaOrderRepository(jpaOrderRepository);
        fixture.registerAnnotatedCommandHandler(orderHandler);
        when(jpaOrderRepository.save(any(Order.class))).thenReturn(new Order());
    }

    @Test
    public void orderCreateTest() {
        fixture.givenNoPriorActivity()
                .when(new OrderCreateCommand(1, 200, 1))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new OrderCreatedEvent(1, 200, 1));
    }

}
