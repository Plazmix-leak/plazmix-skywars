package net.plazmix.skywars.util;

public final class GameConstants {

    public static final String TITLE                        = ("SkyWars");
    public static final String PREFIX                       = String.format("§d§l%s §8:: §f", GameConstants.TITLE);

    // Installer cache.
    public static final String ISLANDS_COUNT_CACHE          = "IslandsCount";
    public static final String ISLANDS_SPAWNS_CACHE         = "IslandsSpawns";
    public static final String ISLAND_CHESTS_CACHE          = "IslandChestsLocations";
    public static final String MIDDLE_CHESTS_CACHE          = "MiddleChestsLocations";

    // Skywars ingame cache.
    public static final String INGAME_LOOT_REFILLS_COUNT    = "LootRefillsCount";
    public static final String INGAME_PLAYER_ISLAND_LOC     = "PlayerIslandLoc";
    public static final String INGAME_WINNER_TEAM           = "SkywarsWinner";
    public static final String INGAME_PLAYER_KILLS          = "IngameKills";

    // Skywars database cache.
    public static final String DATABASE_PLAYER_DAMN_PERCENT = "DamnPercent";
    public static final String DATABASE_PLAYER_SOULS        = "Souls";

    public static final String DATABASE_PLAYER_EXP          = "Experience";
    public static final String DATABASE_PLAYER_WINS         = "Wins";
    public static final String DATABASE_PLAYER_KILLS        = "Kills";
    public static final String DATABASE_PLAYER_CHEST_FOUND  = "Chests";
    public static final String DATABASE_PLAYER_RATING       = "Rating";

    // Skywars categories ids
    public static final int DANCES_ID       = 5;
    public static final int KILL_EFFECTS_ID = 4;
    public static final int KITS_ID         = 3;
    public static final int CAGES_ID        = 2;
    public static final int PRE_DEATH_ID    = 1;
    public static final int PERKS_ID        = 0;

}
