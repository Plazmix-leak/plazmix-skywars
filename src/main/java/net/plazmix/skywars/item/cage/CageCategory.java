package net.plazmix.skywars.item.cage;

import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.GameItemsCategory;
import net.plazmix.skywars.util.GameConstants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class CageCategory extends GameItemsCategory {

    public CageCategory() {
        super(GameConstants.CAGES_ID, 16, "Клетки", new ItemStack(Material.BUCKET));

        addItem(new CageItem(0, GameItemPrice.createDefault(0), "Стеклянная клетка", new ItemStack(Material.GLASS)));
        addItem(new CageItem(1, GameItemPrice.createDefault(20_000), "Невидимая клетка", new ItemStack(Material.BARRIER)));
        addItem(new CageItem(2, GameItemPrice.createDefault(15_000), "Ледяная клетка", new ItemStack(Material.PACKED_ICE)));
        addItem(new CageItem(3, GameItemPrice.createDefault(15_000), "Слаймовая клетка", new ItemStack(Material.SLIME_BLOCK)));
        addItem(new CageItem(4, GameItemPrice.createDefault(15_000), "Белая клетка", new ItemStack(Material.STAINED_GLASS)));
        addItem(new CageItem(5, GameItemPrice.createDefault(15_000), "Железная клетка", new ItemStack(Material.IRON_BARDING)));
    }

}
