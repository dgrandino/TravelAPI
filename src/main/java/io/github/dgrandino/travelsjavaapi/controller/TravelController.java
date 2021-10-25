package io.github.dgrandino.travelsjavaapi.controller;

import io.github.dgrandino.travelsjavaapi.model.Travel;
import io.github.dgrandino.travelsjavaapi.service.TravelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api-travels/travels")
public class TravelController {

    private static final Logger logger = LogManager.getLogger(TravelController.class);

    @Autowired
    private TravelService travelService;

    @GetMapping
    public ResponseEntity<List<Travel>> find(){
        if(travelService.find().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        logger.info(travelService.find());
        return ResponseEntity.ok(travelService.find());
    }

    @DeleteMapping
    public ResponseEntity<Boolean> delete(){
        try{
            travelService.delete();
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            logger.error(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Travel> create(@RequestBody String  travel){

        try{
            JSONObject trip = new JSONObject(travel);
            if(travelService.isJSONValid(trip.toString())){

                Travel travelCreated = travelService.create(trip);

                var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(travelCreated.getOrderNumber()).build().toUri();

                if(travelService.isStartDateGreaterThanEndDate((travelCreated))){
                    logger.error("The start date is greater than en date");
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
                }
                else{
                    travelService.add(travelCreated);
                    return ResponseEntity.created(uri).body(null);
                }
            }
            else{
                return ResponseEntity.badRequest().body(null);
            }
        }catch (Exception e){
            logger.error("JSON fields are not parsable." + e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @PutMapping(path = "/{id}",produces = { "application/json" })
    public ResponseEntity<Travel> update(@PathVariable("id") long id, @RequestBody String  travel){
        try{
            JSONObject trip = new JSONObject(travel);

            if(travelService.isJSONValid(trip.toString())){
                Travel travelToUpdate = travelService.findById(id);
                if(travelToUpdate == null){
                    logger.error("Travel not found");
                    return ResponseEntity.notFound().build();
                }
                else{
                    travelToUpdate = travelService.update(travelToUpdate, trip);
                    return ResponseEntity.ok(travelToUpdate);
                }
            }
            else{
                return ResponseEntity.badRequest().body((null));
            }

        }catch(Exception e){
            logger.error("JSON fields are not parsable."+ e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

}
