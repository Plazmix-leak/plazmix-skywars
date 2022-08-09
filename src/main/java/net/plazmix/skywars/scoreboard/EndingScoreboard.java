package net.plazmix.skywars.scoreboard;

import lombok.NonNull;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.user.GameUser;
import net.plazmix.scoreboard.BaseScoreboardBuilder;
import net.plazmix.scoreboard.BaseScoreboardScope;
import net.plazmix.utility.DateUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EndingScoreboard {

    public EndingScoreboard(@NonNull GameUser winnerUser, @NonNull Player player) {
        BaseScoreboardBuilder scoreboardBuilder = BaseScoreboardBuilder.newScoreboardBuilder();

        scoreboardBuilder.scoreboardDisplay("§b§lSKYWARS");
        scoreboardBuilder.scoreboardScope(BaseScoreboardScope.PROTOTYPE);

        scoreboardBuilder.scoreboardLine(10, ChatColor.GRAY + "SkyWars " + DateUtil.formatPattern(DateUtil.DEFAULT_DATE_PATTERN));
        scoreboardBuilder.scoreboardLine(9, "");
        scoreboardBuilder.scoreboardLine(8, "§fПобедитель игры:");
        scoreboardBuilder.scoreboardLine(7, " " + winnerUser.getPlazmixHandle().getDisplayName());
        scoreboardBuilder.scoreboardLine(6, "");
        scoreboardBuilder.scoreboardLine(5, "§fКарта: §a" + GamePlugin.getInstance().getService().getMapName());
        scoreboardBuilder.scoreboardLine(3, "§fСервер: §a" + PlazmixCoreApi.getCurrentServerName());
        scoreboardBuilder.scoreboardLine(2, "");
        scoreboardBuilder.scoreboardLine(1, "§dwww.plazmix.net");

        scoreboardBuilder.build().setScoreboardToPlayer(player);
    }

}
