package com.mood.diary.service.exception.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

@Schema(description = "Common object that will be returned on any exception")
public record ErrorMessage(
        @Schema(description = "Status code of error", example = "404")
        int statusCode,

        @Schema(description = "Time when error was handled", example = "2023-05-07T15:51:56.769+00:00")
        Date timestamp,

        @Schema(description = "Array of errors", example = """
                [
                        {
                            "message": "username: size must be between 4 and 10"
                        },
                        {
                            "message": "password: size must be between 6 and 12"
                        }
                ]
                """)
        List<ErrorDetail> errors,

        @Schema(description = "Where error was handled")
        String description
) {
}
