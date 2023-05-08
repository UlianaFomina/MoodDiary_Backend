package com.mood.diary.service.demo;

import com.mood.diary.service.auth.model.response.AuthenticationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    public AuthenticationResponse demo() {
        return new AuthenticationResponse("token1234");
    }
}
