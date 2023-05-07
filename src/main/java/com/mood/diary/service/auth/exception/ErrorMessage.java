package com.mood.diary.service.auth.exception;

import java.util.Date;
import java.util.List;

public record ErrorMessage(
     int statusCode,
     Date timestamp,
     List<ErrorDetail> errors,
     String description
) {}
