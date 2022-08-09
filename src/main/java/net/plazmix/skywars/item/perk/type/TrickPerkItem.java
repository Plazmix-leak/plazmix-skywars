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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class TrickPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public TrickPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Хитрость", new ItemStack(Material.ANVIL));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 5, GameItemPrice.createDefault(15_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 10, GameItemPrice.createDefault(30_000)));

        setParameter(upgradeParameter);
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        ItemStack hand = damager.getInventory().getItemInHand();
        if(!(hand == null || hand.getType() == Material.AIR)) {
            return;
        }

        GameUser gameUser = GameUser.from(damager);
        UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

        if (playerUpgrade == null) {
            return;
        }

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId() && PercentUtil.acceptRandomPercent(playerUpgrade.getValue())) {
            damager.getInventory().removeItem(hand);
            ((Player) event.getEntity()).getInventory().addItem(hand);
        }
    }
}
