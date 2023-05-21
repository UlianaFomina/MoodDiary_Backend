package com.mood.diary.service.story;

import com.mood.diary.service.AbstractServiceTest;
import com.mood.diary.service.auth.exception.variants.UserNotFoundException;
import com.mood.diary.service.story.model.StatisticsGraphResponse;
import com.mood.diary.service.story.model.Story;
import com.mood.diary.service.story.service.StoryService;
import com.mood.diary.service.story.service.statistics.StoryStatisticsService;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import com.mood.diary.service.util.EntityBuilder;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StoryStatisticsServiceTest extends AbstractServiceTest {

    @Autowired
    StoryStatisticsService storyStatisticsService;

    @Autowired
    StoryService storyService;

    @Autowired
    AuthUserService authUserService;

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo:4.0.10");

    @Container
    static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        String redisHost = String.format("redis://%s:%s", redisContainer.getHost(), redisContainer.getMappedPort(6379));
        Supplier<Object> redisSupplier = () -> redisHost;

        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
        registry.add("redis.uri", redisSupplier);
    }

    @Test
    void getStoriesByUserIdAndTimeRange() {
        AuthUser savedUser = getUserWithTimeRangeStories();

        List<Story> lastTwoDaysStories = storyStatisticsService.getForLastDays(savedUser.getId(), 3);
        List<Story> allSavedUserStories = storyService.findAllByUserId(savedUser.getId());

        assertThat(allSavedUserStories.size()).isEqualTo(6);
        assertThat(lastTwoDaysStories.stream().map(Story::getContent))
                .contains("two", "one", "now");
    }

    @Test
    void getStoriesByUserIdAndTimeRange_empty() {
        AuthUser user = initDefaultUser("username", "email@gmail.com");

        List<Story> lastTwoDaysStories = storyStatisticsService.getForLastDays(user.getId(), 3);

        assertThat(lastTwoDaysStories).isEmpty();
    }

    @Test
    void getStoriesByUserIdAndTimeRange_wrong() {
        AuthUser savedUser = getUserWithTimeRangeStories();

        List<Story> lastTwoDaysStories = storyStatisticsService.getForLastDays(savedUser.getId(), -5);
        List<Story> allSavedUserStories = storyService.findAllByUserId(savedUser.getId());

        assertThat(allSavedUserStories.size()).isEqualTo(6);
        assertThat(lastTwoDaysStories).isEmpty();
    }

    @Test
    void getStoriesByUserIdAndTimeRange_throwUserNotFoundException() {
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> storyStatisticsService.getForLastDays("randomUserId", 3));
    }

    @Test
    void getSatisfactionRatesInTimeRange() {
        AuthUser savedUser = getUserWithTimeRangeStories();

        List<StatisticsGraphResponse> response = storyStatisticsService.satisfactionRatesForLastDays(savedUser.getId(), 3);
        List<Story> allSavedUserStories = storyService.findAllByUserId(savedUser.getId());

        assertThat(allSavedUserStories.size()).isEqualTo(6);
        assertThat(response.stream().map(StatisticsGraphResponse::satisfactionRate))
                .containsExactlyInAnyOrder(0.4, 0.4, 0.4);
    }

    @Test
    void getSatisfactionRatesInTimeRange_empty() {
        AuthUser savedUser = initDefaultUser("username", "password");

        List<StatisticsGraphResponse> response = storyStatisticsService.satisfactionRatesForLastDays(savedUser.getId(), 3);

        assertThat(response).isEmpty();
    }

    @Test
    void getSatisfactionRatesInTimeRange_wrong() {
        AuthUser savedUser = getUserWithTimeRangeStories();

        List<StatisticsGraphResponse> response = storyStatisticsService.satisfactionRatesForLastDays(savedUser.getId(), -5);
        List<Story> allSavedUserStories = storyService.findAllByUserId(savedUser.getId());

        assertThat(allSavedUserStories.size()).isEqualTo(6);
        assertThat(response).isEmpty();
    }

    @Test
    void getSatisfactionRatesInTimeRange_throwUserNotFoundException() {
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> storyStatisticsService.satisfactionRatesForLastDays("randomId", -5));

    }

    private AuthUser getUserWithTimeRangeStories() {
        AuthUser user = initDefaultUser("username", "email@gmail.com");

        Story fiveDaysAgoStory = EntityBuilder
                .buildWithTime(user.getId(), "five", LocalDateTime.now().minusDays(5));
        Story fourDaysAgoStory = EntityBuilder
                .buildWithTime(user.getId(), "four", LocalDateTime.now().minusDays(4));
        Story threeDaysAgoStory = EntityBuilder
                .buildWithTime(user.getId(), "three", LocalDateTime.now().minusDays(3));
        Story twoDaysAgoStory = EntityBuilder
                .buildWithTime(user.getId(), "two", LocalDateTime.now().minusDays(2));
        Story oneDayAgeStory = EntityBuilder
                .buildWithTime(user.getId(), "one", LocalDateTime.now().minusDays(1));
        Story nowStory = EntityBuilder
                .buildWithTime(user.getId(), "now", LocalDateTime.now());

        user.getStories().addAll(List.of(
                fiveDaysAgoStory, fourDaysAgoStory, threeDaysAgoStory,
                twoDaysAgoStory, oneDayAgeStory, nowStory
        ));

        return authUserService.save(user);
    }
}
