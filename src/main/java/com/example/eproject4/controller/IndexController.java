package com.example.eproject4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping
public class IndexController {
    @GetMapping("/")
    public String Index() {
        return "index/index";
    }

    @GetMapping("/about")
    public String About() {
        return "index/about";
    }
    
}
