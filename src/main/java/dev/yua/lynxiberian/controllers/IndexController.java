package dev.yua.lynxiberian.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

@Controller
public class IndexController {
    @GetMapping(value = {"","/"})
    public Object index(){
        return "index.html";
    }
}