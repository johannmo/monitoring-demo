package no.paomarki.monitoring;

import no.paomarki.monitoring.config.DinnerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(DinnerProperties.class)
@SpringBootApplication
public class MonitoringDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringDemoApplication.class, args);
    }

}
