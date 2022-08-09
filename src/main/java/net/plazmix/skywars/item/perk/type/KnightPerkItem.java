package net.plazmix.skywars.item.perk.type;

import lombok.NonNull;
import net.plazmix.game.event.GameStateActivateEvent;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.parameter.UpgradeGameItemParameter;
import net.plazmix.game.state.GameState;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.item.perk.PerkItem;
import net.plazmix.skywars.state.IngameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class KnightPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public KnightPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Рыцарь", new ItemStack(Material.IRON_HELMET));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 5, GameItemPrice.createDefault(2500)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 10, GameItemPrice.createDefault(5000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(3, 10, GameItemPrice.createDefault(15_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(4, 10, GameItemPrice.createDefault(30_000)));

        setParameter(upgradeParameter);
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {

    }

    @EventHandler
    public void onInGameState(GameStateActivateEvent event) {
        if(event.getState().getStateName().equalsIgnoreCase("IngameState")) {

            Bukkit.getOnlinePlayers().forEach(player -> {

                GameUser gameUser = GameUser.from(player);
                UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

                if (playerUpgrade != null && gameUser.getSelectedItem(itemCategory).getId() == this.getId()) {

                    int amplifier = (playerUpgrade.getLevel()> 1) ? 1 : 0; // Если уровень больше 1, то дается сопротивление II
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20 * playerUpgrade.getValue(),amplifier));

                }
            });

        }
    }
}
