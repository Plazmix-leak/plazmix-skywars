package net.plazmix.skywars.item.perk.type;

import lombok.NonNull;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.item.parameter.UpgradeGameItemParameter;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.PlazmixSkywarsPlugin;
import net.plazmix.skywars.item.perk.PerkItem;
import net.plazmix.utility.PercentUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BeekeeperPerkItem extends PerkItem {

    private final UpgradeGameItemParameter upgradeParameter
            = new UpgradeGameItemParameter(this);

    public BeekeeperPerkItem(int id) {
        super(id, GameItemPrice.createDefault(1), "Пчеловод", new ItemStack(Material.POTATO_ITEM));

        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(1, 5, GameItemPrice.createDefault(5000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(2, 10, GameItemPrice.createDefault(10_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(3, 15, GameItemPrice.createDefault(23_000)));
        upgradeParameter.addUpgrade(new UpgradeGameItemParameter.Upgrade(4, 20, GameItemPrice.createDefault(40_000)));

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

        Player player = (Player) event.getEntity();
        GameUser gameUser = GameUser.from(player);
        UpgradeGameItemParameter.Upgrade playerUpgrade = upgradeParameter.getPlayerUpgrade(gameUser);

        if (playerUpgrade == null) {
            return;
        }

        if (gameUser.getSelectedItem(itemCategory).getId() == this.getId() && PercentUtil.acceptRandomPercent(playerUpgrade.getValue())) {
            Player target = (Player) event.getDamager();

            Vector direction = target.getLocation().getDirection().normalize();
            direction.multiply(-2);

            Location location = target.getLocation().add(direction);

            Bat bat = target.getWorld().spawn(location, Bat.class);
            ((CraftEntity) bat).getHandle().setInvisible(true);

            new BukkitRunnable() {

                @Override
                public void run() {

                    if (bat.isDead()) {
                        cancel();
                        return;
                    }

                    bat.getNearbyEntities(10, 10, 10).forEach(en -> {

                        if (en instanceof Player) {
                            Player player = (Player) en;
                            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
                        }
                    });

                }
            }.runTaskTimer(PlazmixSkywarsPlugin.getInstance(), 0, 20L);

        }
    }
}
