package net.plazmix.skywars.util;

import lombok.*;
import lombok.experimental.FieldDefaults;
import net.plazmix.game.GamePlugin;
import net.plazmix.skywars.loot.SkywarsChestManager;
import net.plazmix.skywars.loot.SkywarsLootEngine;
import net.plazmix.skywars.state.IngameState;
import net.plazmix.utility.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.TNTPrimed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum SkywarsEvent {

    // Refilling all chests on the map.
    REFILL_CHESTS("Обновление лута", EventTime.create(true, 30, TimeUnit.SECONDS),
            (gamePlugin, ingameState) -> {

        List<Location> middleChestsLocations = gamePlugin.getCache().getList(GameConstants.MIDDLE_CHESTS_CACHE, Location.class);

        SkywarsMode skywarsMode = SkywarsMode.getCurrentMode(GamePlugin.getInstance());
        SkywarsLootEngine skywarsLootEngine = SkywarsLootEngine.getInstance();

        // Fill an island chests.
        for (int i = 0; i < 16; i ++) {
            List<Location> islandChestsLocations = gamePlugin.getCache().getList(GameConstants.ISLAND_CHESTS_CACHE + i, Location.class);
            if (islandChestsLocations == null) {
                continue;
            }

            SkywarsChestManager chestManager = skywarsLootEngine.createChestManager(
                    islandChestsLocations.stream().map(Location::getBlock).filter(block -> block.getType().name().contains("CHEST")).map(block -> (Chest) block.getState()).collect(Collectors.toList())
            );

            skywarsLootEngine.fillChestManager(chestManager,
                    skywarsMode.getLootbox().initExactlyIslandUnits().join(),
                    skywarsMode.getLootbox().initProbablyIslandUnits().join());

            chestManager.fill();
        }

        // Fill a middle chests.
        for (List<Location> middleChests : SkywarsHelper.splitLocations(2, middleChestsLocations)) {
            SkywarsChestManager chestManager = skywarsLootEngine.createChestManager(
                    middleChests.stream().map(Location::getBlock).filter(block -> block.getType().name().contains("CHEST")).map(block -> (Chest) block.getState()).collect(Collectors.toList())
            );

            skywarsLootEngine.fillChestManager(chestManager,
                    skywarsMode.getLootbox().initExactlyMiddleUnits().join(),
                    skywarsMode.getLootbox().initProbablyMiddleUnits().join());

            chestManager.fill();
        }
    }),

    // Spawning a five ender-dragons on the center map location.
    DRAGONS_SPAWN("Конец света", EventTime.create(false, 30, TimeUnit.SECONDS),
            (gamePlugin, ingameState) -> {

        Location spawnLocation = gamePlugin.getService().getMapWorld().getSpawnLocation()
                .clone().add(0, 50, 0);

        for (int i = 0; i < 5; i++) {
            spawnLocation.getWorld().spawn(spawnLocation, EnderDragon.class);
        }
    }),

    // Spawning falling primed tnt filled the map.
    FALLING_TNT("Ядерные бомбы", EventTime.create(true, 30, TimeUnit.SECONDS),
            (gamePlugin, ingameState) -> {

        int spawnRadius = gamePlugin.getConfig().getInt("map-radius");
        Location originSpawnLoc = gamePlugin.getService().getMapWorld().getSpawnLocation()
                .clone()
                .add(0, 50, 0);

        for (int i = 0; i < NumberUtil.randomInt(200, 300); i++) {
            Location tntSpawnLocation = originSpawnLoc.clone().add(
                    NumberUtil.randomInt(-spawnRadius, spawnRadius), 0, NumberUtil.randomInt(-spawnRadius, spawnRadius)
            );

            tntSpawnLocation.getWorld().spawn(tntSpawnLocation, TNTPrimed.class);
        }
    }),

    // Spawning a five ender-dragons on the center map location.
    RESIZE_WORLD_BORDER("Сокращение мира", EventTime.create(false, 30, TimeUnit.SECONDS),
            (gamePlugin, ingameState) -> {

        Location spawnLocation = gamePlugin.getService().getMapWorld().getSpawnLocation();

        long resizeDelay = TimeUnit.MINUTES.toSeconds(2);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "worldborder center " + spawnLocation.getX() + " " + spawnLocation.getZ());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "worldborder set " + gamePlugin.getConfig().getInt("map-radius"));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "worldborder set 50 " + resizeDelay);
    }),
    ;

    public static final SkywarsEvent[] SKYWARS_EVENT_VALUES = SkywarsEvent.values();
    private static final List<Integer> previousEvents = new ArrayList<>();

    @Setter
    private static BiConsumer<SkywarsEvent, SkywarsEvent> onUpdate;

    @Getter
    private static long lastEventUpdateTimeMillis = System.currentTimeMillis();
    @Getter
    private static SkywarsEvent currentEvent = SkywarsEvent.REFILL_CHESTS;

    public static SkywarsEvent nextEvent() {
        if (currentEvent != null && System.currentTimeMillis() - lastEventUpdateTimeMillis < currentEvent.eventTime.unit.toMillis(currentEvent.eventTime.delay)) {
            return currentEvent;
        }

        SkywarsEvent previousEvent = currentEvent;
        SkywarsEvent nextEvent = SKYWARS_EVENT_VALUES[NumberUtil.randomInt(0, SKYWARS_EVENT_VALUES.length)];

        if (!nextEvent.eventTime.isCanReplied() && previousEvents.contains(nextEvent.ordinal())) {
            return nextEvent();
        }

        currentEvent = nextEvent;
        lastEventUpdateTimeMillis = System.currentTimeMillis();

        if (onUpdate != null) {
            onUpdate.accept(previousEvent, currentEvent);
        }

        previousEvents.add(currentEvent.ordinal());
        return currentEvent;
    }

    String title;

    EventTime eventTime;
    BiConsumer<GamePlugin, IngameState> eventAction;

    public void fireEvent(IngameState ingameState) {

        if (eventAction != null) {
            eventAction.accept(ingameState.getPlugin(), ingameState);
        }
    }

    @Getter
    @Value(staticConstructor = "create")
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class EventTime {

        boolean canReplied;

        long delay;
        TimeUnit unit;
    }
}
