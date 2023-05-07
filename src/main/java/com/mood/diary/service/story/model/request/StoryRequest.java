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
@Schema(description = "Story request body to create new one and attach to user")
public class StoryRequest {

    @Size(max = 200)
    @Schema(description = "Short information about user",
            example = """
                    Passionate learner, creative thinker, and avid explorer.
                    Embracing life's challenges, seeking growth, and spreading positivity
                    Dreamer, doer, and lover of all things art.
                    """, maxLength = 200, requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @NotNull
    @Schema(description = "Identifier of user, to whom attach story",
            example = "userId",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;
}
