package com.mood.diary.service.story.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Story {

    private String id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String content;
    private Double satisfactionRate;

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
