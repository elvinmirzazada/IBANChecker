package com.hometask.ibanchecker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    /*
    The main page for checking single or multiple IBANs at same time.
     */
    public String home(){
        return "main";
    }

    @GetMapping("/history")
    public String history(){
        /*
        The historyy page is responsible for showing the history of IBAN searches.
         */
        return "history";
    }
}
