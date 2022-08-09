package net.plazmix.skywars.state;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.item.GameItem;
import net.plazmix.game.item.GameItemsCategory;
import net.plazmix.game.mysql.GameMysqlDatabase;
import net.plazmix.game.setting.GameSetting;
import net.plazmix.game.state.type.StandardEndingState;
import net.plazmix.game.user.GameUser;
import net.plazmix.game.utility.GameSchedulers;
import net.plazmix.game.utility.hotbar.GameHotbar;
import net.plazmix.game.utility.hotbar.GameHotbarBuilder;
import net.plazmix.game.utility.worldreset.GameWorldReset;
import net.plazmix.listener.PlayerListener;
import net.plazmix.protocollib.team.ProtocolTeam;
import net.plazmix.skywars.database.DeathAngelMysqlDatabase;
import net.plazmix.skywars.database.SkywarsStatsMysqlDatabase;
import net.plazmix.skywars.scoreboard.EndingScoreboard;
import net.plazmix.skywars.util.GameConstants;
import net.plazmix.skywars.util.deathangel.SkywarsDamnService;
import net.plazmix.utility.ItemUtil;
import net.plazmix.utility.NumberUtil;
import net.plazmix.utility.location.LocationUtil;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class EndingState extends StandardEndingState {

    private final GameHotbar gameHotbar = GameHotbarBuilder.newBuilder()
            .setMoveItems(false)
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


    public EndingState(GamePlugin plugin) {
        super(plugin, "Перезагрузка");

        GameSetting.INTERACT_BLOCK.set(plugin.getService(), false);
    }

    @SneakyThrows
    private void initProtocolTags() {
        ProtocolTeam.removeAll();

        // Getting method PlayerListener#getPlayerTeam()
        PlayerListener playerListener = new PlayerListener();

        Method method = playerListener.getClass().getDeclaredMethod("getPlayerTeam", Player.class);
        method.setAccessible(true);

        // Init new players tags.
        for (Player player : Bukkit.getOnlinePlayers()) {
            ProtocolTeam playerTag = ((ProtocolTeam) method.invoke(playerListener, player));

            playerTag.addPlayerEntry(player);
            playerTag.broadcastToServer();
        }
    }

    @Override
    protected String getWinnerPlayerName() {
        return plugin.getCache().getString(GameConstants.INGAME_WINNER_TEAM);
    }

    @Override
    protected void handleStart() {
        GameWorldReset.resetAllWorlds();
        List<GameUser> winnerUsersList = Arrays.stream(getWinnerPlayerName().split(" & ")).map(GameUser::from).collect(Collectors.toList());

        if (winnerUsersList.isEmpty()) {
            plugin.broadcastMessage(ChatColor.RED + "Произошли технические неполадки, из-за чего игра была принудительно остановлена!");

            forceShutdown();
            return;
        }

        this.initProtocolTags();

        // Add player win.
        for (GameUser winnerUser : winnerUsersList) {

            winnerUser.getCache().increment(GameConstants.DATABASE_PLAYER_WINS);
            winnerUser.getCache().add(GameConstants.DATABASE_PLAYER_RATING, 5);
        }

        // Run fireworks spam.
        GameSchedulers.runTimer(0, 20, () -> {

            for (GameUser winnerUser : winnerUsersList) {
                if (winnerUser.getBukkitHandle() == null) {
                    return;
                }

                Firework firework = winnerUser.getBukkitHandle().getWorld().spawn(winnerUser.getBukkitHandle().getLocation(), Firework.class);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();

                fireworkMeta.setPower(1);
                fireworkMeta.addEffect(FireworkEffect.builder()
                        .with(FireworkEffect.Type.STAR)
                        .withColor(Color.RED)
                        .withColor(Color.GREEN)
                        .withColor(Color.WHITE)
                        .build());

                firework.setFireworkMeta(fireworkMeta);
            }
        });

        boolean isDamnGame = SkywarsDamnService.INSTANCE.isActive();

        GameMysqlDatabase statsMysqlDatabase = plugin.getService().getGameDatabase(SkywarsStatsMysqlDatabase.class);
        GameMysqlDatabase deathAngelMysqlDatabase = plugin.getService().getGameDatabase(DeathAngelMysqlDatabase.class);

        for (Player player : Bukkit.getOnlinePlayers()) {
            GameUser gameUser = GameUser.from(player);
            gameUser.setGhost(false);

            if (!winnerUsersList.contains(gameUser)) {
                gameUser.getCache().set(GameConstants.DATABASE_PLAYER_RATING, Math.max(0, gameUser.getCache().getInt(GameConstants.DATABASE_PLAYER_RATING) - 5));
            }

            int ingameKills = gameUser.getCache().getInt(GameConstants.INGAME_PLAYER_KILLS);
            gameUser.getCache().add(GameConstants.DATABASE_PLAYER_KILLS, ingameKills);

            // Add chests founded count cache.
            gameUser.getCache().add(GameConstants.DATABASE_PLAYER_CHEST_FOUND, player.getStatistic(Statistic.CHEST_OPENED));

            // Announcements.
            player.playSound(player.getLocation(), Sound.FIREWORK_LARGE_BLAST2, 1, 0);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 0);

            player.sendMessage(GameConstants.PREFIX + "§aИгра окончена!");

            // Init rewards
            int skywarsCoinsReward = 0;
            int skywarsXpReward = 0;
            int serverXpReward = 0;

            if (winnerUsersList.contains(gameUser)) {
                player.sendTitle("§6§lПОБЕДА", "§fПоздравляем, Вы выиграли этот матч!");

                skywarsCoinsReward += isDamnGame ? 800 : 500;
                skywarsXpReward += isDamnGame ? 20 : 35;
                serverXpReward += isDamnGame ? 250 : 100;

            } else {

                player.sendTitle("§c§lПОРАЖЕНИЕ", "§fВы проиграли!");

                skywarsCoinsReward += 1;
                serverXpReward += 10;
            }

            skywarsCoinsReward += (ingameKills * 75);
            skywarsXpReward += (ingameKills * 3);
            serverXpReward += (ingameKills * 15);

            // Give rewards
            player.sendMessage(" §e+" + NumberUtil.spaced(skywarsCoinsReward) + " Skywars монет");
            player.sendMessage(" §3+" + NumberUtil.spaced(skywarsXpReward) + " Skywars опыта");
            player.sendMessage(" §b+" + NumberUtil.spaced(serverXpReward) + " серверного опыта");

            gameUser.getPlazmixHandle().addCoins(skywarsCoinsReward);
            gameUser.getPlazmixHandle().addExperience(serverXpReward);
            gameUser.getCache().add(GameConstants.DATABASE_PLAYER_EXP, skywarsXpReward);

            // Set hotbar items.
            gameHotbar.setHotbarTo(player);
            GameSchedulers.runLater(10, () -> player.getInventory().setHeldItemSlot(4));

            // Update player data in database.
            statsMysqlDatabase.insert(false, gameUser);
            deathAngelMysqlDatabase.insert(false, gameUser);
        }

        // Use Win-Dances for winners.
        for (GameUser winnerUser : winnerUsersList) {

            GameItemsCategory dancesCategory = plugin.getService().getItemsCategory(GameConstants.DANCES_ID);
            GameItem selectedDance = winnerUser.getSelectedItem(dancesCategory);

            if (selectedDance != null) {
                selectedDance.applyItem(winnerUser);
            }
        }
    }

    @Override
    protected void handleScoreboardSet(@NonNull Player player) {
        new EndingScoreboard(GameUser.from(getWinnerPlayerName()), player);
    }

    @Override
    protected Location getTeleportLocation() {
        return LocationUtil.stringToLocation(plugin.getConfig().getString("wait-lobby-spawn"));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

}
