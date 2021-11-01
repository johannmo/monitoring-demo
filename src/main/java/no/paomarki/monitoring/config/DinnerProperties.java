package no.paomarki.monitoring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(value = "dinner")
public class DinnerProperties {

    private int unreliableDinnerSuccessRate;

    @NotNull
    private String identifierServiceUrl;

    private String printReportCronExpression;
}
