package dev.yua.lynxiberian.controllers;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.events.Event;
import dev.yua.lynxiberian.models.Bucket;
import dev.yua.lynxiberian.repositories.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

    @PostMapping(path = {"","/"})
    public ResponseEntity<Object> send(@RequestBody Event event){
        Object response = LynxiberianApplication.getEventManager().sendEvent(event, Object.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
