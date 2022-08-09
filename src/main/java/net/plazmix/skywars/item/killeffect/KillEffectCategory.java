package net.plazmix.skywars.item.killeffect;

import net.plazmix.cosmetics.game.KillEffectManager;
import net.plazmix.game.item.GameItemsCategory;
import net.plazmix.skywars.util.GameConstants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class KillEffectCategory
        extends GameItemsCategory {

    public KillEffectCategory() {
        super(GameConstants.KILL_EFFECTS_ID, 32, "Эффекты при убийстве", new ItemStack(Material.SKULL_ITEM));

        for (KillEffectManager killEffect : KillEffectManager.values()) {
            addItem(new KillEffectItem(killEffect.ordinal(), killEffect));
        }
    }

}
