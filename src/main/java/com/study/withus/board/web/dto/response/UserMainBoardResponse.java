package com.study.withus.board.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMainBoardResponse {

    private Long id;
    private String title;
    private String content;
    private Long commentsCount;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;


}
