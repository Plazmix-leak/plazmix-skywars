package net.plazmix.skywars.loot;

import lombok.RequiredArgsConstructor;
import net.plazmix.game.GamePlugin;
import net.plazmix.skywars.util.GameConstants;
import net.plazmix.skywars.util.SkywarsHelper;
import net.plazmix.skywars.util.SkywarsMode;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class SkywarsChestManager {

    private final List<Chest> chestList;
    private final List<SkywarsLootUnit> lootElementList = new ArrayList<>();

    public SkywarsChestManager addLootElement(SkywarsLootUnit element) {
        for (SkywarsLootUnit cachedElement : this.lootElementList) {

            if (cachedElement.getExcludeList().contains(element)) {
                throw new SkywarsLootException("exclude");
            }
        }

        this.lootElementList.add(element);
        return this;
    }

    private int currentChestCounter = 0;
    private void fillItems(List<ItemStack> itemList) {

        ThreadLocalRandom random = ThreadLocalRandom.current();
        Chest chest = chestList.get(currentChestCounter);

        if (chest != null && chest.getBlockInventory() != null) {
            chest.getBlockInventory().clear();

            itemList.forEach(item -> {
                Inventory inventory = chest.getBlockInventory();
                inventory.setItem(this.generateNewSlot(random, inventory), item);
            });
        }

        currentChestCounter++;
    }

    public void fill() {
        List<ItemStack> lootableItemList = new ArrayList<>();

        for (SkywarsLootUnit skywarsLootUnit : lootElementList) {
            lootableItemList.addAll(skywarsLootUnit.getItems());
        }

        Arrays.stream(SkywarsHelper.chunkify(lootableItemList, chestList.size()))
                .forEach(this::fillItems);

        currentChestCounter = 0;
        GamePlugin.getInstance().getCache().increment(GameConstants.INGAME_LOOT_REFILLS_COUNT);
    }

    private int generateNewSlot(ThreadLocalRandom random, Inventory inventory) {
        int slot = random.nextInt(0, inventory.getSize());

        while (inventory.getItem(slot) != null && !inventory.getItem(slot).getType().equals(Material.AIR)) {
            slot = random.nextInt(0, inventory.getSize());
        }

        return slot;
    }

    public int getIngameLootRefillsCount() {
        return GamePlugin.getInstance().getCache().getInt(GameConstants.INGAME_LOOT_REFILLS_COUNT);
    }
}
