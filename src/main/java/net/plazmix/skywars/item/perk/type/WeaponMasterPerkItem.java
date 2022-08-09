package net.plazmix.skywars.item.perk.type;

import lombok.NonNull;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.parameter.UpgradeGameItemParameter;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.item.perk.PerkItem;
import net.plazmix.utility.PercentUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class WeaponMasterPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public WeaponMasterPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Мастер оружия", new ItemStack(Material.GOLD_SWORD));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 2, GameItemPrice.createDefault(45_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 4, GameItemPrice.createDefault(85_000)));

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
        ItemStack hand = killer.getInventory().getItemInHand();
        if(hand == null || hand.getType() == Material.AIR) {
            return;
        }

        GameUser gameUser = GameUser.from(killer);
        UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

        if (playerUpgrade == null) {
            return;
        }

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId() && PercentUtil.acceptRandomPercent(playerUpgrade.getValue())) {

            addEnchantment(hand,Enchantment.DAMAGE_ALL);

        }
    }

    private void addEnchantment(ItemStack hand, Enchantment enchantment) {
        int levelOfEnchantment = 1;

        if(hand.containsEnchantment(enchantment)) {
            levelOfEnchantment =  hand.getEnchantmentLevel(enchantment) + 1;
     //       hand.removeEnchantment(enchantment);
        }

        hand.getItemMeta().addEnchant(enchantment,levelOfEnchantment,true);
    }
}
