package pl.start.your.life.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.spring.config.EnableAxon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableAxon
@Configuration
public class AxonConfig extends JpaConfig {

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public CommandBus commandBus() {
        return new SimpleCommandBus();
    }

}
