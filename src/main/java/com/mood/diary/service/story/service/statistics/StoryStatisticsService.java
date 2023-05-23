package com.mood.diary.service.story.service.statistics;

import com.mood.diary.service.story.model.StatisticsGraphResponse;
import com.mood.diary.service.story.model.StatisticsMoodResponse;
import com.mood.diary.service.story.model.Story;

import java.util.List;

public interface StoryStatisticsService {
    List<Story> getForLastDays(String userId, int days);
    StatisticsMoodResponse satisfactionRatesForLastDays(String userId, int days);
}
