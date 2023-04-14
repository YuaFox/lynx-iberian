package dev.yua.lynxiberian.controllers;

import dev.yua.lynxiberian.models.Bucket;
import dev.yua.lynxiberian.models.ExplorerRequest;
import dev.yua.lynxiberian.repositories.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.models.Media;

@RestController
@RequestMapping("/api/v1/drivers/explorer")
public class ExplorerController {

    @Autowired
    private BucketRepository bucketRepository;
    
    @GetMapping(value = "/{driver}", produces = "application/json")
    public ResponseEntity<Media> index(
            @PathVariable(required=false,name="driver") String driver,
            @RequestParam(value = "bucket", required = false) String bucketName
        ){
        ExplorerRequest explorerRequest = new ExplorerRequest();
        if(bucketName != null && !bucketName.isEmpty()){
            Bucket bucket = this.bucketRepository.getBucketByName(bucketName);
            if(bucket == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            explorerRequest.setBucket(bucket);
        }
        return new ResponseEntity<>(LynxiberianApplication.getDriverManager().getExplorerDriver(driver).getMedia(explorerRequest), HttpStatus.OK);
    }
}