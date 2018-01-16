package pl.start.your.life;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableAutoConfiguration
@SpringBootApplication
public class LifeApp {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(LifeApp.class);
        springApplication.run(args);
    }
}
