package net.plazmix.skywars.item.perk.type;

import lombok.NonNull;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.parameter.UpgradeGameItemParameter;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.item.perk.PerkItem;
import net.plazmix.utility.PercentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class EnderMasterPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public EnderMasterPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Мастер Эндера", new ItemStack(Material.ENDER_PEARL));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 25, GameItemPrice.createDefault(2500)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 50, GameItemPrice.createDefault(5000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(3, 75, GameItemPrice.createDefault(15_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(4, 100, GameItemPrice.createDefault(40_000)));

        setParameter(upgradeParameter);
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        GameUser gameUser = GameUser.from(player);
        UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

        if (playerUpgrade == null) {
            return;
        }

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId() && event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && PercentUtil.acceptRandomPercent(playerUpgrade.getValue()) ) {
            player.teleport(event.getFrom());
        }
    }
}
