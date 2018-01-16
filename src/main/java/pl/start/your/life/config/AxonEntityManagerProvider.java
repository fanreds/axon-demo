package pl.start.your.life.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class AxonEntityManagerProvider implements EntityManagerProvider {

    @Getter
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
