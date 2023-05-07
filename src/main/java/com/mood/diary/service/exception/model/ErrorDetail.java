package com.mood.diary.service.exception.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Common object that will be returned on any error added in exception")
public record ErrorDetail (
        @Schema(description = "Text message of error", example = "Not valid email!")
        String message
) {}
