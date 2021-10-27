package no.paomarki.monitoring.model;

import lombok.Value;

import java.util.List;

@Value
public class Dinner {
    String name;
    List<String> ingredients;
}
