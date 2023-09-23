package dev.yua.lynxiberian.controllers;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/drivers/gatherer")
public class GathererController {
    @PostMapping(value = "/{driver}", produces = "application/json")
    public ResponseEntity<GatherResult> index(
            @PathVariable(required=false,name="driver") String driver,
            @RequestBody GatherRequest gatherRequest
            ){
        Bucket bucket = gatherRequest.getBucket();
        if(bucket.exists()){
            return new ResponseEntity<>(
                    LynxiberianApplication.getDriverManager().getGathererDriver(driver).gather(gatherRequest), HttpStatus.OK
            );
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}