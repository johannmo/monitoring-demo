package no.paomarki.monitoring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(value = "dinner")
public class DinnerProperties {

    private int unreliableDinnerSuccessRate;

}
