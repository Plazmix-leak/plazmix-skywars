package net.plazmix.skywars.util;

import lombok.*;
import lombok.experimental.FieldDefaults;
import net.plazmix.game.GamePlugin;
import net.plazmix.skywars.loot.SkywarsChestManager;
import net.plazmix.skywars.loot.mode.SkywarsModeLootbox;
import net.plazmix.skywars.loot.mode.impl.SoloInsaneLootbox;
import net.plazmix.skywars.util.item.MagicBoxItem;
import net.plazmix.skywars.util.item.PotionItem;
import net.plazmix.utility.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("all")
@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum SkywarsMode {

    SOLO(ChatColor.GREEN, "Solo", 1) {

        private final SkywarsModeLootbox lootbox = new SoloInsaneLootbox();

        @Override
        public SkywarsModeLootbox getLootbox() {
            return lootbox;
        }
    },

    TEAM(ChatColor.AQUA, "Team", 2) {

        @Override
        public SkywarsModeLootbox getLootbox() {
            return null;
        }
    },

    CRAZY(ChatColor.RED, "Crazy", 1) {

        @Override
        public SkywarsModeLootbox getLootbox() {
            return null;
        }
    },

    RANKED(ChatColor.GOLD, "Ranked", 1) {

        @Override
        public SkywarsModeLootbox getLootbox() {
            return null;
        }
    },
    ;

    public static final SkywarsMode[] VALUES = SkywarsMode.values();

    public static SkywarsMode fromTitle(@NonNull String modeTitle) {
        for (SkywarsMode skywarsMode : VALUES) {

            if (skywarsMode.title.equalsIgnoreCase(modeTitle)) {
                return skywarsMode;
            }
        }

        return null;
    }

    public static SkywarsMode getCurrentMode(@NonNull GamePlugin gamePlugin) {
        return fromTitle(gamePlugin.getService().getServerMode());
    }


    ChatColor color;
    String title;

    int maxPlayersInTeam;

    public abstract SkywarsModeLootbox getLootbox();
}
