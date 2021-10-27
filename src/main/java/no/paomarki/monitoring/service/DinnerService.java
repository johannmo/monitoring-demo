package no.paomarki.monitoring.service;

import no.paomarki.monitoring.model.Dinner;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DinnerService {

    private final List<Dinner> dinners;

    public DinnerService() {
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
}
