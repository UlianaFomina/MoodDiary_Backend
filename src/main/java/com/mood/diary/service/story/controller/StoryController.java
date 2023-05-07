package com.mood.diary.service.story.controller;

import com.mood.diary.service.story.model.Story;
import com.mood.diary.service.story.model.request.StoryRequest;
import com.mood.diary.service.story.model.request.UpdateStoryRequest;
import com.mood.diary.service.story.service.StoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/story")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @GetMapping("{userId}")
    public List<Story> getAllByUserId(@Valid @NotNull @PathVariable String userId) {
        return storyService.findAllByUserId(userId);
    }

    @GetMapping("{userId}/{storyId}")
    public Story getByUserIdAndStoryId(@Valid @NotNull @PathVariable String userId,
                                       @Valid @NotNull @PathVariable String storyId) {
        return storyService.findByUserIdAndStoryId(userId, storyId);
    }

    @PostMapping
    public void attachStoryByUserId(@Valid @RequestBody StoryRequest storyRequest) {
        storyService.attachToUserById(storyRequest);
    }

    @PutMapping
    public void updateByUserIdAndStoryId(@Valid @RequestBody UpdateStoryRequest updateStoryRequest) {
        storyService.updateByIdAndUserId(updateStoryRequest);
    }
}
