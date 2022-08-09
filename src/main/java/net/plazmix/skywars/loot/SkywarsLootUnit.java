package net.plazmix.skywars.loot;

import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SkywarsLootUnit {

    public static SkywarsLootUnit create() {
        return new SkywarsLootUnit();
    }

    private final Set<ItemStack> lootItemsSet = new HashSet<>();
    private final Set<Enchantment> upgradableEnchantments = new HashSet<>();

    private boolean setEnchantmentsForUpgrade;

    @Getter
    private final List<SkywarsLootUnit> excludeList = new ArrayList<>();

    @Getter
    private double chance = 100.0;

    public SkywarsLootUnit addUpgradeEnchantment(Enchantment enchantmentForUpgrade) {
        upgradableEnchantments.add(enchantmentForUpgrade);
        return this;
    }

    public SkywarsLootUnit addItem(ItemStack item) {
        lootItemsSet.add(item);
        return this;
    }

    public SkywarsLootUnit addExclude(SkywarsLootUnit exclude) {
        excludeList.add(exclude);
        return this;
    }

    public SkywarsLootUnit setChance(double chance) {
        this.chance = chance;
        return this;
    }

    public SkywarsLootUnit setEnchantmentsForUpgrade(boolean flag) {
        this.setEnchantmentsForUpgrade = flag;
        return this;
    }

    public Set<ItemStack> getItems() {
        if (!setEnchantmentsForUpgrade) {
            return lootItemsSet;
        }

        for (ItemStack itemStack : this.lootItemsSet) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            for (Enchantment enchantment : this.upgradableEnchantments) {
                int previousLevel = Math.max(0, itemMeta.getEnchantLevel(enchantment));

                itemMeta.removeEnchant(enchantment);
                itemMeta.addEnchant(enchantment, previousLevel + 1, true);
            }
        }

        return lootItemsSet;
    }

}
