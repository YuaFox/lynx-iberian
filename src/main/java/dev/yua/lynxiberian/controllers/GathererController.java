package dev.yua.lynxiberian.controllers;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.models.Bucket;
import dev.yua.lynxiberian.models.GatherRequest;
import dev.yua.lynxiberian.models.GatherResult;
import dev.yua.lynxiberian.models.Filter;
import dev.yua.lynxiberian.repositories.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers/gatherer")
public class GathererController {

    @Autowired
    private BucketRepository bucketRepository;

    @PostMapping(value = "/{driver}", produces = "application/json")
    public GatherResult index(
            @PathVariable(required=false,name="driver") String driver,
            @RequestBody GatherRequest gatherRequest
            ){
        Bucket provisionalBucket = gatherRequest.getBucket();
        if(provisionalBucket != null){
            if(provisionalBucket.getName() != null){
                Bucket bucket = this.bucketRepository.getBucketByName(provisionalBucket.getName());
                if(bucket == null) return null;
                gatherRequest.setBucket(bucket);
            }
        }
        return LynxiberianApplication.getDriverManager().getGathererDriver(driver).gather(gatherRequest);
    }
}