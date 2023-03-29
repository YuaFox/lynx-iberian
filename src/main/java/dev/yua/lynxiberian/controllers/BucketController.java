package dev.yua.lynxiberian.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping({"","/"})
    public Object create(@RequestParam("name") String name){
        Bucket bucket = new Bucket();
        bucket.setName(name);
        repository.save(bucket);
        return null;
    }
}