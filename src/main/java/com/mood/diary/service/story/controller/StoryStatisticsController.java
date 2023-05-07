package com.mood.diary.service.story.controller;

import com.mood.diary.service.story.model.StatisticsGraphResponse;
import com.mood.diary.service.story.model.Story;
import com.mood.diary.service.story.service.statistics.StoryStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/story/statistics")
@RequiredArgsConstructor
@Tag(
        name = "Story Statistics Controller",
        description = "Endpoints to retrieve statistics about stories"
)
public class StoryStatisticsController {

    private final StoryStatisticsService storyStatisticsService;

    @Operation(
            summary = "Get user stories for set time range",
            description = """
                    Give you in response stories for last configured days.
                    """
    )
    @GetMapping("{userId}/{lastDays}")
    public List<Story> getStoriesInTimeRange(
            @Parameter(description = "User identifier", example = "userId", required = true) @Valid @NotNull @PathVariable String userId,
            @Parameter(description = "Value to configure time range in days",example = "3", required = true) @Valid @Min(0) @PathVariable int lastDays) {
        return storyStatisticsService.getForLastDays(userId, lastDays);
    }

    @Operation(
            summary = "Get user stories for set time range",
            description = """
                    Here we retrieve stories in set time range, but
                    Here we map data to array of Doubles - satisfactionRates
                    """
    )
    @GetMapping("{userId}/{lastDays}/graph")
    public StatisticsGraphResponse getSatisfactionRatesInTimeRange(
            @Parameter(description = "User identifier", example = "userId", required = true) @Valid @NotNull @PathVariable String userId,
            @Parameter(description = "Value to configure time range", example = "3", required = true) @Valid @Min(0) @Max(90) @PathVariable int lastDays) {
        List<Double> satisfactionRates = storyStatisticsService
                .satisfactionRatesForLastDays(userId, lastDays);

        return new StatisticsGraphResponse(satisfactionRates);
    }
}
