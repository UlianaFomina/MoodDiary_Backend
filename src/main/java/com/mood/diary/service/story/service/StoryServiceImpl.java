package com.mood.diary.service.story.service;

import com.mood.diary.service.standfort.AnalyzeService;
import com.mood.diary.service.story.exception.variants.StoryNotFoundException;
import com.mood.diary.service.story.model.Story;
import com.mood.diary.service.story.model.request.StoryRequest;
import com.mood.diary.service.story.model.request.UpdateStoryRequest;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final AuthUserService authUserService;
    private final AnalyzeService analyzeService;

    @Override
    public String attachToUserById(StoryRequest storyRequest) {
        AuthUser dbUser = authUserService.findById(storyRequest.getUserId());

        double satisfactionRate = analyzeService.satisfaction(storyRequest.getContent());

        Story story = Story.builder()
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .content(storyRequest.getContent())
                .userId(storyRequest.getUserId())
                .id(UUID.randomUUID().toString())
                .satisfactionRate(satisfactionRate)
                .build();

        dbUser.getStories().add(story);

        authUserService.save(dbUser);

        return story.getId();
    }

    @Override
    public List<Story> findAllByUserId(String userId) {
        AuthUser user = authUserService.findById(userId);

        return user.getStories();
    }

    @Override
    public Story findByUserIdAndStoryId(String userId, String storyId) {
        return findAllByUserId(userId)
                .stream()
                .filter(e -> e.getId().equals(storyId))
                .findFirst()
                .orElseThrow(() -> new StoryNotFoundException(String.format("Story with id: '%s' not found!", storyId)));
    }

    @Override
    public void updateByIdAndUserId(UpdateStoryRequest updateStoryRequest) {
        AuthUser user = authUserService.findById(updateStoryRequest.getUserId());

        double satisfactionRate = analyzeService.satisfaction(updateStoryRequest.getContent());

        Story story = findByUserIdAndStoryId(updateStoryRequest.getUserId(), updateStoryRequest.getStoryId())
                .setUpdatedAt(LocalDateTime.now())
                .setSatisfactionRate(satisfactionRate)
                .setContent(updateStoryRequest.getContent());

        List<Story> stories = user.getStories()
                .stream()
                .filter(e -> !e.getId().equals(story.getId()))
                .collect(Collectors.toList());

        stories.add(story);
        user.setStories(stories);

        authUserService.save(user);
    }
}
