package net.plazmix.skywars.item.kit;

import net.plazmix.game.item.GameItemsCategory;
import net.plazmix.skywars.util.GameConstants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class KitCategory extends GameItemsCategory {

    public KitCategory() {
        super(GameConstants.KITS_ID, 14, "Наборы", new ItemStack(Material.LEATHER_CHESTPLATE));

        for (KitItem kitItem : KitItem.values()) {
            addItem(kitItem.createGameItem());
        }
    }

}
