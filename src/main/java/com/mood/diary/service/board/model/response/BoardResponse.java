package com.mood.diary.service.board.model.response;

import com.mood.diary.service.board.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {
    private String name;
    private String description;
    private BoardFlag flag;
    private BoardFrequency frequency;
    private BoardImportance importance;
    private BoardStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long spendTime;
    private String userId;

    public static BoardResponse of(Board board) {
        return BoardResponse.builder()
                .name(board.getName())
                .description(board.getDescription())
                .flag(board.getFlag())
                .frequency(board.getFrequency())
                .importance(board.getImportance())
                .status(board.getStatus())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .spendTime(board.getSpendTime())
                .userId(board.getUserId())
                .build();
    }
}
