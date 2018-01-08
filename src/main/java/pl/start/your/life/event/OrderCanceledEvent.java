package pl.start.your.life.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCanceledEvent {
    private Integer orderId;
    private Integer accountId;
}
