package pl.start.your.life.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentAcceptedEvent {
    private Integer orderId;
    private Integer accountId;
}
