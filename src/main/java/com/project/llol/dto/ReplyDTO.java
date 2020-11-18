package com.project.llol.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReplyDTO {
    private int replynum;
    private int boardnum;
    private String replywriter;
    private String replycontent;
    private Timestamp replydate;
}
