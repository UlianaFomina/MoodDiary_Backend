package com.mood.diary.service.story.service;

import com.mood.diary.service.story.model.Story;
import com.mood.diary.service.story.model.request.StoryRequest;
import com.mood.diary.service.story.model.request.UpdateStoryRequest;

import java.util.List;

public interface StoryService {
    String attachToUserById(StoryRequest storyRequest);
    List<Story> findAllByUserId(String userId);
    Story findByUserIdAndStoryId(String userId, String storyId);
    void updateByIdAndUserId(UpdateStoryRequest updateStoryRequest);
}
