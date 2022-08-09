package net.plazmix.skywars.loot.mode;

import net.plazmix.skywars.loot.SkywarsLootUnit;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SkywarsModeLootbox {

    CompletableFuture<List<SkywarsLootUnit>> initExactlyIslandUnits();

    CompletableFuture<List<SkywarsLootUnit>> initProbablyIslandUnits();

    CompletableFuture<List<SkywarsLootUnit>> initExactlyMiddleUnits();

    CompletableFuture<List<SkywarsLootUnit>> initProbablyMiddleUnits();
}
