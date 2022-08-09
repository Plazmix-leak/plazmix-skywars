package net.plazmix.skywars.item.predeath;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.plazmix.cosmetics.game.DeathSoundManager;
import net.plazmix.game.item.GameItem;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.user.GameUser;
import net.plazmix.utility.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PreDeathItem extends GameItem {

    @NonNull
    DeathSoundManager deathSoundManager;

    public PreDeathItem(int id, DeathSoundManager deathSoundManager) {
        super(id, GameItemPrice.createDefault(10_000), deathSoundManager.getName(), new ItemStack(Material.RECORD_4));
        this.deathSoundManager = deathSoundManager;
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            deathSoundManager.run(player);
        }
    }

    @Override
    protected void onCancel(@NonNull GameUser gameUser) {
        // nothing.
    }
}
