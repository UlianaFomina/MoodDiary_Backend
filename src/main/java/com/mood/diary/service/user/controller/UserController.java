package com.mood.diary.service.user.controller;

import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthUserService authUserService;

    @GetMapping("{id}")
    public AuthUser getById(@PathVariable String id) {
        return authUserService.findById(id);
    }

    @PostMapping("/avatar")
    public void attachAvatar(@RequestParam("id") String id,
                             @RequestParam("avatar") MultipartFile avatar) {
        authUserService.attachAvatar(id, avatar);
    }
}
