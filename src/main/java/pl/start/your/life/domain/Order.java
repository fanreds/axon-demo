package pl.start.your.life.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "ORDERS")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    @NonNull
    private Integer id;

    @NonNull
    private Integer price;
    @NonNull
    private Integer accountId;
    private Boolean payed;
    private Boolean canceled;
    private Boolean approved;

}
