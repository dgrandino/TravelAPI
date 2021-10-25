package io.github.dgrandino.travelsjavaapi.factory;

import io.github.dgrandino.travelsjavaapi.model.Travel;

public interface TravelFactory {
    Travel createTravel (String type);
}
