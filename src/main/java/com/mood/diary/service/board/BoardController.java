package com.mood.diary.service.board;

import com.mood.diary.service.board.model.response.BoardResponse;
import com.mood.diary.service.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{userId}")
    public List<BoardResponse> findByUserId(@PathVariable String userId) {
        return boardService.findTodosByUserIdForCurrentWeek(userId);
    }

}
