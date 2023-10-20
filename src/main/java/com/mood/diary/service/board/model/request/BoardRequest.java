package com.mood.diary.service.board.model.request;

import com.mood.diary.service.board.model.BoardFlag;
import com.mood.diary.service.board.model.BoardFrequency;
import com.mood.diary.service.board.model.BoardImportance;
import com.mood.diary.service.board.model.BoardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Story request body to create new one and attach to user")
public class BoardRequest {
    private String name;
    private String description;
    private BoardFlag flag;
    private BoardFrequency frequency;
    private BoardImportance importance;
    private BoardStatus status;
    private long spendTime;
    private String userId;
}
