package com.project.llol.dto;

import lombok.Data;

import java.util.List;

@Data
public class MatchDTO {
    private long gameId;
    private String platformId;
    private long gameCreation;
    private long gameDuration;
    private int queueId;
    private int mapId;
    private int seasonId;
    private String gameVersion;
    private String gameMode;
    private String gameType;
    private List<TeamsDTO> teams;
    private List<ParticipantsDTO> participants;
    private List<ParticipantIdentitiesDTO> participantIdentities;
}
