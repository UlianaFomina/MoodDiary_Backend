package com.mood.diary.service.goal;

import com.mood.diary.service.goal.model.Goal;
import com.mood.diary.service.goal.model.request.CreateGoalRequest;
import com.mood.diary.service.goal.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/goal")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @GetMapping("/not-expired/{userId}")
    public List<Goal> findNotExpired(@PathVariable String userId) {
        return goalService.findNotExpiredGoalsByUserId(userId);
    }

    @GetMapping("/{userId}")
    public List<Goal> findAllByUserId(@PathVariable String userId) {
        return goalService.findByUserId(userId);
    }

    @PostMapping
    public void attachByUserId(@RequestBody CreateGoalRequest createGoalRequest) {
        goalService.attachGoalByUserId(createGoalRequest);
    }
}
