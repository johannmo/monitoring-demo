package no.paomarki.monitoring.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.paomarki.monitoring.config.DinnerProperties;
import no.paomarki.monitoring.model.Dinner;
import no.paomarki.monitoring.service.DinnerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/dinners")
public class DinnerController {

    private final DinnerProperties dinnerProperties;
    private final DinnerService dinnerService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dinner> getDinners() {
        log.info("Getting all dinners");
        return dinnerService.getDinners();
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<Dinner> getDinner(@PathVariable String name) {
        log.info("Looks for dinner with name '{}'", name);
        Optional<Dinner> match = dinnerService.getDinner(name);
        return match.isPresent()
                ? ResponseEntity.of(match)
                : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/random")
    public ResponseEntity<Dinner> getRandomDinner() {
        log.info("Getting random dinner");
        Optional<Dinner> dinner = Optional.of(dinnerService.getRandomDinner());
        return ResponseEntity.of(dinner);
    }

    @GetMapping(value = "/slow")
    public ResponseEntity<Dinner> getSlowDinner() {
        log.warn("Getting slow dinner");
        try {
            Thread.sleep(new Random().nextInt(3000));
        } catch (InterruptedException e) {
            log.error("Something happened while getting a slow dinner", e);
        }

        return getRandomDinner();
    }

    @GetMapping(value = "/bad")
    public ResponseEntity<Dinner> getBadDinner() {
        log.error("Getting a bad dinner");
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping(value = "/unreliable")
    public ResponseEntity<Dinner> getDinnerIfYourLucky() {
        log.info("Try my luck at getting a dinner...");
        Random random = new Random();
        int number = random.nextInt(100);
        if (number < dinnerProperties.getUnreliableDinnerSuccessRate()) {
            return random.nextInt(3) > 2
                    ? getSlowDinner()
                    : getRandomDinner();
        }
        if (random.nextInt(10) > 8) {
            log.info("...sorry, Mac - no dinner today");
            return ResponseEntity.notFound().build();
        }
        return getBadDinner();
    }

    @GetMapping(value = "/queue")
    public ResponseEntity<?> addGuestToQueue() {
        dinnerService.addWaitingGuest();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/dequeue")
    public ResponseEntity<?> removeGuestFromQueue() {
        return dinnerService.removeWaitingGuest()
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();

    }

}
