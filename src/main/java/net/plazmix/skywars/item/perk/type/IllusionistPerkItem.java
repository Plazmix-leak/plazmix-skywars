package net.plazmix.skywars.item.perk.type;

import lombok.NonNull;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.parameter.UpgradeGameItemParameter;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.item.perk.PerkItem;
import net.plazmix.utility.PercentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public class IllusionistPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public IllusionistPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Иллюзионист", new ItemStack(Material.ENDER_PORTAL_FRAME));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 10, GameItemPrice.createDefault(2500)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 15, GameItemPrice.createDefault(5000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(3, 20, GameItemPrice.createDefault(10_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(4, 30, GameItemPrice.createDefault(15_000)));

        setParameter(upgradeParameter);
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {

    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        GameUser gameUser = GameUser.from(player);

        UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

        if (playerUpgrade == null) {
            return;
        }

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId() && PercentUtil.acceptRandomPercent(playerUpgrade.getValue())) {
            player.getInventory().addItem(new ItemStack(event.getBlock().getType()));
        }
    }

}
