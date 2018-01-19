package pl.start.your.life.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentCommand {
    private String aggId;
    private Integer accountId;
    private Integer price;
}
