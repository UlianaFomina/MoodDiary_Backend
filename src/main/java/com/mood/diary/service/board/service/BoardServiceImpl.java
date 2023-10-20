package com.mood.diary.service.board.service;

import com.mood.diary.service.board.model.Board;
import com.mood.diary.service.board.model.request.BoardRequest;
import com.mood.diary.service.board.model.request.UpdateBoardRequest;
import com.mood.diary.service.board.model.response.BoardResponse;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final AuthUserService authUserService;

    @Override
    public void attachTodoByUserId(BoardRequest boardRequest) {
        AuthUser dbUser = authUserService.findById(boardRequest.getUserId());

        Board board = Board.builder()
                .name(boardRequest.getName())
                .description(boardRequest.getDescription())
                .flag(boardRequest.getFlag())
                .frequency(boardRequest.getFrequency())
                .importance(boardRequest.getImportance())
                .status(boardRequest.getStatus())
                .spendTime(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .userId(boardRequest.getUserId())
                .build();

        dbUser.getTodos().add(board);

        authUserService.save(dbUser);
    }

    @Override
    public List<BoardResponse> findTodosByUserIdForCurrentWeek(String userId) {
        AuthUser dbUser = authUserService.findById(userId);

        return dbUser.getTodos()
                .stream()
                .filter(this::filterByCurrentWeek)
                .map(BoardResponse::of)
                .toList();
    }

    @Override
    public List<Board> findTodoByUserId(String userId) {
        AuthUser dbUser = authUserService.findById(userId);

        return dbUser.getTodos()
                .stream()
                .filter(e -> userId.equals(e.getUserId()))
                .toList();
    }

    @Override
    public Board findTodoByUserAndBoardId(String userId, String boardId) {
        return findTodoByUserId(userId)
                .stream()
                .filter(e -> boardId.equals(e.getId()))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public List<Board> findAllByUserId(String userId) {
        AuthUser dbUser = authUserService.findById(userId);

        return dbUser.getTodos();
    }

    @Override
    public void updateTodoByUserId(UpdateBoardRequest updateBoardRequest) {
        AuthUser user = authUserService.findById(updateBoardRequest.getUserId());

        Board dbBoardToUpdate = Board.builder()
                .name(updateBoardRequest.getName())
                .id(updateBoardRequest.getBoardId())
                .description(updateBoardRequest.getDescription())
                .flag(updateBoardRequest.getFlag())
                .frequency(updateBoardRequest.getFrequency())
                .importance(updateBoardRequest.getImportance())
                .status(updateBoardRequest.getStatus())
                .spendTime(updateBoardRequest.getSpendTime())
                .userId(updateBoardRequest.getUserId())
                .build();

        List<Board> todosWithoutUpdated = user.getTodos()
                .stream()
                .filter(e -> !dbBoardToUpdate.getId().equals(e.getId()))
                .collect(Collectors.toList());

        todosWithoutUpdated.add(dbBoardToUpdate);
        user.setTodos(todosWithoutUpdated);

        authUserService.save(user);
    }

    private boolean filterByCurrentWeek(Board todo) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return todo.getCreatedAt().isEqual(startOfWeek) ||
                todo.getCreatedAt().isEqual(endOfWeek) ||
                (todo.getCreatedAt().isAfter(startOfWeek) && todo.getCreatedAt().isBefore(endOfWeek));
    }
}
