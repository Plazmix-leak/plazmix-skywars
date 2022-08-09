package net.plazmix.skywars.item.killeffect;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.plazmix.cosmetics.game.KillEffectManager;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.item.GameItem;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.user.GameUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class KillEffectItem extends GameItem {

    @NonNull
    KillEffectManager killEffect;

    public KillEffectItem(int id, KillEffectManager killEffect) {
        super(id, GameItemPrice.createDefault(10_000), killEffect.getName(), new ItemStack(Material.RECORD_4));
        this.killEffect = killEffect;
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {
        Player player = gameUser.getBukkitHandle();

        if (player != null && player.getKiller() != null) {
            killEffect.run(GamePlugin.getInstance(), player.getKiller(), player);
        }
    }

    @Override
    protected void onCancel(@NonNull GameUser gameUser) {
        // nothing.
    }
}
