package com.project.llol.dto;

import lombok.Data;

@Data
public class ParticipantsDTO {
    private int participantId;
    private int teamId;
    private int championId;
    private int spell1Id;
    private int spell2Id;
    private ParticipantsStatsDTO stats;
    private TimeLineDTO timeline;
}
