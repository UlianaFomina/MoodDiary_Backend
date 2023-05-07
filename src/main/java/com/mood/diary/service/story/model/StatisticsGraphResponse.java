package com.mood.diary.service.story.model;

import java.util.List;

public record StatisticsGraphResponse (
    List<Double> satisfactionRates
) {}
