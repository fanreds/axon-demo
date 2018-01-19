package pl.start.your.life.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCreatedEvent {
    private String aggIdentifier;
    private Integer price;
    private Integer accountId;
}
