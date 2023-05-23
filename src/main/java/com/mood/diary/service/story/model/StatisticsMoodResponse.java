package com.mood.diary.service.story.model;

import java.util.List;

public record StatisticsMoodResponse(
        List<StatisticsGraphResponse> statistics,
        String moodPhrase
) {
}
