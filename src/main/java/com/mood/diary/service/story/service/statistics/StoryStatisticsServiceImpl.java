package com.mood.diary.service.story.service.statistics;

import com.mood.diary.service.story.model.StatisticsGraphResponse;
import com.mood.diary.service.story.model.StatisticsMoodResponse;
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
    public StatisticsMoodResponse satisfactionRatesForLastDays(String userId, int days) {
        List<StatisticsGraphResponse> statisticsResponse = getForLastDays(userId, days)
                .stream()
                .map(el -> new StatisticsGraphResponse(el.getSatisfactionRate(), el.getCreatedAt()))
                .toList();

        String moodPhrase = getMoodPhrase(statisticsResponse);
        return new StatisticsMoodResponse(statisticsResponse, moodPhrase);
    }

    private String getMoodPhrase(List<StatisticsGraphResponse> statisticsResponse) {
        double averageMood = statisticsResponse
                .stream()
                .mapToDouble(StatisticsGraphResponse::satisfactionRate)
                .average()
                .orElse(0.0);

        String tip;
        if (averageMood >= 1.5) {
            tip = "Your mood is excellent! Keep up the positive vibes!";
        } else if (averageMood >= 0.5) {
            tip = "Your mood is good! Stay optimistic!";
        } else if (averageMood >= -0.5) {
            tip = "Your mood is neutral. Keep a balanced mindset!";
        } else if (averageMood >= -1.5) {
            tip = "Your mood is not so great. Try to find some positivity!";
        } else {
            tip = "Your mood is very poor. Reach out for support and take care of yourself!";
        }

        return tip;
    }

    private Predicate<Story> timeRangePredicate(LocalDateTime start, LocalDateTime end) {
        return e -> e.getCreatedAt().isAfter(start) && e.getCreatedAt().isBefore(end);
    }
}
