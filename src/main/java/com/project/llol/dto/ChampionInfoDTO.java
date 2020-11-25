package com.project.llol.dto;

import lombok.Data;

@Data
public class ChampionInfoDTO {
    // 챔피언 성향 프로그래스바 표시용
    private int attack;
    private int defense;
    private int magic;
    private int difficulty;
}
