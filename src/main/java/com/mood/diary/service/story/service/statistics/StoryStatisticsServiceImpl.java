package com.mood.diary.service.story.service.statistics;

import com.mood.diary.service.story.model.StatisticsGraphResponse;
import com.mood.diary.service.story.model.Story;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class StoryStatisticsServiceImpl implements StoryStatisticsService {

    private final AuthUserService authUserService;

    @Override
    public List<Story> getForLastDays(String userId, int days) {
        AuthUser user = authUserService.findById(userId);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(days);

        return user.getStories()
                .stream()
                .filter(timeRangePredicate(startDate, now))
                .toList();
    }

    @Override
    public List<StatisticsGraphResponse> satisfactionRatesForLastDays(String userId, int days) {
        return getForLastDays(userId, days)
                .stream()
                .map(el -> new StatisticsGraphResponse(el.getSatisfactionRate(), el.getCreatedAt()))
                .toList();
    }

    private Predicate<Story> timeRangePredicate(LocalDateTime start, LocalDateTime end) {
        return e -> e.getCreatedAt().isAfter(start) && e.getCreatedAt().isBefore(end);
    }
}
