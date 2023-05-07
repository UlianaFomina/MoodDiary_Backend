package com.mood.diary.service.story.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStoryRequest {
    @NotNull
    private String storyId;

    @NotNull
    private String userId;

    @NotNull
    private String content;
}
