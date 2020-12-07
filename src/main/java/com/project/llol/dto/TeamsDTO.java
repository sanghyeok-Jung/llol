package com.project.llol.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamsDTO {
    private int teamId;
    private String win;

    private boolean firstBlood;
    private boolean firstTower;
    private boolean firstInhibitor;
    private boolean firstBaron;
    private boolean firstDragon;
    private boolean firstRiftHerald;

    private int towerKills;
    private int inhibitorKills;
    private int baronKills;
    private int dragonKills;
    private int vilemawKills;
    private int riftHeraldKills;
    private int dominionVictoryScore;

    private List<BansDTO> bans;
}
