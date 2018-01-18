package pl.start.your.life.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDto {
    private Integer accountId;
    private Integer price;
}
