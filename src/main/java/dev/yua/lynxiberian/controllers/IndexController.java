package dev.yua.lynxiberian.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class IndexController {
    @GetMapping(value = {"","/"})
    public Object index(){
        return new HashMap<String, Integer>();
    }
}