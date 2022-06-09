package com.example.userservice.contrller;

import com.example.userservice.vo.Greeting;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class UserController {

    private Environment env;
    private Greeting greeting;


    @GetMapping("/health_check")
    public String status(){
        return "It's Working in User Service";
    }

    @GetMapping("/welcome")
    public String greeting(){
//        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

}
