package com.mood.diary.service.story.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@Schema(description = "Response with array of satisfaction rates")
public record StatisticsGraphResponse (
        @Schema(description = "Array of satisfaction rates", example = "[0.3, 0.4, 0.24]")
        List<Double> satisfactionRates
) {}
