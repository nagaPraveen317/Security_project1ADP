package com.webage.Security_Authorization.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class ServiceAPI {

    @GetMapping

    public String doCheck(){
        return "Service is running up good";
    }
    
}
