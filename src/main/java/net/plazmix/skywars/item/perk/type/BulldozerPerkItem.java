package net.plazmix.skywars.item.perk.type;

import lombok.NonNull;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.parameter.UpgradeGameItemParameter;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.item.perk.PerkItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BulldozerPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public BulldozerPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Бульдозер", new ItemStack(Material.DIAMOND_SWORD));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 2, GameItemPrice.createDefault(10_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 3, GameItemPrice.createDefault(20_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(3, 4, GameItemPrice.createDefault(40_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(4, 5, GameItemPrice.createDefault(60_000)));

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

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId()) {
            killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20 * playerUpgrade.getValue(),0));
        }
    }
}
