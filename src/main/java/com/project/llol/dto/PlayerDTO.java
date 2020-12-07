package com.project.llol.dto;

import lombok.Data;

@Data
public class PlayerDTO {
    private String platformId;
    private String accountId;
    private String summonerName;
    private String summonerId;
    private String currentPlatformId;
    private String currentAccountId;
    private String matchHistoryUri;
    private int profileIcon;
}
