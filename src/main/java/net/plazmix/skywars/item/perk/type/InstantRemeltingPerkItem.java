package net.plazmix.skywars.item.perk.type;

import lombok.NonNull;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.parameter.UpgradeGameItemParameter;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.item.perk.PerkItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class InstantRemeltingPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public InstantRemeltingPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Мнгновенная переплавка", new ItemStack(Material.FURNACE));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 1, GameItemPrice.createDefault(5000)));

        setParameter(upgradeParameter);
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();

        GameUser gameUser = GameUser.from(player);
        UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

        if (playerUpgrade == null) {
            return;
        }

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId()) {
            Block block = event.getBlock();
            event.getBlock().getDrops().clear();
            Material dropMaterial = null;
            switch (block.getType()) {
                case IRON_ORE:
                    dropMaterial = Material.IRON_INGOT;
                    break;
                case DIAMOND_ORE:
                    dropMaterial = Material.DIAMOND;
                    break;
                case COAL_ORE:
                    dropMaterial = Material.COAL;
                    break;
                case EMERALD_ORE:
                    dropMaterial = Material.EMERALD;
                    break;
                case GOLD_ORE:
                    dropMaterial = Material.GOLD_ORE;
                    break;
            }
            if(dropMaterial != null) {
                player.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(dropMaterial));
            }
        }
    }
}
