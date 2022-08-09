package net.plazmix.skywars.item.cage;

import lombok.NonNull;
import net.plazmix.game.item.GameItem;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.util.GameConstants;
import net.plazmix.utility.location.region.CuboidRegion;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public final class CageItem extends GameItem {

    public CageItem(int id, @NonNull GameItemPrice price, @NonNull String itemName, @NonNull ItemStack iconItem) {
        super(id, price, itemName, iconItem);
    }

    private CuboidRegion initRegion(@NonNull GameUser gameUser) {
        Location islandLocation = gameUser.getCache().get(GameConstants.INGAME_PLAYER_ISLAND_LOC, Location.class);

        Location startLocation = islandLocation.clone().add(2, 4, 2);
        Location endLocation = islandLocation.clone().subtract(3, 1, 3);

        return new CuboidRegion(startLocation, endLocation);
    }

    private void updateBlock(@NonNull Block block) {
        block.getWorld().spigot().playEffect(block.getLocation(), Effect.CLOUD, 0, 0,
                0.1f, 0.1f, 0.1f, 0.1f, 5, 1);

        block.setType(getIconItem().getType());
        block.setData((byte) getIconItem().getDurability());
    }

    private void clearBlock(@NonNull Block block) {
        block.getWorld().spigot().playEffect(block.getLocation(), Effect.SMOKE, 0, 0,
                0.1f, 0.1f, 0.1f, 0.1f, 5, 1);

        block.setType(Material.AIR);
    }

    @Override
    protected void onApply(@NonNull GameUser gameUser) {
        CuboidRegion cuboidRegion = initRegion(gameUser);

        cuboidRegion.getFace(BlockFace.UP).forEachBlock(this::updateBlock);
        cuboidRegion.getFace(BlockFace.DOWN).forEachBlock(this::updateBlock);
        cuboidRegion.getFace(BlockFace.EAST).forEachBlock(this::updateBlock);
        cuboidRegion.getFace(BlockFace.NORTH).forEachBlock(this::updateBlock);
        cuboidRegion.getFace(BlockFace.SOUTH).forEachBlock(this::updateBlock);
        cuboidRegion.getFace(BlockFace.WEST).forEachBlock(this::updateBlock);
    }

    @Override
    protected void onCancel(@NonNull GameUser gameUser) {
        CuboidRegion cuboidRegion = initRegion(gameUser);

        cuboidRegion.getFace(BlockFace.UP).forEachBlock(this::clearBlock);
        cuboidRegion.getFace(BlockFace.DOWN).forEachBlock(this::clearBlock);
        cuboidRegion.getFace(BlockFace.EAST).forEachBlock(this::clearBlock);
        cuboidRegion.getFace(BlockFace.NORTH).forEachBlock(this::clearBlock);
        cuboidRegion.getFace(BlockFace.SOUTH).forEachBlock(this::clearBlock);
        cuboidRegion.getFace(BlockFace.WEST).forEachBlock(this::clearBlock);
    }

}
