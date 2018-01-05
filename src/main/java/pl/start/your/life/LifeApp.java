package pl.start.your.life;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LifeApp {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(LifeApp.class);
        springApplication.run(args);
    }
}
