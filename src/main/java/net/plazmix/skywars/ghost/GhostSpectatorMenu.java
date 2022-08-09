package net.plazmix.skywars.ghost;

import lombok.NonNull;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.item.GameItem;
import net.plazmix.game.item.GameItemsCategory;
import net.plazmix.game.user.GameUser;
import net.plazmix.inventory.impl.BasePaginatedInventory;
import net.plazmix.inventory.updater.SimpleInventoryUpdater;
import net.plazmix.skywars.util.GameConstants;
import net.plazmix.utility.ItemUtil;
import net.plazmix.utility.NumberUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public final class GhostSpectatorMenu extends BasePaginatedInventory {

    private final GameItemsCategory kitsCategory;
    private final GameItemsCategory perksCategory;

    public GhostSpectatorMenu(@NonNull Player player) {
        super("Наблюдатель", 5);
        openInventory(player);

        this.kitsCategory = GamePlugin.getInstance().getService().getItemsCategory(GameConstants.KITS_ID);
        this.perksCategory = GamePlugin.getInstance().getService().getItemsCategory(GameConstants.PERKS_ID);
    }

    @Override
    public void drawInventory(Player player) {
        addRowToMarkup(2, 2);
        addRowToMarkup(3, 2);
        addRowToMarkup(4, 2);

        for (GameUser alivePlayer : GamePlugin.getInstance().getService().getAlivePlayers()) {
            EntityDamageEvent lastDamageCause = alivePlayer.getBukkitHandle().getLastDamageCause();

            GameItem kitItem = kitsCategory != null ? alivePlayer.getSelectedItem(kitsCategory) : null;
            GameItem perkItem = perksCategory != null ? alivePlayer.getSelectedItem(perksCategory) : null;

            addClickItemToMarkup(ItemUtil.newBuilder(Material.SKULL_ITEM)
                            .setDurability(3)
                            .setName(alivePlayer.getPlazmixHandle().getDisplayName())

                            .addLore("")
                            .addLore("§8Общая статистика:")
                            .addLore(" §7Жизни: §c" + Math.round(alivePlayer.getBukkitHandle().getHealth()))
                            .addLore(" §7Голод: §b" + alivePlayer.getBukkitHandle().getFoodLevel())
                            .addLore(" §7Уровень: §e" + alivePlayer.getBukkitHandle().getLevel())

                            .addLore("")
                            .addLore("§8Последний урон:")
                            .addLore(" §7Количество: §c" + (lastDamageCause == null ? "N/A" : "-" + Math.round(lastDamageCause.getDamage()) + " HP"))
                            .addLore(" §7Причина: §b" + (lastDamageCause == null ? "§cN/A" : lastDamageCause.getCause().name()))

                            .addLore("")
                            .addLore("§8Игровая информация:")
                            .addLore(" §7Набор: " + (kitItem == null ? "§cN/A" : "§e" + kitItem.getItemName()))
                            .addLore(" §7Перк: " + (perkItem == null ? "§cN/A" : "§e" + perkItem.getItemName()))

                            .addLore(" §7Убийств: §c" + NumberUtil.formattingSpaced(alivePlayer.getCache().getInt(GameConstants.INGAME_PLAYER_KILLS),
                                    "игрок", "игрока", "игроков"))

                            .addLore("")
                            .addLore("§e▸ Нажмите, чтобы телепортироваться!")
                            .build(),

                    (player1, inventoryClickEvent) -> player.teleport(alivePlayer.getBukkitHandle().getLocation()));
        }

        setOriginalItem(5, ItemUtil.newBuilder(Material.SIGN)
                .setName("§aОбщая информация")
                .addLore("§7Всего выживших: §f" + GamePlugin.getInstance().getService().getAlivePlayers().size())
                .build());

        setInventoryUpdater(20, new SimpleInventoryUpdater(player) {

            @Override
            public void applyRunnable(Player player) {
                updateInventory(player);
            }
        });
    }

}
