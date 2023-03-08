package dev.yua.lynxiberian.controllers;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.models.entity.Filter;
import dev.yua.lynxiberian.models.entity.Media;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers/gatherer")
public class GathererController {
    @PostMapping(value = "/{driver}", produces = "application/json")
    public Object index(
            @PathVariable(required=false,name="driver") String driver,
            @RequestBody List<Filter> filters
        ){
        LynxiberianApplication.getDriverManager().getGathererDriver(driver).gather(filters);
        return true;
    }
}