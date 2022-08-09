package net.plazmix.skywars.scoreboard;

import lombok.NonNull;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.user.GameUser;
import net.plazmix.scoreboard.BaseScoreboardBuilder;
import net.plazmix.scoreboard.BaseScoreboardScope;
import net.plazmix.scoreboard.animation.ScoreboardDisplayFlickAnimation;
import net.plazmix.skywars.util.GameConstants;
import net.plazmix.skywars.util.SkywarsEvent;
import net.plazmix.utility.DateUtil;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

public class GhostScoreboard {

    private static final ScoreboardDisplayFlickAnimation DISPLAY_ANIMATION = new ScoreboardDisplayFlickAnimation()
    {{
        addColor(ChatColor.WHITE);
        addColor(ChatColor.AQUA);
        addColor(ChatColor.LIGHT_PURPLE);
        addColor(ChatColor.AQUA);

        addTextToAnimation("§lSKYWARS");
    }};

    public GhostScoreboard(@NonNull Player player) {
        BaseScoreboardBuilder scoreboardBuilder = BaseScoreboardBuilder.newScoreboardBuilder();

        scoreboardBuilder.scoreboardDisplay(DISPLAY_ANIMATION);
        scoreboardBuilder.scoreboardScope(BaseScoreboardScope.PROTOTYPE);

        scoreboardBuilder.scoreboardLine(ChatColor.GRAY + "SkyWars " + DateUtil.formatPattern(DateUtil.DEFAULT_DATE_PATTERN),
                "",
                "§fСледующее событие:",
                " §cN/A (00:00)",
                "",
                "§fНаблюдателей: §6N/A",
                "§fВыживших: §aN/A",
                "",
                "§fКарта: §a" + GamePlugin.getInstance().getService().getMapName(),
                "",
                "§dwww.plazmix.net");

        scoreboardBuilder.scoreboardUpdater((baseScoreboard, player1) -> {

            baseScoreboard.updateScoreboardLine(6, player, "§fНаблюдателей: §6" + GamePlugin.getInstance().getService().getGhostPlayers().size());
            baseScoreboard.updateScoreboardLine(5, player, "§fВыживших: §a" + GamePlugin.getInstance().getService().getAlivePlayers().size());

            // Update skywars events.
            {
                SkywarsEvent currentEvent = SkywarsEvent.nextEvent();
                SkywarsEvent.EventTime eventTime = currentEvent.getEventTime();

                long timeOfEventAction = eventTime.getUnit().toMillis(eventTime.getDelay()) -
                        (System.currentTimeMillis() - SkywarsEvent.getLastEventUpdateTimeMillis());

                baseScoreboard.updateScoreboardLine(8, player, " §e" + currentEvent.getTitle() + " (" + parseClockedTime(timeOfEventAction) + ")");
            }

        }, 20);

        scoreboardBuilder.build().setScoreboardToPlayer(player);
    }


    private static final NumberFormat CLOCK_NUMBER_FORMAT = new DecimalFormat("00");

    private String parseClockedTime(long currentMillis) {
        long time = currentMillis;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        time -= TimeUnit.MINUTES.toMillis(minutes);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);

        return CLOCK_NUMBER_FORMAT.format(minutes) + ":" + CLOCK_NUMBER_FORMAT.format(seconds);
    }
}
