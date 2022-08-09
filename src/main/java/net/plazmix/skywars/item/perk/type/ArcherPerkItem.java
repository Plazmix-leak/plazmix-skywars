package net.plazmix.skywars.item.perk.type;

import lombok.NonNull;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.parameter.UpgradeGameItemParameter;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.item.perk.PerkItem;
import net.plazmix.utility.PercentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArcherPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public ArcherPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Лучник", new ItemStack(Material.BOW));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 10, GameItemPrice.createDefault(2500)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 20, GameItemPrice.createDefault(5000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(3, 30, GameItemPrice.createDefault(15_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(4, 40, GameItemPrice.createDefault(40_000)));

        setParameter(upgradeParameter);
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {

    }

    @EventHandler
    public void onDeath(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player) || !(event.getEntity() instanceof Arrow)) {
            return;
        }

        Player player = (Player)event.getEntity().getShooter();
        GameUser gameUser = GameUser.from(player);
        UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

        if (playerUpgrade == null) {
            return;
        }

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId() && PercentUtil.acceptRandomPercent(playerUpgrade.getValue())) {
            player.getInventory().addItem(new ItemStack(Material.ARROW));
        }
    }
}
