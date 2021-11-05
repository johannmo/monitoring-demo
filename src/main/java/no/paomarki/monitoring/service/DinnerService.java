package no.paomarki.monitoring.service;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import no.paomarki.monitoring.model.Dinner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class DinnerService {

    private final List<Dinner> dinners;
    private final MeterRegistry meterRegistry;
    private final List<String> waitingGuests = new ArrayList<>(0);
    private final Counter dinnersSinceLastRestart;

    private final IdentifierService identifierService;

    public DinnerService(MeterRegistry meterRegistry, IdentifierService identifierService) {
        this.meterRegistry = meterRegistry;
        this.identifierService = identifierService;
        this.dinnersSinceLastRestart = Counter.builder("dinner.meals.served")
                .description("Dinners served since last restart")
                .register(this.meterRegistry);
        dinners = Collections.unmodifiableList(new ArrayList<>() {{
            add(new Dinner("pizza", List.of("pizza base", "tomatoes", "topping", "cheese")));
            add(new Dinner("okonomiyaki", List.of("pancake dough", "cabbage", "bacon", "mayonnaise", "sauce")));
            add(new Dinner("fish soup", List.of("fish", "soup", "vegetables", "spices")));
            add(new Dinner("smalahove", List.of("smoked sheep head", "potatoes", "turnips")));
            add(new Dinner("hamburger", List.of("bun", "burger", "salad", "sauce", "topping")));
            add(new Dinner("chili non carne", List.of("beans", "tomatoes", "chili", "chocolate", "peppers", "onion")));
            add(new Dinner("mushroom wraps", List.of("champignon", "tortilla", "salsa", "peppers", "onion", "avocado")));
            add(new Dinner("sardine casserole", List.of("sardines", "potatoes", "tomatoes", "onions", "spinach", "olives", "spices")));
            add(new Dinner("fiskekling", List.of("fresh fish", "salt", "pepper", "flatbread", "sour cream")));
            add(new Dinner("pasta with chicken", List.of("pasta", "chicken thighs", "broccoli", "cheese", "Oslo sauce")));
        }});
        Gauge queueSize = Gauge.builder("dinner.waiting.guests", waitingGuests, List::size)
                .register(meterRegistry);
    }

    public List<Dinner> getDinners() {
        return dinners;
    }

    public Optional<Dinner> getDinner(String name) {
        return dinners.stream()
                .filter(dinner -> dinner.getName().equals(name))
                .findFirst();
    }

    public Dinner getRandomDinner() {
        Random random = new Random();
        dinnersSinceLastRestart.increment();
        return dinners.get(random.nextInt(dinners.size()));
    }

    public void addWaitingGuest() {
        String identifier = identifierService.getIdentifier();
        waitingGuests.add(identifier);
        log.info("Added {} to dinner queue", identifier);
    }

    public boolean removeWaitingGuest() {
        if (waitingGuests.size() > 0) {
            String identifier = waitingGuests.get(0);
            waitingGuests.remove(0);
            log.info("Removed {} from dinner queue", identifier);
            return true;
        }
        return false;
    }

    @Timed(value = "dinner.report.generate", longTask = true)
    @Scheduled(cron = "${dinner.print-report-cron-expression:-}")
    public void PrintScheduledReport() throws InterruptedException {
        Thread.sleep(30000);
        System.out.println("---- Generating Report - " + LocalDateTime.now() + "----");
        Thread.sleep(30000);
        System.out.println("Dinners in menu: " + dinners.size());
        Thread.sleep(30000);
        System.out.println("Guests in queue: " + waitingGuests.size());
        System.out.println("---- Report Generated - " + LocalDateTime.now() + "----");
    }
}
