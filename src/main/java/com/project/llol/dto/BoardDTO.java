package com.project.llol.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BoardDTO {
    private int boardnum;
    private String boardwriter;
    private String boardtitle;
    private String boardcontent;
    private String boardimage;
    private String boardyoutube;
    private Timestamp boarddate;
    private int boardvisit;
}
