package net.plazmix.skywars.item.perk;

import net.plazmix.game.item.GameItemsCategory;
import net.plazmix.skywars.item.perk.type.*;
import net.plazmix.skywars.util.GameConstants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class PerkCategory extends GameItemsCategory {

    public PerkCategory() {
        super(GameConstants.PERKS_ID, 12, "Перки", new ItemStack(Material.BLAZE_POWDER));

        addItem(new AdrenalinePerkItem(0));
        addItem(new ArcherPerkItem(1));
        addItem(new BeekeeperPerkItem(2));
        addItem(new BulldozerPerkItem(3));
        addItem(new EnderMasterPerkItem(4));
        addItem(new FatPerkItem(5));
        addItem(new FireArrowPerkItem(6));
        addItem(new HeightMasterPerkItem(7));
        addItem(new IllusionistPerkItem(8));
        addItem(new InstantRemeltingPerkItem(9));
        addItem(new JuggernautPerkItem(10));
        addItem(new KnightPerkItem(11));
        addItem(new LuckCharmPerkItem(12));
        addItem(new MinerPerkItem(13));
        addItem(new RatMasterPerkItem(14));
        addItem(new TrickPerkItem(15));
        addItem(new WeaponMasterPerkItem(16));
    }

}
