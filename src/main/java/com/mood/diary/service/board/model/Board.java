package com.mood.diary.service.board.model;

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
@Schema(description = "Board entity, how it's stored in database")
public class Board {
    private String id;
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
}
