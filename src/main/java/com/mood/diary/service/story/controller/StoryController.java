package com.mood.diary.service.story.controller;

import com.mood.diary.service.story.model.Story;
import com.mood.diary.service.story.model.request.StoryRequest;
import com.mood.diary.service.story.model.request.UpdateStoryRequest;
import com.mood.diary.service.story.service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/story")
@RequiredArgsConstructor
@Tag(
        name = "Story Controller",
        description = "Endpoints for manipulating with stories attached to user"
)
public class StoryController {

    private final StoryService storyService;

    @Operation(
            summary = "Get user stories",
            description = """
                    Give you all user's stories attached by user's 'id'
                    """
    )
    @GetMapping("{userId}")
    public List<Story> getAllByUserId(
            @Parameter(description = "User identifier", example = "userId", required = true)
            @Valid @NotNull @PathVariable String userId) {
        return storyService.findAllByUserId(userId);
    }

    @Operation(
            summary = "Get user story",
            description = """
                    Give you only one story by 2 identifiers: user's id and story's id
                    """
    )
    @GetMapping("{userId}/{storyId}")
    public Story getByUserIdAndStoryId(
            @Parameter(description = "User identifier", example = "userId", required = true)
            @Valid @NotNull @PathVariable String userId,
            @Parameter(description = "Story identifier", example = "storyId", required = true)
            @Valid @NotNull @PathVariable String storyId) {
        return storyService.findByUserIdAndStoryId(userId, storyId);
    }

    @Operation(
            summary = "Attach story to exists user",
            description = """
                    Add 1 story to user
                    """
    )
    @PostMapping
    public void attachStoryByUserId(
            @Parameter(description = "Body of story", required = true)
            @Valid @RequestBody StoryRequest storyRequest) {
        storyService.attachToUserById(storyRequest);
    }

    @Operation(
            summary = "Update exists story on exists user",
            description = """
                    Update exists story content field, with recounting rate parameter
                    Also, updateDateTime parameter will be changed on current time.
                    """
    )
    @PutMapping
    public void updateByUserIdAndStoryId(
            @Parameter(description = "Body of story to update", required = true)
            @Valid @RequestBody UpdateStoryRequest updateStoryRequest) {
        storyService.updateByIdAndUserId(updateStoryRequest);
    }
}
