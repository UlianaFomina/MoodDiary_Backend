package com.mood.diary.service.goal.service;

import com.mood.diary.service.goal.model.Goal;
import com.mood.diary.service.goal.model.request.CreateGoalRequest;

import java.util.List;

public interface GoalService {
    Goal findByUserIdGoalId(String userId, String goalId);
    List<Goal> findByUserId(String userId);
    void attachGoalByUserId(CreateGoalRequest createGoalRequest);
    List<Goal> findNotExpiredGoalsByUserId(String userId);
}
