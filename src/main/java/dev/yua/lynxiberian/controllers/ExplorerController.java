package dev.yua.lynxiberian.controllers;

import dev.yua.lynxiberian.models.Bucket;
import dev.yua.lynxiberian.models.ExplorerRequest;
import dev.yua.lynxiberian.models.GatherRequest;
import dev.yua.lynxiberian.repositories.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.models.Media;

@RestController
@RequestMapping("/api/v1/drivers/explorer")
public class ExplorerController {

    @Autowired
    private BucketRepository bucketRepository;
    
    @GetMapping(value = "/{driver}", produces = "application/json")
    public Object index(
            @PathVariable(required=false,name="driver") String driver,
            @RequestBody(required = false) ExplorerRequest explorerRequest
        ){
        if(explorerRequest == null) explorerRequest = new ExplorerRequest();
        Bucket provisionalBucket = explorerRequest.getBucket();
        if(provisionalBucket != null){
            if(provisionalBucket.getName() != null){
                Bucket bucket = this.bucketRepository.getBucketByName(provisionalBucket.getName());
                if(bucket == null) return null;
                explorerRequest.setBucket(bucket);
            }
        }
        return LynxiberianApplication.getDriverManager().getExplorerDriver(driver).getMedia(explorerRequest);
    }
}