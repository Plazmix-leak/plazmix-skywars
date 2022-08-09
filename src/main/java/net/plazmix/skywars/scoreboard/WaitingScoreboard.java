package net.plazmix.skywars.scoreboard;

import lombok.NonNull;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.state.type.StandardWaitingState;
import net.plazmix.scoreboard.BaseScoreboardBuilder;
import net.plazmix.scoreboard.BaseScoreboardScope;
import net.plazmix.utility.DateUtil;
import net.plazmix.utility.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WaitingScoreboard {

    public WaitingScoreboard(@NonNull StandardWaitingState.TimerStatus timerStatus, @NonNull Player player) {
        GamePlugin gamePlugin = GamePlugin.getInstance();
        BaseScoreboardBuilder scoreboardBuilder = BaseScoreboardBuilder.newScoreboardBuilder();

        scoreboardBuilder.scoreboardDisplay("§b§lSKYWARS");
        scoreboardBuilder.scoreboardScope(BaseScoreboardScope.PROTOTYPE);

        scoreboardBuilder.scoreboardLine(11, "SkyWars " + ChatColor.GRAY + DateUtil.formatPattern(DateUtil.DEFAULT_DATE_PATTERN));
        scoreboardBuilder.scoreboardLine(10, "");
        scoreboardBuilder.scoreboardLine(9, "§fКарта: §a" + gamePlugin.getService().getMapName());
        scoreboardBuilder.scoreboardLine(8, "§fИгроки: §a" + Bukkit.getOnlinePlayers().size() + "§f/§c" + gamePlugin.getService().getMaxPlayers());
        scoreboardBuilder.scoreboardLine(7, "");
        scoreboardBuilder.scoreboardLine(6, " §cОжидание игроков...");
        scoreboardBuilder.scoreboardLine(5, "");
        scoreboardBuilder.scoreboardLine(4, "§fРежим: §a" + GamePlugin.getInstance().getService().getServerMode());
        scoreboardBuilder.scoreboardLine(3, "§fСервер: §a" + PlazmixCoreApi.getCurrentServerName());
        scoreboardBuilder.scoreboardLine(2, "");
        scoreboardBuilder.scoreboardLine(1, "§dwww.plazmix.net");

        scoreboardBuilder.scoreboardUpdater((baseScoreboard, player1) -> {
            baseScoreboard.updateScoreboardLine(8, player, "§fИгроки: §a" + Bukkit.getOnlinePlayers().size() + "§f/§c" + gamePlugin.getService().getMaxPlayers());
            baseScoreboard.updateScoreboardLine(6, player, (!timerStatus.isLived() ? "§cОжидание игроков..." : "§fИгра начнется через §e" + NumberUtil.formattingSpaced(timerStatus.getLeftSeconds(), "§fсекунду", "§fсекунды", "§fсекунд")));

        }, 20);

        scoreboardBuilder.build().setScoreboardToPlayer(player);
    }

}
