package com.mood.diary.service.auth.exception;

import java.util.Date;

public record ErrorMessage(
     int statusCode,
     Date timestamp,
     String message,
     String description
) {}
