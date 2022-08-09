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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HeightMasterPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public HeightMasterPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Мастер высоты", new ItemStack(Material.EYE_OF_ENDER));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 5, GameItemPrice.createDefault(5000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 10, GameItemPrice.createDefault(15_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(3, 15, GameItemPrice.createDefault(30_000)));

        setParameter(upgradeParameter);
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }

        if(!(event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID)) {
            return;
        }

        Player killer = event.getEntity().getKiller();

        GameUser gameUser = GameUser.from(killer);
        UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

        if (playerUpgrade == null) {
            return;
        }

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId() && PercentUtil.acceptRandomPercent(playerUpgrade.getValue())) {
            killer.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
        }
    }
}
