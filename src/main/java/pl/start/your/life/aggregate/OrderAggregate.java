package pl.start.your.life.aggregate;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Aggregate
@RequiredArgsConstructor
@Data
public class OrderAggregate implements AggregateSetter<Integer> {
    @AggregateIdentifier
    @NonNull
    private String id;

    private Integer orderId;
    @NonNull
    private Integer price;
    @NonNull
    private Integer accountId;
    private Boolean payed;
    private Boolean canceled;
    private Boolean approved;

    @Override
    public void setEntityId(Integer id) {
        setOrderId(id);
    }
}
