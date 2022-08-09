package net.plazmix.skywars.item.dance;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.plazmix.cosmetics.game.DanceManager;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.item.GameItem;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.user.GameUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class DanceItem extends GameItem {

    @NonNull
    DanceManager danceManager;

    public DanceItem(int id, DanceManager danceManager) {
        super(id, GameItemPrice.createDefault(10_000), danceManager.getName(), new ItemStack(Material.RECORD_4));
        this.danceManager = danceManager;
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {
        Player player = gameUser.getBukkitHandle();

        if (player != null) {
            danceManager.run(GamePlugin.getInstance(), player);
        }
    }

    @Override
    protected void onCancel(@NonNull GameUser gameUser) {
        // nothing.
    }
}
