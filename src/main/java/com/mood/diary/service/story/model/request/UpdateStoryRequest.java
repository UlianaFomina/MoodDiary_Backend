package com.mood.diary.service.story.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Object to update exists user's story")
public class UpdateStoryRequest {

    @NotNull
    @Schema(description = "Identifier of story, which story update",
            example = "storyId",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String storyId;

    @NotNull
    @Schema(description = "Identifier of user, on whom user update story",
            example = "userId",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @Size(max = 200)
    @Schema(description = "Short information which will be updated",
            example = """
                    Passionate learner, creative thinker, and avid explorer.
                    Embracing life's challenges, seeking growth, and spreading positivity
                    Dreamer, doer, and lover of all things art.
                    """, maxLength = 200, requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;
}
