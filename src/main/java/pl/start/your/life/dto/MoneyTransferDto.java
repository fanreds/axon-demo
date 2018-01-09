package pl.start.your.life.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoneyTransferDto {
    private Integer accountId;
    private Integer amount;
}
