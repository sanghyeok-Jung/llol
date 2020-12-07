package com.project.llol.dto;

import lombok.Data;

@Data
public class ParticipantsStatsDTO {
    private int participantId;
    private boolean win;
    private int item0;
    private int item1;
    private int item2;
    private int item3;
    private int item4;
    private int item5;
    private int item6;

    private int kills;
    private int deaths;
    private int assists;

    private int largestKillingSpree;
    private int largestMultiKill;
    private int killingSprees;
    private long longestTimeSpentLiving;
    private int doubleKills;
    private int tripleKills;
    private int quadraKills;
    private int pentaKills;
    private int unrealKills;

    private long totalDamageDealt;
    private long magicDamageDealt;
    private long physicalDamageDealt;
    private long trueDamageDealt;
    private long largestCriticalStrike;
    private long totalDamageDealtToChampions;
    private long magicDamageDealtToChampions;
    private long physicalDamageDealtToChampions;
    private long trueDamageDealtToChampions;
    private long totalHeal;
    private long totalUnitsHealed;
    private long damageSelfMitigated;
    private long damageDealtToObjectives;
    private long damageDealtToTurrets;
    private int visionScore;
    private long timeCCingOthers;
    private long totalDamageTaken;
    private long magicalDamageTaken;
    private long physicalDamageTaken;
    private long trueDamageTaken;
    private long goldEarned;
    private long goldSpent;
    private int turretKills;
    private int inhibitorKills;
    private int totalMinionsKilled;
    private int neutralMinionsKilled;
    private int neutralMinionsKilledTeamJungle;
    private int neutralMinionsKilledEnemyJungle;
    private long totalTimeCrowdControlDealt;
    private int champLevel;
    private int visionWardsBoughtInGame;
    private int sightWardsBoughtInGame;
    private int wardsPlaced;
    private int wardsKilled;

    private boolean firstBloodKill;
    private boolean firstBloodAssist;
    private boolean firstTowerKill;
    private boolean firstTowerAssist;
    private boolean firstInhibitorKill;
    private boolean firstInhibitorAssist;

    private int combatPlayerScore;
    private int objectivePlayerScore;
    private int totalPlayerScore;
    private int totalScoreRank;

    private int playerScore0;
    private int playerScore1;
    private int playerScore2;
    private int playerScore3;
    private int playerScore4;
    private int playerScore5;
    private int playerScore6;
    private int playerScore7;
    private int playerScore8;
    private int playerScore9;

    private long perk0;
    private long perk0Var1;
    private long perk0Var2;
    private long perk0Var3;
    private long perk1;
    private long perk1Var1;
    private long perk1Var2;
    private long perk1Var3;
    private long perk2;
    private long perk2Var1;
    private long perk2Var2;
    private long perk2Var3;
    private long perk3;
    private long perk3Var1;
    private long perk3Var2;
    private long perk3Var3;
    private long perk4;
    private long perk4Var1;
    private long perk4Var2;
    private long perk4Var3;
    private long perk5;
    private long perk5Var1;
    private long perk5Var2;
    private long perk5Var3;
    private long perkPrimaryStyle;
    private long perkSubStyle;
    private long statPerk0;
    private long statPerk1;
    private long statPerk2;
}
