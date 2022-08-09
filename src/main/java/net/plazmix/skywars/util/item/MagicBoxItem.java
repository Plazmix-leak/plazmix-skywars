package net.plazmix.skywars.util.item;

import lombok.NonNull;
import net.plazmix.actionitem.AbstractActionItem;
import net.plazmix.actionitem.ActionItem;
import net.plazmix.game.GamePlugin;
import net.plazmix.holographic.ProtocolHolographic;
import net.plazmix.skywars.util.deathangel.SkywarsDamnService;
import net.plazmix.utility.ItemUtil;
import net.plazmix.utility.PercentUtil;
import net.plazmix.utility.RotatingHead;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public final class MagicBoxItem extends AbstractActionItem {

    private static final String SIMPLE_BOX_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWFmOGRhYWRjZGRiMDg4YThlZDg3NTliYTAyNzcwZDcyODIxNGYwN2NkZDkzYTYzMGI4ZTdkM2NhMDM3M2RjIn19fQ==";
    private static final String DAMN_BOX_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmE2ZGFjODAzNWQzNjFiYTdmMmMyYTYxNGI0ZWJhYWZjMWU1ZTMxMDFmODViZWVmNjgzNTM2ZjMzN2U1MDkwIn19fQ==";

    private static final String SIMPLE_POTION_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM2MjllODZhZmRiNTgyZGI4MjM1MzI3NDQ5NTczMTNmYzI2YjYyMTk0ODI5YzhkM2Y4MTRjODAyODk2YjIifX19";
    private static final String DAMN_POTION_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM2MjllODZhZmRiNTgyZGI4MjM1MzI3NDQ5NTczMTNmYzI2YjYyMTk0ODI5YzhkM2Y4MTRjODAyODk2YjIifX19";

    private static final ItemStack[] SIMPLE_DROPPED_ITEMS_ARRAY = {

            ItemUtil.newBuilder(Material.GOLD_SWORD)
                    .setName("§6Магический меч")
                    .addEnchantment(Enchantment.DAMAGE_ALL, 4)
                    .addEnchantment(Enchantment.FIRE_ASPECT, 2)
                    .build(),

            ItemUtil.newBuilder(Material.DIAMOND_CHESTPLATE)
                    .setName("§6Магический доспех")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                    .build(),

            new PotionItem("§6Магическое зелье", SIMPLE_POTION_TEXTURE,
                    new PotionEffect(PotionEffectType.SPEED, (50 * 20) + 40, 1),
                    new PotionEffect(PotionEffectType.SPEED, (50 * 20) + 40, 1))

                    .getActionItem()
                    .getItemStack(),
    };

    private static final ItemStack[] DAMN_DROPPED_ITEMS_ARRAY = {

            ItemUtil.newBuilder(Material.GOLD_SWORD)
                    .setName("§cПроклятый меч")
                    .addEnchantment(Enchantment.DAMAGE_ALL, 4)
                    .addEnchantment(Enchantment.FIRE_ASPECT, 2)
                    .build(),

            ItemUtil.newBuilder(Material.DIAMOND_CHESTPLATE)
                    .setName("§cПроклятый доспех")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                    .build(),

            new PotionItem("§cПроклятое зелье", DAMN_POTION_TEXTURE,
                    new PotionEffect(PotionEffectType.SPEED, (30 * 20) + 40, 1),
                    new PotionEffect(PotionEffectType.SPEED, (30 * 20) + 40, 1))

                    .getActionItem()
                    .getItemStack(),
    };

    public MagicBoxItem() {
        super(ItemUtil.newBuilder(Material.SKULL_ITEM)
                .setDurability(3)

                .setName("§6Магический ящик")
                .setTextureValue(SkywarsDamnService.INSTANCE.isActive() ? DAMN_BOX_TEXTURE : SIMPLE_BOX_TEXTURE)

                .build());
    }

    @Override
    public void handle(@NonNull ActionItem actionItem) {
        actionItem.setInteractHandler(interactEvent -> {

            ItemStack item = interactEvent.getItem();
            item.setAmount(item.getAmount() - 1);

            interactEvent.getPlayer().setItemInHand(item);
            interactEvent.setCancelled(true);

            this.startPlaceAnimation(interactEvent);
        });

        actionItem.setPlaceHandler(blockPlaceEvent -> blockPlaceEvent.setCancelled(true));
    }

    private void startPlaceAnimation(PlayerInteractEvent event) {
        this.startAnimationTask(event.getPlayer(),
                new RotatingHead(GamePlugin.getInstance(), event.getClickedBlock().getLocation().clone().add(0, 1, 0),
                        SkywarsDamnService.INSTANCE.isActive() ? DAMN_BOX_TEXTURE : SIMPLE_BOX_TEXTURE));
    }

    private void startAnimationTask(Player opener, RotatingHead rotatingHead) {
        rotatingHead.register();

        new BukkitRunnable() {

            private long counter;
            private float upSoundPitch;

            @Override
            public void cancel() {
                this.counter = 0;
                super.cancel();

                rotatingHead.getLocation().getWorld().playEffect(rotatingHead.getFakeArmorStand().getLocation(), Effect.EXPLOSION_LARGE, 3);
                rotatingHead.unregister();

                ItemStack[] itemsArray = SkywarsDamnService.INSTANCE.isActive() ? DAMN_DROPPED_ITEMS_ARRAY : SIMPLE_DROPPED_ITEMS_ARRAY;

                for (int count = 0; count <= ThreadLocalRandom.current().nextInt(1, itemsArray.length + 1); count++) {

                    ItemStack itemStack = itemsArray[ThreadLocalRandom.current().nextInt(0, itemsArray.length)];
                    Location location = rotatingHead.getLocation().clone().add(0, 1.85, 0);

                    rotatingHead.getLocation().getWorld().dropItemNaturally(location, itemStack);
                }

                if (SkywarsDamnService.INSTANCE.isActive() && PercentUtil.acceptRandomPercent(20)) {

                    opener.setHealth(opener.getHealth() - 1);
                    opener.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (5 * 20) + 40, 1));

                    opener.sendMessage("§cТы хотел получить редкий предмет, но в этот раз ты был проклят!");
                }
            }

            @Override
            public void run() {
                counter++;
                if (counter % 2 != 0) {
                    return;
                }

                Location location = rotatingHead.getFakeArmorStand().getLocation().clone().add(0, 0.1, 0);

                // 2 sec later
                if (this.counter >= (20L * 2)) {
                    Location explosionLocation = location.clone().add(0, 1.85, 0);

                    location.getWorld().playEffect(explosionLocation, Effect.EXPLOSION_LARGE, 3);
                    location.getWorld().playSound(explosionLocation, Sound.EXPLODE, 1, 1);

                    rotatingHead.getFakeArmorStand().remove();
                    rotatingHead.setRotateSpeed(0);

                    this.cancel();

                } else {

                    float rotatingSpeed = rotatingHead.getRotatingSpeed() + 0.12F;

                    rotatingHead.getFakeArmorStand().teleport(location);
                    rotatingHead.setRotateSpeed(rotatingSpeed);


                    Location particlesLocation = location.clone().add(0, 1.85, 0);

                    location.getWorld().spigot().playEffect(particlesLocation, Effect.FIREWORKS_SPARK, 0, 0, 0.05f, 0.05f, 0.05f, 0.05f, 5, 100);
                    location.getWorld().spigot().playEffect(particlesLocation, Effect.WITCH_MAGIC, 0, 0, 0.05f, 0.05f, 0.05f, 0.05f, 5, 100);

                    location.getWorld().playSound(particlesLocation, Sound.NOTE_PLING, 1, upSoundPitch += 0.05f);
                }
            }
        }.runTaskTimer(GamePlugin.getInstance(), 0, 1);
    }

}
