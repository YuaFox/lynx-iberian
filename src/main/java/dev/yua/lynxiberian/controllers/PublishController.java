package dev.yua.lynxiberian.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.yua.lynxiberian.models.entity.Post;
import org.springframework.web.bind.annotation.*;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.models.entity.Media;

@RestController
@RequestMapping("/api/v1/drivers/publisher")
public class PublishController {
    @CrossOrigin
    @GetMapping(value = {"","/"}, produces = "application/json")
    public List<?> index(){
        return LynxiberianApplication.getDriverManager().getPublishDrivers();
    }

    @PostMapping(value = "/{driver}", produces = "application/json")
    public List<?> driver(
            @PathVariable(required=false,name="driver") String driver,
            @RequestBody Post post
        ){
        LynxiberianApplication.getDriverManager().getPublishDriver(driver).publish(post);
        return new ArrayList<>();
    }
}