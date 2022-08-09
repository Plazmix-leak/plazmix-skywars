package net.plazmix.skywars.listener;

import com.google.common.base.Joiner;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.plazmix.coreconnector.utility.localization.LocalizationResource;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.util.GameConstants;
import net.plazmix.utility.ChatUtil;
import net.plazmix.utility.NumberUtil;
import net.plazmix.utility.leveling.LevelingUtil;
import net.plazmix.utility.player.PlazmixUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Pattern;

public final class StaticChatListener implements Listener {

    public static final Pattern DETECT_PLAYER_MESSAGE_PATTERN = Pattern.compile("@[A-zА-я0-9_]{4,16}");

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        PlazmixUser plazmixUser = PlazmixUser.of(event.getPlayer());
        String message = event.getMessage();

        event.setCancelled(true);

        if (DETECT_PLAYER_MESSAGE_PATTERN.matcher(message).find()) {
            String announcedPlayerName = message;

            for (String replacement : DETECT_PLAYER_MESSAGE_PATTERN.split(announcedPlayerName)) {
                announcedPlayerName = announcedPlayerName.replace(replacement, "");
            }

            announcedPlayerName = announcedPlayerName.substring(1);

            // Говорим игроку об упоминании
            Player announcedPlayer = Bukkit.getPlayer(announcedPlayerName);
            if (announcedPlayer != null) {

                announcedPlayer.playSound(announcedPlayer.getLocation(), Sound.LEVEL_UP, 1, 1);
                announcedPlayer.sendTitle("", "§7Вы были упомянуты " + plazmixUser.getDisplayName() + " §7в чате");

                // Заменяем цвет ника в сообщении
                event.setMessage(message.replace(("@").concat(announcedPlayerName), ChatColor.GREEN + announcedPlayerName + plazmixUser.getGroup().getSuffix()));

            } else {

                // Если не в сети, то на красный заменяем
                event.setMessage(message.replace(("@").concat(announcedPlayerName), ChatColor.RED + announcedPlayerName + " [Не в сети]" + plazmixUser.getGroup().getSuffix()));
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(this.createHoverChatMessage(PlazmixUser.of(player).localization().getLocalizationResource(), plazmixUser, event.getMessage()));
        }
    }

    private String getPrestigeStar(PlazmixUser plazmixUser) {
        int skywarsLevel = LevelingUtil.getLevel(GameUser.from(plazmixUser.getPlayerId()).getCache().getInt(GameConstants.DATABASE_PLAYER_EXP));
        ChatColor chatColor = ChatColor.GRAY;

        if (skywarsLevel >= 5 && skywarsLevel <= 9) {
            chatColor = ChatColor.WHITE;
        }
        else if (skywarsLevel >= 10 && skywarsLevel <= 14) {
            chatColor = ChatColor.AQUA;
        }
        else if (skywarsLevel >= 15 && skywarsLevel <= 19) {
            chatColor = ChatColor.GOLD;
        }
        else if (skywarsLevel >= 20 && skywarsLevel <= 24) {
            chatColor = ChatColor.LIGHT_PURPLE;
        }
        else if (skywarsLevel >= 25 && skywarsLevel <= 29) {
            chatColor = ChatColor.BLUE;
        }
        else if (skywarsLevel >= 30 && skywarsLevel <= 34) {
            chatColor = ChatColor.DARK_PURPLE;
        }
        else if (skywarsLevel >= 35 && skywarsLevel <= 39) {
            chatColor = ChatColor.DARK_BLUE;
        }
        else if (skywarsLevel >= 40 && skywarsLevel <= 44) {
            chatColor = ChatColor.DARK_GREEN;
        }
        else if (skywarsLevel >= 45 && skywarsLevel <= 49) {
            chatColor = ChatColor.GREEN;
        }
        else if (skywarsLevel >= 50) {
            chatColor = ChatColor.RED;
        }

        return chatColor + ("[").concat(String.valueOf(skywarsLevel)).concat("☆").concat("]").concat(" ");
    }

    private BaseComponent[] createHoverChatMessage(@NonNull LocalizationResource localizationResource, @NonNull PlazmixUser plazmixUser, @NonNull String message) {
        String hoverText = Joiner.on("\n").join(localizationResource.getTextList("CHAT_HOVER"))
                .replace("%player%", plazmixUser.getDisplayName())
                .replace("%level%", NumberUtil.spaced(plazmixUser.getLevel()))
                .replace("%exp%", NumberUtil.spaced(plazmixUser.getExperience()))
                .replace("%max_exp%", NumberUtil.spaced(plazmixUser.getMaxExperience()));

        return ChatUtil.newBuilder(this.getPrestigeStar(plazmixUser) + plazmixUser.getDisplayName() + " §8➥ " + plazmixUser.getGroup().getSuffix() + message)

                .setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + plazmixUser.getName())
                .setHoverEvent(HoverEvent.Action.SHOW_TEXT, ChatColor.translateAlternateColorCodes('&', hoverText))

                .build();
    }

}
