package com.mood.diary.service.story;

import com.mood.diary.service.AbstractServiceTest;
import com.mood.diary.service.auth.exception.variants.UserNotFoundException;
import com.mood.diary.service.story.exception.variants.StoryNotFoundException;
import com.mood.diary.service.story.model.Story;
import com.mood.diary.service.story.model.request.StoryRequest;
import com.mood.diary.service.story.model.request.UpdateStoryRequest;
import com.mood.diary.service.story.service.StoryService;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StoryServiceTest extends AbstractServiceTest {

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
    void attachToUserById_throwIdMustBeSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> storyService.attachToUserById(new StoryRequest()));
    }

    @Test
    void attachToUserById_throwUserNotFound() {
        StoryRequest storyRequest = StoryRequest.builder()
                .userId("userId")
                .content("content")
                .build();

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> storyService.attachToUserById(storyRequest));
    }

    @Test
    void attachToUserById_pass() {
        AuthUser user = initDefaultUser("username", "email@gmail.com");
        AuthUser savedUser = authUserService.save(user);

        String userId = savedUser.getId();

        StoryRequest.StoryRequestBuilder builder = StoryRequest
                .builder()
                .userId(userId);

        storyService.attachToUserById(builder.content("content1").build());
        storyService.attachToUserById(builder.content("content2").build());

        List<Story> userStories = storyService.findAllByUserId(userId);

        assertThat(userStories.stream().map(Story::getContent))
                .containsExactlyInAnyOrder("content1", "content2");
    }

    @Test
    void findAllByUserId_empty() {
        AuthUser user = initDefaultUser("username", "email@gmail.com");
        AuthUser savedUser = authUserService.save(user);

        List<Story> stories = storyService.findAllByUserId(savedUser.getId());

        assertThat(stories).isEmpty();
    }

    @Test
    void findAllByUserId() {
        AuthUser user = initDefaultUser("username", "email@gmail.com");
        AuthUser savedUser = authUserService.save(user);

        String userId = savedUser.getId();

        StoryRequest.StoryRequestBuilder builder = StoryRequest
                .builder()
                .userId(userId);

        storyService.attachToUserById(builder.content("content1").build());
        storyService.attachToUserById(builder.content("content2").build());
        storyService.attachToUserById(builder.content("content3").build());
        storyService.attachToUserById(builder.content("content4").build());

        List<Story> stories = storyService.findAllByUserId(savedUser.getId());

        assertThat(stories.stream().map(Story::getContent))
                .containsExactlyInAnyOrder("content1", "content2", "content3", "content4");
    }

    @Test
    void updateByUserIdStoryId() {
        AuthUser user = initDefaultUser("username", "email@gmail.com");
        AuthUser savedUser = authUserService.save(user);

        String userId = savedUser.getId();

        StoryRequest.StoryRequestBuilder builder = StoryRequest
                .builder()
                .userId(userId);

        String storyId = storyService.attachToUserById(builder.content("content1").build());
        storyService.attachToUserById(builder.content("content2").build());
        storyService.attachToUserById(builder.content("content3").build());
        storyService.attachToUserById(builder.content("content4").build());

        String newUpdatedContent = "newUpdatedContent";
        UpdateStoryRequest request = UpdateStoryRequest.builder()
                .storyId(storyId)
                .userId(userId)
                .content(newUpdatedContent)
                .build();

        Story storyBefore = storyService.findByUserIdAndStoryId(userId, storyId);
        storyService.updateByIdAndUserId(request);
        Story updatedStory = storyService.findByUserIdAndStoryId(userId, storyId);

        List<Story> stories = storyService.findAllByUserId(userId);

        assertThat(updatedStory.getContent()).isEqualTo(newUpdatedContent);
        assertThat(updatedStory.getCreatedAt()).isEqualTo(storyBefore.getCreatedAt());
        assertThat(updatedStory.getUpdatedAt()).isNotEqualTo(storyBefore.getUpdatedAt());

        assertThat(stories.stream().map(Story::getContent))
                .containsExactlyInAnyOrder(newUpdatedContent, "content2", "content3", "content4");
    }

    @Test
    void update_throwUserNotFoundException() {
        UpdateStoryRequest request = UpdateStoryRequest.builder()
                .userId("userId")
                .content("content")
                .storyId("storyId")
                .build();

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> storyService.updateByIdAndUserId(request));
    }

    @Test
    void update_throwIllegalArgumentException() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> storyService.updateByIdAndUserId(new UpdateStoryRequest()));
    }

    @Test
    void update_throwStoryNotFound() {
        AuthUser user = initDefaultUser("username", "email@gmail.com");

        UpdateStoryRequest request = UpdateStoryRequest.builder()
                .userId(user.getId())
                .content("content")
                .storyId("storyId")
                .build();

        assertThatExceptionOfType(StoryNotFoundException.class)
                .isThrownBy(() -> storyService.updateByIdAndUserId(request));
    }
}
