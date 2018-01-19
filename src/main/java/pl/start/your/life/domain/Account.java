package pl.start.your.life.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account implements JPAEntity<Integer> {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer balance;

}
