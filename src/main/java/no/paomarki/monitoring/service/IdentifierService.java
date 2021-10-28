package no.paomarki.monitoring.service;

import io.micrometer.core.instrument.MeterRegistry;
import no.paomarki.monitoring.config.DinnerProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.actuate.metrics.AutoTimer;
import org.springframework.boot.actuate.metrics.web.reactive.client.DefaultWebClientExchangeTagsProvider;
import org.springframework.boot.actuate.metrics.web.reactive.client.MetricsWebClientFilterFunction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class IdentifierService {

    private final WebClient webClient;

    public IdentifierService(DinnerProperties dinnerProperties, MeterRegistry meterRegistry) {
        MetricsWebClientFilterFunction metricsFilter = new MetricsWebClientFilterFunction(
                meterRegistry, new DefaultWebClientExchangeTagsProvider(),
                "identifier.service",
                AutoTimer.ENABLED
        );
        webClient = WebClient.builder()
                .baseUrl(dinnerProperties.getIdentifierServiceUrl())
                .filter(metricsFilter)
                .build();
    }

    public String getIdentifier() {
        String uuid = webClient
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class).block();
        return uuid != null
                ? uuid
                : RandomStringUtils.randomAlphabetic(10);
    }
}
