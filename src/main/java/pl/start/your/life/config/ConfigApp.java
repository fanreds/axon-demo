package pl.start.your.life.config;

import org.axonframework.spring.config.EnableAxon;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@EntityScan("pl.start.your.life.domain")
@EnableJpaRepositories(basePackages = {"pl.start.your.life.repository"})
@EnableAxon
public class ConfigApp {
}
