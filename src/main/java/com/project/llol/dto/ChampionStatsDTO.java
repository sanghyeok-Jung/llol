package com.project.llol.dto;

import lombok.Data;

@Data
public class ChampionStatsDTO {
    private int hp;
    private int hpperlevel;
    private int mp;
    private int mpperlevel;
    private int movespeed;
    private double armor;
    private double armorperlevel;
    private double spellblock;
    private double spellblockperlevel;
    private int attackrange;
    private double hpregen;
    private double hpregenperlevel;
    private double mpregen;
    private double mpregenperlevel;
    private double crit;
    private double critperlevel;
    private double attackdamage;
    private double attackdamageperlevel;
    private double attackspeedperlevel;
    private double attackspeed;
}
