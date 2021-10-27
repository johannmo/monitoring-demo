package no.paomarki.monitoring.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import no.paomarki.monitoring.model.Dinner;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DinnerService {

    private final List<Dinner> dinners;
    private final MeterRegistry meterRegistry;
    private List<String> waitingGuests = new ArrayList<>(0);
    private Gauge queueSize;

    public DinnerService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
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
        queueSize = Gauge.builder("dinner.waiting.guests", waitingGuests, List::size)
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
        return dinners.get(random.nextInt(dinners.size()));
    }

    public void addWaitingGuest() {
        String name = RandomStringUtils.randomAlphabetic(10);
        waitingGuests.add(name);
        log.info("Added {} to dinner queue", name);
    }

    public boolean removeWaitingGuest() {
        if (waitingGuests.size() > 0) {
            String name = waitingGuests.get(0);
            waitingGuests.remove(0);
            log.info("Removed {} from dinner queue", name);
            return true;
        }
        return false;
    }
}
