package com.project.llol.dto;

import lombok.Data;

@Data
public class ChampionSpellsDTO {
    private ImageDTO image;
    private String cooldownBurn;
    private String name;
    private String description;
    private String costBurn;
}
