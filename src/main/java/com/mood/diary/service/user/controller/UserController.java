package com.mood.diary.service.user.controller;

import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthUserService authUserService;

    @GetMapping("{id}")
    public AuthUser getById(@PathVariable String id) {
        return authUserService.findById(id);
    }
}
