package net.plazmix.skywars;

import lombok.NonNull;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.installer.GameInstallerTask;
import net.plazmix.skywars.util.GameConstants;
import net.plazmix.skywars.util.SkywarsMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public final class PlazmixSkywarsInstaller extends GameInstallerTask {

    @NonNull
    SkywarsMode skywarsMode;

    public PlazmixSkywarsInstaller(@NonNull SkywarsMode skywarsMode, @NonNull GamePlugin plugin) {
        super(plugin);

        this.skywarsMode = skywarsMode;
    }

    @Override
    protected void handleExecute(@NonNull Actions actions, @NonNull Settings settings) {
        settings.setCenter(plugin.getService().getMapWorld().getSpawnLocation());
        settings.setRadius(plugin.getConfig().getInt("map-radius"));

        settings.setUseOnlyTileBlocks(true);

        actions.addEntity(EntityType.ARMOR_STAND, entity -> {

            // Remove ArmorStand entity.
            entity.remove();

            // Remove Beacon block.
            Block beacon = entity.getLocation().clone().subtract(0, 1, 0).getBlock();
            beacon.setType(Material.AIR);

            // Add locations to cache.
            List<Location> islandsSpawnsList = plugin.getCache().getOrDefault(GameConstants.ISLANDS_SPAWNS_CACHE, ArrayList::new);
            islandsSpawnsList.add(beacon.getLocation());

            plugin.getCache().set(GameConstants.ISLANDS_COUNT_CACHE, islandsSpawnsList.size());
            plugin.getCache().set(GameConstants.ISLANDS_SPAWNS_CACHE, islandsSpawnsList);

            // Service max players settings update.
            plugin.getService().setMaxPlayers(skywarsMode.getMaxPlayersInTeam() * plugin.getCache().getInt(GameConstants.ISLANDS_COUNT_CACHE));
        });

        actions.addBlock(Material.CHEST, block -> {

            int durability = block.getRelative(BlockFace.UP).getData();
            block.getRelative(BlockFace.UP).setType(Material.AIR);

            List<Location> islandChestsLocationList = plugin.getCache().getOrDefault(GameConstants.ISLAND_CHESTS_CACHE + durability, ArrayList::new);
            islandChestsLocationList.add(block.getLocation());

            plugin.getCache().set(GameConstants.ISLAND_CHESTS_CACHE + durability, islandChestsLocationList);
        });

        actions.addBlock(Material.TRAPPED_CHEST, block -> {

            List<Location> middleChestsLocationList = plugin.getCache().getOrDefault(GameConstants.MIDDLE_CHESTS_CACHE, ArrayList::new);
            middleChestsLocationList.add(block.getLocation());

            plugin.getCache().set(GameConstants.MIDDLE_CHESTS_CACHE, middleChestsLocationList);
        });
    }
    
}
