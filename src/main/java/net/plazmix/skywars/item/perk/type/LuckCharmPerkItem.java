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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LuckCharmPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public LuckCharmPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Шарм удачи", new ItemStack(Material.GOLDEN_APPLE));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 10, GameItemPrice.createDefault(5000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 20, GameItemPrice.createDefault(10_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(3, 30, GameItemPrice.createDefault(20_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(4, 40, GameItemPrice.createDefault(30_000)));

        setParameter(upgradeParameter);
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            return;
        }
        Player killer = event.getEntity().getKiller();

        GameUser gameUser = GameUser.from(killer);
        UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

        if (playerUpgrade == null) {
            return;
        }

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId() && PercentUtil.acceptRandomPercent(playerUpgrade.getValue())) {
            killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
        }
    }
}
