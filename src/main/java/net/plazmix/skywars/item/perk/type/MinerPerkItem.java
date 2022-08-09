package net.plazmix.skywars.item.perk.type;

import lombok.NonNull;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.parameter.UpgradeGameItemParameter;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.item.perk.PerkItem;
import net.plazmix.utility.PercentUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MinerPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public MinerPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Шахтёр", new ItemStack(Material.IRON_PICKAXE));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 25, GameItemPrice.createDefault(2500)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 50, GameItemPrice.createDefault(5000)));

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

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId() && PercentUtil.acceptRandomPercent(playerUpgrade.getValue())) {
            event.getBlock().getDrops().addAll(event.getBlock().getDrops());
        }
    }
}
