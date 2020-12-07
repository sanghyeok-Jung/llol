package com.project.llol.dto;

import lombok.Data;

import java.util.Map;

@Data
public class TimeLineDTO {
    private int participantId;
    private Map<String, Double> creepsPerMinDeltas;
    private Map<String, Double> xpPerMinDeltas;
    private Map<String, Double> goldPerMinDeltas;
    private Map<String, Double> csDiffPerMinDeltas;
    private Map<String, Double> xpDiffPerMinDeltas;
    private Map<String, Double> damageTakenPerMinDeltas;
    private Map<String, Double> damageTakenDiffPerMinDeltas;
    private String role;
    private String lane;
}
