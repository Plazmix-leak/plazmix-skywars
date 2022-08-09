package net.plazmix.skywars.item.predeath;

import net.plazmix.cosmetics.game.DeathSoundManager;
import net.plazmix.game.item.GameItemsCategory;
import net.plazmix.skywars.util.GameConstants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class PreDeathCategory
        extends GameItemsCategory {

    public PreDeathCategory() {
        super(GameConstants.PRE_DEATH_ID, 30, "Предсмертные крики", new ItemStack(Material.NOTE_BLOCK));

        for (DeathSoundManager deathSound : DeathSoundManager.values()) {
            addItem(new PreDeathItem(deathSound.ordinal(), deathSound));
        }
    }

}
