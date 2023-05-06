package com.mood.diary.service.auth.service;

import com.mood.diary.service.auth.model.request.AuthenticationRequest;
import com.mood.diary.service.auth.model.request.RegisterRequest;
import com.mood.diary.service.auth.model.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest registerRequest);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    void resetPassword(String token, String email);
}
