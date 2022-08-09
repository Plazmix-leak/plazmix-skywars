package net.plazmix.skywars.state;

import com.comphenix.protocol.utility.MinecraftReflection;
import lombok.NonNull;
import net.md_5.bungee.api.ChatMessageType;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.game.GameCache;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.GamePluginService;
import net.plazmix.game.event.GameGhostChangeEvent;
import net.plazmix.game.item.GameItem;
import net.plazmix.game.item.GameItemsCategory;
import net.plazmix.game.mysql.GameMysqlDatabase;
import net.plazmix.game.setting.GameSetting;
import net.plazmix.game.state.GameState;
import net.plazmix.game.team.GameTeam;
import net.plazmix.game.user.GameUser;
import net.plazmix.game.utility.GameSchedulers;
import net.plazmix.game.utility.hotbar.GameHotbar;
import net.plazmix.game.utility.hotbar.GameHotbarBuilder;
import net.plazmix.protocollib.team.ProtocolTeam;
import net.plazmix.skywars.database.DeathAngelMysqlDatabase;
import net.plazmix.skywars.database.SkywarsStatsMysqlDatabase;
import net.plazmix.skywars.ghost.GhostSpectatorMenu;
import net.plazmix.skywars.scoreboard.GhostScoreboard;
import net.plazmix.skywars.scoreboard.IngameScoreboard;
import net.plazmix.skywars.util.Actionbar;
import net.plazmix.skywars.util.GameConstants;
import net.plazmix.skywars.util.SkywarsEvent;
import net.plazmix.skywars.util.SkywarsMode;
import net.plazmix.skywars.util.deathangel.SkywarsDamnService;
import net.plazmix.utility.ItemUtil;
import net.plazmix.utility.PercentUtil;
import net.plazmix.utility.PlayerUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public final class IngameState extends GameState {

    private final GameHotbar ghostsHotbar = GameHotbarBuilder.newBuilder()
            .setMoveItems(false)
            .addItem(1, ItemUtil.newBuilder(Material.COMPASS)
                            .setName("§aНаблюдатель §7(ПКМ)")
                            .build(),

                    GhostSpectatorMenu::new)

            .addItem(5, ItemUtil.newBuilder(Material.PAPER)
                            .setName("§aСыграть еще раз §7(ПКМ)")
                            .build(),

                    player -> GamePlugin.getInstance().getService().playAgain(player))

            .addItem(9, ItemUtil.newBuilder(Material.SKULL_ITEM)
                            .setDurability(3)
                            .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjhkNmFkNjkyN2NkYWE5ZDgxNzkzMzdjYWRmOTY0NDYwNjcyMTE3YjMyNmU2MGY1YjFkMTlhNGI1NGYyYTMyMyJ9fX0=")
                            .setName("§aПокинуть арену §7(ПКМ)")
                            .build(),

                    PlazmixCoreApi::redirectToLobby)
            .build();

    public IngameState(@NonNull GamePlugin plugin) {
        super(plugin, "Идет игра", false);
    }

    private void cleanupGameUsers() {
        GamePluginService service = super.getPlugin().getService();

        new HashSet<>(service.getGameUsers().keySet())
                .forEach(playerName -> {

            if (Bukkit.getPlayer(playerName) == null) {
                service.getGameUsers().remove(playerName.toLowerCase());
            }
        });
    }

    private void initPlayersTeams() {
        // Init teams.
        int teamMembersLength = SkywarsMode.getCurrentMode(plugin).getMaxPlayersInTeam();
        int teamsLength = Bukkit.getOnlinePlayers().size() / teamMembersLength;

        for (int index = 0; index < teamsLength; index++) {
            plugin.getService().registerTeam(new GameTeam(index, ChatColor.WHITE, String.valueOf(index)));
        }

        plugin.getService().throwPlayersToTeams(teamMembersLength, plugin.getService().getLoadedTeams());
    }

    private void initProtocolTags() {

        // Create enemy tag displayable.
        ProtocolTeam enemiesTag = ProtocolTeam.get("enemies");

        enemiesTag.setPrefix(ChatColor.RED.toString());
        enemiesTag.addAutoReceived();

        for (Player player : Bukkit.getOnlinePlayers()) {
            enemiesTag.addPlayerEntry(player);
        }

        // Create team tag displayable.
        for (GameTeam team : plugin.getService().getLoadedTeams()) {

            ProtocolTeam teamTag = ProtocolTeam.get(("team_") + team.getTeamName());
            teamTag.setPrefix(ChatColor.GREEN.toString());

            team.handleBroadcast(gameUser -> teamTag.addPlayerEntry(gameUser.getBukkitHandle()));
        }
    }

    private void initDamnGame() {
        int summaryPercent = SkywarsDamnService.INSTANCE.summaryPercent(
                Bukkit.getOnlinePlayers().stream().map(GameUser::from).collect(Collectors.toList())
        );

        boolean canDamnActive = PercentUtil.acceptRandomPercent(summaryPercent);

        if (SkywarsMode.getCurrentMode(super.getPlugin()) != SkywarsMode.RANKED) {
            SkywarsDamnService.INSTANCE.setActive(canDamnActive);
        }
    }

    private void initSkywarsEvents() {
        SkywarsEvent.REFILL_CHESTS.fireEvent(this);

        SkywarsEvent.setOnUpdate((previousEvent, newEvent) -> {
            previousEvent.fireEvent(this);

            for (Player player : plugin.getServer().getOnlinePlayers()) {

                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 2);
                player.sendTitle("§3§lНОВОЕ СОБЫТИЕ!", previousEvent.getTitle());
            }
        });
    }

    private void initGuardOnFirstTimeTask() {
        GameSchedulers.runLater(20 * 5, () -> {
            Location arenaCenter = plugin.getService().getMapWorld().getSpawnLocation();

            for (GameUser gameUser : plugin.getService().getAlivePlayers()) {
                Player bukkit = gameUser.getBukkitHandle();

                // Send Titles.
                bukkit.playSound(gameUser.getBukkitHandle().getLocation(), Sound.EXPLODE, 1, 0);
                bukkit.playSound(gameUser.getBukkitHandle().getLocation(), Sound.LEVEL_UP, 1, 0);

                if (SkywarsDamnService.INSTANCE.isActive()) {
                    bukkit.sendTitle("§c§lSKYWARS §c(Проклятая игра)", "§fИгра началась, лутайте сундуки и нападайте!");

                    if (arenaCenter != null) {
                        arenaCenter.getWorld().strikeLightning(arenaCenter);
                    }
                }
                else {
                    bukkit.sendTitle("§d§lSKYWARS", "§fИгра началась, лутайте сундуки и нападайте!");
                }

                // Remove cage blocks.
                GameItem cage = gameUser.getCache().get("Cage");

                if (cage != null) {
                    cage.cancelItem(gameUser);
                }
            }

            // Set new ingame settings values.
            GameSetting.BLOCK_BREAK.set(plugin.getService(), true);
            GameSetting.BLOCK_PLACE.set(plugin.getService(), true);
            GameSetting.BLOCK_PHYSICS.set(plugin.getService(), true);
            GameSetting.BLOCK_BURN.set(plugin.getService(), true);

            GameSetting.PLAYER_DAMAGE.set(plugin.getService(), true);
            GameSetting.PLAYER_DROP_ITEM.set(plugin.getService(), true);
            GameSetting.PLAYER_PICKUP_ITEM.set(plugin.getService(), true);

            GameSetting.ENTITY_EXPLODE.set(plugin.getService(), true);

            GameSetting.INTERACT_BLOCK.set(plugin.getService(), true);
            GameSetting.INTERACT_ITEM.set(plugin.getService(), true);
        });
    }

    private void resetPlayerSettings(boolean isScoreboardUpdate, Player player) {
        if (isScoreboardUpdate) {
            new IngameScoreboard(player);
        }

        player.updateInventory();

        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().clear();

        player.setFlying(false);
        player.setAllowFlight(false);

        player.setGameMode(GameMode.SURVIVAL);

        player.setLevel(0);
        player.setExp(0);

        player.getInventory().setHeldItemSlot(0);

        player.setVelocity(player.getVelocity());

        try {
            MinecraftReflection.getCraftPlayerClass().getMethod("updateScaledHealth").invoke(player);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ignored) {
        }
    }

    private void playKillEffect(GameUser gameUser, GameUser killerUser) {
        GameItemsCategory killEffectsCategory = plugin.getService().getItemsCategory(GameConstants.KILL_EFFECTS_ID);
        GameItem selectedKillEffect = killerUser.getSelectedItem(killEffectsCategory);

        if (selectedKillEffect != null) {
            selectedKillEffect.applyItem(gameUser);
        }
    }

    private void playDeathSound(GameUser gameUser) {
        GameItemsCategory deathSoundCategory = plugin.getService().getItemsCategory(GameConstants.PRE_DEATH_ID);
        GameItem selectedDeathSound = gameUser.getSelectedItem(deathSoundCategory);

        if (selectedDeathSound != null) {
            selectedDeathSound.applyItem(gameUser);
        }
    }

    private void givePlayerKit(GameUser gameUser) {
        GameItemsCategory kitsCategory = plugin.getService().getItemsCategory(GameConstants.KITS_ID);
        GameItem selectedKit = gameUser.getSelectedItem(kitsCategory);

        if (selectedKit != null) {
            selectedKit.applyItem(gameUser);
        }
    }

    private void createPlayerCage(GameUser gameUser) {
        GameItemsCategory cagesCategory = plugin.getService().getItemsCategory(GameConstants.CAGES_ID);
        GameItem selectedCage = gameUser.getSelectedItem(cagesCategory);

        if (selectedCage == null) {
            (selectedCage = cagesCategory.getItem(0)).applyItem(gameUser);

        } else {

            selectedCage.applyItem(gameUser);
        }

        gameUser.getCache().set("Cage", selectedCage);
    }

    private void initPlayerSpawnpoint(List<Location> playersSpawnsList, GameCache userCache) {
        Location islandLocation = playersSpawnsList.get((int) (Math.random() * playersSpawnsList.size()));

        userCache.set(GameConstants.INGAME_PLAYER_ISLAND_LOC, islandLocation.clone().add(0, 1, 0));
        playersSpawnsList.remove(islandLocation);
    }

    private void teleportToIslandSpawn(Player player) {
        Location teleportLocation = GameUser.from(player).getCache().getLocation(GameConstants.INGAME_PLAYER_ISLAND_LOC);
        if (teleportLocation == null) {
            teleportLocation = plugin.getService().getMapWorld().getSpawnLocation();
        }

        Vector vector = plugin.getService().getMapWorld().getSpawnLocation().clone()
                .subtract(teleportLocation)
                .toVector()
                .normalize();

        teleportLocation.setDirection(vector);

        teleportLocation.setYaw(teleportLocation.getYaw());
        teleportLocation.setPitch(teleportLocation.getPitch());

        player.teleport(teleportLocation.clone().add(-0.5, 0, -0.5));
    }

    private void sendPlayerVisualOnStart(Player player) {
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 0);

        if (SkywarsDamnService.INSTANCE.isActive()) {
            player.sendTitle("§c§lSKYWARS §c(Проклятая игра)", "§fВоздушная битва не §aна жизнь§f, а §сна смерть");
        }
        else {
            player.sendTitle("§d§lSKYWARS", "§fВоздушная битва не §aна жизнь§f, а §cна смерть");
        }
    }

    private void removeAllLivingEntities() {
        for (World world : plugin.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {

                if (entity instanceof Player) {
                    continue;
                }

                entity.remove();
            }
        }
    }

    private boolean findAndSetWinner(Player applicant) {
        List<GameTeam> aliveTeamsList = super.getPlugin().getService().getAlivePlayers()
                .stream()
                .map(GameUser::getCurrentTeam)
                .filter(Objects::nonNull)
                .filter(team -> team.getPlayersCount() > 0)
                .collect(Collectors.toList());

        if (aliveTeamsList.size() != 1) {

            if (super.getPlugin().getService().getAlivePlayers().size() <= 1) {
                plugin.broadcastMessage("§c§lPlazmix §8:: §cВозникла техническая ошибка, из-за которой был прерван игровой процесс!");
                return true;
            }

            return false;
        }

        GameTeam winnerTeam = (applicant != null)
                ? GameUser.from(applicant).getCurrentTeam()
                : aliveTeamsList.stream().findFirst().orElse(null);

        String winnerPlayersString = winnerTeam.getPlayers().stream().map(GameUser::getName).collect(Collectors.joining(" & "));

        if (!winnerPlayersString.isEmpty()) {
            plugin.getCache().set(GameConstants.INGAME_WINNER_TEAM, winnerPlayersString);
            return true;
        }

        return false;
    }

    private void setGhost(Player death, Player killer) {
        GameUser deathGameUser = GameUser.from(death);
        deathGameUser.setGhost(true);

        if (killer != null) {
            death.teleport(killer.getLocation());
        }
        else {
            this.teleportToIslandSpawn(death);
        }

        if (this.findAndSetWinner(killer)) {
            super.nextStage();
        }
    }

    @Override
    protected void onStart() {
        this.cleanupGameUsers();

        GameSetting.setAll(plugin.getService(), false);
        GameSetting.CREATURE_SPAWN_CUSTOM.set(plugin.getService(), true);

        List<Location> playersSpawnsList = plugin.getCache().getList(GameConstants.ISLANDS_SPAWNS_CACHE, Location.class);

        // Initialize ingame parameters.
        this.initPlayersTeams();
        this.initProtocolTags();

        // Check damn game available by summary percents.
        this.initDamnGame();

        // Remove all entities.
        this.removeAllLivingEntities();

        // Init players.
        for (Player player : Bukkit.getOnlinePlayers()) {
            GameUser gameUser = GameUser.from(player);

            // Generate & fill island chests.
            this.initPlayerSpawnpoint(playersSpawnsList, gameUser.getCache());

            // Applying player settings.
            this.resetPlayerSettings(true, player);

            // Give a kit.
            this.givePlayerKit(gameUser);

            // Create a cage.
            this.createPlayerCage(gameUser);

            // Teleport player to self island.
            this.teleportToIslandSpawn(player);

            // Send player visual effects & messages.
            this.sendPlayerVisualOnStart(player);
        }

        // Init skywars events.
        this.initSkywarsEvents();

        // Add cages guard timeout task.
        this.initGuardOnFirstTimeTask();
    }

    @Override
    protected void onShutdown() {
        SkywarsEvent.setOnUpdate(null);

        Bukkit.getOnlinePlayers().forEach(bukkit -> this.resetPlayerSettings(false, bukkit));

        this.removeAllLivingEntities();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        this.setGhost(event.getPlayer(), null);

        // Update user game-mysql-database data.
        GameUser gameUser = GameUser.from(event.getPlayer());

        GameMysqlDatabase statsMysqlDatabase = plugin.getService().getGameDatabase(SkywarsStatsMysqlDatabase.class);
        GameMysqlDatabase deathAngelMysqlDatabase = plugin.getService().getGameDatabase(DeathAngelMysqlDatabase.class);

        statsMysqlDatabase.insert(false, gameUser);
        deathAngelMysqlDatabase.insert(false, gameUser);
    }

    @EventHandler
    public void onGhostJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (GameUser.from(player).isGhost()) {
            new GhostScoreboard(player);
        }
        else {
            new IngameScoreboard(player);
        }
    }

    @EventHandler
    public void onFirstDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        GameUser gameUser = GameUser.from(event.getEntity().getName());
        GameCache gameCache = gameUser.getCache();

        if (!gameCache.contains("firstDamage")) {
            gameCache.set("firstDamage", 1);

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFall(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        if (event.getTo().getY() < 10) {
            this.setGhost(player, null);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        Player death = event.getEntity();
        Player killer = death.getKiller();

        this.resetPlayerSettings(false, death);
        death.getWorld().strikeLightning(death.getLocation());

        // If the player instanceof ghost then teleport to the spawnpoint of map.
        GameUser deathGameUser = GameUser.from(death);

        if (deathGameUser.isGhost()) {
            death.teleport(plugin.getService().getMapWorld().getSpawnLocation());
            return;
        }

        this.setGhost(death, killer);
        this.playDeathSound(deathGameUser);

        // Check the player killer.
        if (killer == null) {
            plugin.broadcastMessage(ChatMessageType.ACTION_BAR, PlayerUtil.getDisplayName(death) + " §fвыпал из мира");
        }
        else {
            GameUser killerUser = GameUser.from(killer);
            killer.playSound(killer.getLocation(), Sound.NOTE_PLING, 1, 1);

            plugin.broadcastMessage(ChatMessageType.ACTION_BAR, PlayerUtil.getDisplayName(death) + " §fбыл убит " + PlayerUtil.getDisplayName(killer));

            killerUser.getCache().increment(GameConstants.INGAME_PLAYER_KILLS);
            this.playKillEffect(deathGameUser, killerUser);

            if (SkywarsDamnService.INSTANCE.isActive()) {
                Actionbar.sendMessage(killer, "§3+2 Skywars опыта  §b+3 души");
            }
            else {
                Actionbar.sendMessage(killer, "§3+1 Skywars опыта  §b+1 душа");
            }
        }
    }

    @EventHandler
    public void onGhostStatus(GameGhostChangeEvent event) {
        Player player = event.getGameUser().getBukkitHandle();
        player.setFlying(event.isGhost());

        if (event.isGhost()) {
            ghostsHotbar.setHotbarTo(player);

            new GhostScoreboard(player);
        }
    }
    
}
