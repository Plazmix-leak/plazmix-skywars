package net.plazmix.skywars.item.perk;

import lombok.NonNull;
import net.plazmix.game.item.GameItem;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.user.GameUser;
import org.bukkit.inventory.ItemStack;

public abstract class PerkItem extends GameItem {

    public PerkItem(int id, @NonNull GameItemPrice price, @NonNull String itemName, @NonNull ItemStack iconItem) {
        super(id, price, itemName, iconItem);
    }

    @Override
    protected void onCancel(@NonNull GameUser gameUser) {
        // nothing.
    }

}
