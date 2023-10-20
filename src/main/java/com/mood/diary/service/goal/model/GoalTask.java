package com.mood.diary.service.goal.model;

import com.mood.diary.service.board.model.BoardFlag;
import com.mood.diary.service.board.model.BoardFrequency;
import com.mood.diary.service.board.model.BoardImportance;
import com.mood.diary.service.board.model.BoardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Goal entity, how it's stored in database")
public class GoalTask {
    private String id;
    private String name;
    private String description;
    private BoardStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long spendTime;
    private int retryAttempts;
    private String goalId;
}
