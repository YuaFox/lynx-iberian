package dev.yua.lynxiberian.controllers;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.models.entity.Media;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/drivers/gatherer")
public class GathererController {
    @GetMapping(value = "/{driver}", produces = "application/json")
    public Object index(
            @PathVariable(required=false,name="driver") String driver
        ){
        LynxiberianApplication.getDriverManager().getGathererDriver(driver).gather();
        return true;
    }
}