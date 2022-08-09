package net.plazmix.skywars.item.perk.type;

import lombok.NonNull;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.parameter.UpgradeGameItemParameter;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.item.perk.PerkItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AdrenalinePerkItem extends PerkItem {
    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public AdrenalinePerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Адреналин", new ItemStack(Material.GOLD_AXE));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 1, GameItemPrice.createDefault(30_000)));

        setParameter(upgradeParameter);
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player target = (Player) event.getEntity();
        GameUser gameUser = GameUser.from(target);
        UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

        if (playerUpgrade == null) {
            return;
        }

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId()) {

            if(target.getHealth() < 6.00D) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,60,1));
            }

        }
    }
}
