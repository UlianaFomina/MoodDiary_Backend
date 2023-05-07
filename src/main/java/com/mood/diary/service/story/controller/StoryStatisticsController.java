package com.mood.diary.service.story.controller;

import com.mood.diary.service.story.model.StatisticsGraphResponse;
import com.mood.diary.service.story.model.Story;
import com.mood.diary.service.story.service.statistics.StoryStatisticsService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/story/statistics")
@RequiredArgsConstructor
public class StoryStatisticsController {

    private final StoryStatisticsService storyStatisticsService;

    @GetMapping("{userId}/{lastDays}")
    public List<Story> getStoriesInTimeRange(@PathVariable String userId, @Min(0) @PathVariable int lastDays) {
        return storyStatisticsService.getForLastDays(userId, lastDays);
    }

    @GetMapping("{userId}/{lastDays}/graph")
    public StatisticsGraphResponse getSatisfactionRatesInTimeRange(@PathVariable String userId,
                                                                  @Min(0) @PathVariable int lastDays) {
        List<Double> satisfactionRates = storyStatisticsService
                .satisfactionRatesForLastDays(userId, lastDays);

        return new StatisticsGraphResponse(satisfactionRates);
    }
}
