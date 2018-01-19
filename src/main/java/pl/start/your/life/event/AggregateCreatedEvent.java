package pl.start.your.life.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.start.your.life.aggregate.AggregateSetter;

@Data
@NoArgsConstructor
public class AggregateCreatedEvent {
    private Integer id;
    private AggregateSetter<Integer> aggregate;

    public AggregateCreatedEvent(Integer id, Object aggregate) {
        this.id = id;
        //noinspection unchecked
        this.aggregate = (AggregateSetter<Integer>) aggregate;
    }
}
