package com.mood.diary.service.goal.service.impl;

import com.mood.diary.service.goal.model.Goal;
import com.mood.diary.service.goal.model.GoalStatus;
import com.mood.diary.service.goal.model.request.CreateGoalRequest;
import com.mood.diary.service.goal.service.GoalService;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final AuthUserService authUserService;

    @Override
    public Goal findByUserIdGoalId(String userId, String goalId) {
        AuthUser user = authUserService.findById(userId);

        return user.getGoals()
                .stream()
                .filter(e -> e.getId().equals(goalId))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public List<Goal> findByUserId(String userId) {
        AuthUser user = authUserService.findById(userId);

        return user.getGoals();
    }

    @Override
    public void attachGoalByUserId(CreateGoalRequest createGoalRequest) {
        AuthUser user = authUserService.findById(createGoalRequest.getUserId());

        Goal goalToSave = Goal.builder()
                .id(UUID.randomUUID().toString())
                .userId(createGoalRequest.getUserId())
                .status(GoalStatus.CREATED)
                .deadline(createGoalRequest.getDeadline())
                .goalTasks(createGoalRequest.getTasks())
                .build();

        user.getGoals().add(goalToSave);

        authUserService.save(user);
    }

    @Override
    public List<Goal> findNotExpiredGoalsByUserId(String userId) {
        return findByUserId(userId)
                .stream()
                .filter(goal -> goal.getDeadline().isBefore(LocalDateTime.now()))
                .toList();
    }
}
