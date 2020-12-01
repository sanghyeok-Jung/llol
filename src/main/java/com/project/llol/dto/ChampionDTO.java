package com.project.llol.dto;

import lombok.Data;

@Data
public class ChampionDTO {
    private String version;
    private String key;

    private String name;
    private String id;
    private String title;
    private String blurb; // 배경 스토리

    private ImageDTO image;
    private ChampionInfoDTO info;

    private String[] tags;
    private String partype; // 코스트 타입

    private ChampionStatsDTO stats;
}
