package dev.yua.lynxiberian.controllers;

import org.springframework.web.bind.annotation.*;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.models.entity.Media;

@RestController
@RequestMapping("/api/v1/drivers/explorer")
public class ExplorerController {
    
    @GetMapping(value = "/{driver}", produces = "application/json")
    public Object index(
            @PathVariable(required=false,name="driver") String driver
        ){
        Media media = LynxiberianApplication.getDriverManager().getExplorerDriver(driver).getMedia();
        return media;
    }
}