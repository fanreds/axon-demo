package pl.start.your.life.aggregate;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.start.your.life.event.PaymentAcceptedEvent;

@Data
@Aggregate(repository = "payRepository")
@NoArgsConstructor
public class Pay {
    @AggregateIdentifier
    private Integer id;

    @EventSourcingHandler
    public void on(PaymentAcceptedEvent event) {
        System.out.println("on handler PaymentAcceptedEvent");
        this.id = event.getOrderId();
    }
}
