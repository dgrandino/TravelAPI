package io.github.dgrandino.travelsjavaapi.controller;

import io.github.dgrandino.travelsjavaapi.model.Statistic;
import io.github.dgrandino.travelsjavaapi.model.Travel;
import io.github.dgrandino.travelsjavaapi.service.StatisticService;
import io.github.dgrandino.travelsjavaapi.service.TravelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api-travels/statistics")
public class StatisticController {

    private static final Logger logger = LogManager.getLogger(TravelController.class);

    @Autowired
    private TravelService travelService;

    @Autowired
    private StatisticService statisticsService;

    @GetMapping(produces = { "application/json" })
    public ResponseEntity<Statistic> getStatistics(){

        List<Travel> travels = travelService.find();
        Statistic statistics = statisticsService.create(travels);

        logger.info(statistics);

        return ResponseEntity.ok(statistics);
    }
}
