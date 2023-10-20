package com.mood.diary.service.goal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Goal entity, how it's stored in database")
public class Goal {
    private String id;
    private LocalDateTime deadline;
    private GoalStatus status;
    private List<GoalTask> goalTasks;
    private String userId;
}
