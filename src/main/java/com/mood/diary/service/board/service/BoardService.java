package com.mood.diary.service.board.service;

import com.mood.diary.service.board.model.Board;
import com.mood.diary.service.board.model.request.BoardRequest;
import com.mood.diary.service.board.model.request.UpdateBoardRequest;
import com.mood.diary.service.board.model.response.BoardResponse;

import java.util.List;

public interface BoardService {
    void attachTodoByUserId(BoardRequest boardRequest);
    List<BoardResponse> findTodosByUserIdForCurrentWeek(String userId);
    List<Board> findTodoByUserId(String userId);
    Board findTodoByUserAndBoardId(String userId, String boardId);
    List<Board> findAllByUserId(String userId);
    void updateTodoByUserId(UpdateBoardRequest updateBoardRequest);
}
