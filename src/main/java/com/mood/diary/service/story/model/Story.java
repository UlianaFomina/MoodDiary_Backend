package com.mood.diary.service.story.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Story entity, how it's stored in database")
public class Story {

    @Schema(description = "Unique identifier of story",
            example = "random-generated-uuid")
    private String id;

    @NotNull
    @Schema(description = "Creation date",
            example = "2003-02-17", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createdAt;

    @NotNull
    @Schema(description = "Last updated date. On first creation equal to 'createdAt'",
            example = "2002-01-09", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updatedAt;

    @Max(200)
    @Schema(description = "Story created by user",
            example = """
                    Passionate learner, creative thinker, and avid explorer.
                    Embracing life's challenges, seeking growth, and spreading positivity
                    Dreamer, doer, and lover of all things art.
                    """, maxLength = 200, requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @NotNull
    @Schema(description = "Satisfaction Rate value",
            example = """
                    Satisfaction rate that calculated by AI.
                    How your text is happy or not
                    """, requiredMode = Schema.RequiredMode.REQUIRED)
    private Double satisfactionRate;

    @NotNull
    @Schema(description = "Identifier of user who wrote this story",
            example = "userId", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Story setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Story setContent(String content) {
        this.content = content;
        return this;
    }

    public Story setSatisfactionRate(Double satisfactionRate) {
        this.satisfactionRate = satisfactionRate;
        return this;
    }
}
