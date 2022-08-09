package net.plazmix.skywars.util.item;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.plazmix.actionitem.AbstractActionItem;
import net.plazmix.actionitem.ActionItem;
import net.plazmix.utility.ItemUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class PotionItem extends AbstractActionItem {

    PotionEffect[] potionEffects;

    public PotionItem(@NonNull String title,
                      @NonNull String texture,
                      @NonNull PotionEffect... potionEffects) {

        super(ItemUtil.newBuilder(Material.SKULL_ITEM)
                .setDurability(3)

                .setName(title)
                .setTextureValue(texture)

                .build());

        this.potionEffects = potionEffects;
    }

    @Override
    public void handle(@NonNull ActionItem actionItem) {
        actionItem.setPlaceHandler(blockPlaceEvent -> {
            blockPlaceEvent.setCancelled(true);
        });

        actionItem.setInteractHandler(interactEvent -> {
            Player player = interactEvent.getPlayer();

            for (PotionEffect potionEffect : potionEffects) {
                player.addPotionEffect(potionEffect);
            }

            player.playSound(player.getLocation(), Sound.DRINK, 1f, 0f);

            ItemStack item = interactEvent.getItem();
            item.setAmount(item.getAmount() - 1);

            player.setItemInHand(item);
            interactEvent.setCancelled(true);
        });
    }

}
