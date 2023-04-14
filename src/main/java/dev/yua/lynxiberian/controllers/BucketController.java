package dev.yua.lynxiberian.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.yua.lynxiberian.models.Bucket;
import dev.yua.lynxiberian.repositories.BucketRepository;

@RestController
@RequestMapping("/api/v1/bucket")
public class BucketController {

    @Autowired
    private BucketRepository repository;
  

    @GetMapping({"","/"})
    public List<Bucket> index() {
        Iterator<Bucket> source = this.repository.findAll().iterator();
        List<Bucket> target = new ArrayList<>();
        source.forEachRemaining(target::add);
        return target;
    }

    @PostMapping(path = {"","/"})
    public ResponseEntity<String> create(@RequestParam("name") String name){
        Bucket bucket = new Bucket();
        if(repository.getBucketByName(name) != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bucket already exists");
        bucket.setName(name);
        repository.save(bucket);
        return ResponseEntity.status(HttpStatus.OK).body("Ok");
    }

    @DeleteMapping(path = "/{bucket}")
    public ResponseEntity<String> delete(@PathVariable(name="name") String name){
        Bucket bucket = repository.getBucketByName(name);
        if(bucket != null){
            repository.delete(bucket);
            return ResponseEntity.status(HttpStatus.OK).body("Ok");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bucket does not exists");
        }
    }
}