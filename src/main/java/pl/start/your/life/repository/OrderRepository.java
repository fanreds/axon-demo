package pl.start.your.life.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.start.your.life.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
