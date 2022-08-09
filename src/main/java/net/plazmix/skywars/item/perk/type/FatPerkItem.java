package net.plazmix.skywars.item.perk.type;

import lombok.NonNull;
import net.plazmix.game.event.GameStateActivateEvent;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.parameter.UpgradeGameItemParameter;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.item.perk.PerkItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FatPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public FatPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Толстый", new ItemStack(Material.CHAINMAIL_CHESTPLATE));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 5, GameItemPrice.createDefault(2500)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 10, GameItemPrice.createDefault(5000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(3, 15, GameItemPrice.createDefault(15_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(4, 20, GameItemPrice.createDefault(30_000)));

        setParameter(upgradeParameter);
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {

    }

    @EventHandler
    public void onInGameState(GameStateActivateEvent event) {
        if(event.getState().getStateName().equalsIgnoreCase("Идет игра")) {
            Bukkit.getOnlinePlayers().forEach(player -> {

                GameUser gameUser = GameUser.from(player);
                UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

                if (playerUpgrade != null && gameUser.getSelectedItem(itemCategory).getId() == id) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,20 * playerUpgrade.getValue(),0));
                }
            });
        }
    }
}
