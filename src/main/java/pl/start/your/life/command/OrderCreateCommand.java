package pl.start.your.life.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderCreateCommand {
    private Integer orderId;
    private Integer price;
    private Integer accountId;
}
