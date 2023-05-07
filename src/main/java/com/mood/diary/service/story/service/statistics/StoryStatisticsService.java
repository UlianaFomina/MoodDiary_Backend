package com.mood.diary.service.story.service.statistics;

import com.mood.diary.service.story.model.Story;

import java.util.List;

public interface StoryStatisticsService {
    List<Story> getForLastDays(String userId, int days);
    List<Double> satisfactionRatesForLastDays(String userId, int days);
}
