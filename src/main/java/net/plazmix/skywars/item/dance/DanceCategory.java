package net.plazmix.skywars.item.dance;

import net.plazmix.cosmetics.game.DanceManager;
import net.plazmix.game.item.GameItemsCategory;
import net.plazmix.skywars.util.GameConstants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class DanceCategory
        extends GameItemsCategory {

    public DanceCategory() {
        super(GameConstants.DANCES_ID, 34, "Победные танцы", new ItemStack(Material.NOTE_BLOCK));

        for (DanceManager danceManager : DanceManager.values()) {
            addItem(new DanceItem(danceManager.ordinal(), danceManager));
        }
    }

}
