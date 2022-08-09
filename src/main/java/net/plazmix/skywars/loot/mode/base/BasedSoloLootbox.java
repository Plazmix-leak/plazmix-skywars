package net.plazmix.skywars.loot.mode.base;

import net.plazmix.skywars.loot.SkywarsLootUnit;
import net.plazmix.skywars.loot.mode.SkywarsModeLootbox;
import net.plazmix.skywars.util.item.MagicBoxItem;
import net.plazmix.skywars.util.item.PotionItem;
import net.plazmix.utility.ItemUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BasedSoloLootbox implements SkywarsModeLootbox {

    private final ThreadLocalRandom localRandom = ThreadLocalRandom.current();

    private void addPotionsUnits(List<SkywarsLootUnit> skywarsLootUnitList) {
        SkywarsLootUnit speedPotionUnit = SkywarsLootUnit.create()
                .setChance(80)
                .addItem(new PotionItem("§bЗелье скорости II (1 мин.)",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRjOWZmMTg0YWU3NjdkM2NiZmQ5YzNhYTJjN2U4OGIxMGY5YjU5MTI5N2ZmNjc2ZGE2MzlmYjQ0NDYyMzhjOCJ9fX0=",
                        new PotionEffect(PotionEffectType.SPEED, (60 * 20) + 40, 2))

                        .getActionItem()
                        .getItemStack());

        SkywarsLootUnit regenerationPotionUnit = SkywarsLootUnit.create()
                .setChance(80)
                .addItem(new PotionItem("§bЗелье скорости II (1 мин.)",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRjOWZmMTg0YWU3NjdkM2NiZmQ5YzNhYTJjN2U4OGIxMGY5YjU5MTI5N2ZmNjc2ZGE2MzlmYjQ0NDYyMzhjOCJ9fX0=",
                        new PotionEffect(PotionEffectType.SPEED, (60 * 20) + 40, 2))

                        .getActionItem()
                        .getItemStack());

        SkywarsLootUnit fireResistancePotionUnit = SkywarsLootUnit.create()
                .setChance(80)
                .addItem(new PotionItem("§bЗелье скорости II (1 мин.)",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRjOWZmMTg0YWU3NjdkM2NiZmQ5YzNhYTJjN2U4OGIxMGY5YjU5MTI5N2ZmNjc2ZGE2MzlmYjQ0NDYyMzhjOCJ9fX0=",
                        new PotionEffect(PotionEffectType.SPEED, (60 * 20) + 40, 2))

                        .getActionItem()
                        .getItemStack());

        skywarsLootUnitList.add(speedPotionUnit);
        skywarsLootUnitList.add(regenerationPotionUnit);
        skywarsLootUnitList.add(fireResistancePotionUnit);
    }

    private void addProjectilesUnits(List<SkywarsLootUnit> skywarsLootUnitList) {
        SkywarsLootUnit snowballUnit = SkywarsLootUnit.create()
                .addItem(new ItemStack(Material.SNOW_BALL, 16));

        SkywarsLootUnit eggsUnit = SkywarsLootUnit.create()
                .addItem(new ItemStack(Material.EGG, 16));

        skywarsLootUnitList.add(snowballUnit.addExclude(eggsUnit));
        skywarsLootUnitList.add(eggsUnit.addExclude(snowballUnit));
    }

    private void addBlocksUnits(List<SkywarsLootUnit> skywarsLootUnitList) {
        SkywarsLootUnit blocksUnit = SkywarsLootUnit.create()
                .addItem(new ItemStack(Material.STONE, localRandom.nextBoolean() ? 16 : 20, (byte) 5))
                .addItem(new ItemStack(Material.WOOD, localRandom.nextBoolean() ? 16 : 20, (byte) 2));

        skywarsLootUnitList.add(blocksUnit);
    }

    private void addBucketsUnits(List<SkywarsLootUnit> skywarsLootUnitList) {
        SkywarsLootUnit bucketsUnit = SkywarsLootUnit.create()
                .addItem(new ItemStack(Material.LAVA_BUCKET))
                .addItem(new ItemStack(Material.WATER_BUCKET));

        skywarsLootUnitList.add(bucketsUnit);
    }

    private void addToolsUnits(List<SkywarsLootUnit> skywarsLootUnitList) {
        SkywarsLootUnit diamondSwordUnit = SkywarsLootUnit.create()
                .addItem(ItemUtil.newBuilder(Material.DIAMOND_SWORD).addEnchantment(ThreadLocalRandom.current().nextBoolean() ? null : Enchantment.DAMAGE_ALL, 1).build());

        SkywarsLootUnit fishingRodUnit = SkywarsLootUnit.create()
                .addItem(new ItemStack(Material.FISHING_ROD));

        skywarsLootUnitList.add(diamondSwordUnit);
        skywarsLootUnitList.add(fishingRodUnit);
    }

    @Override
    public CompletableFuture<List<SkywarsLootUnit>> initExactlyIslandUnits() {
        List<SkywarsLootUnit> skywarsLootUnitList = new ArrayList<>();

        // Armor Units.
        SkywarsLootUnit diamondArmorUnit = SkywarsLootUnit.create()
                .addItem(new ItemStack(Material.DIAMOND_HELMET))
                .addItem(new ItemStack(Material.DIAMOND_CHESTPLATE))
                .addItem(new ItemStack(Material.DIAMOND_LEGGINGS))
                .addItem(new ItemStack(Material.DIAMOND_BOOTS));

        SkywarsLootUnit goldenArmorUnit = SkywarsLootUnit.create()
                .addItem(new ItemStack(Material.GOLD_HELMET))
                .addItem(new ItemStack(Material.GOLD_CHESTPLATE))
                .addItem(new ItemStack(Material.GOLD_LEGGINGS))
                .addItem(new ItemStack(Material.GOLD_BOOTS));

        // Tools Units.
        SkywarsLootUnit bowWithArrowsUnit = SkywarsLootUnit.create()
                .addItem(ItemUtil.newBuilder(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, localRandom.nextBoolean() ? 1 : 3).build())
                .addItem(new ItemStack(Material.ARROW, localRandom.nextBoolean() ? 15 : 20));

        // Other Units.
        SkywarsLootUnit experienceBottleUnit = SkywarsLootUnit.create()
                .addItem(new ItemStack(Material.EXP_BOTTLE, 10));

        // Add units to result list.
        skywarsLootUnitList.add(diamondArmorUnit.addExclude(goldenArmorUnit));
        skywarsLootUnitList.add(goldenArmorUnit.addExclude(diamondArmorUnit));

        this.addBlocksUnits(skywarsLootUnitList);

        this.addToolsUnits(skywarsLootUnitList);
        skywarsLootUnitList.add(bowWithArrowsUnit);

        this.addProjectilesUnits(skywarsLootUnitList);

        this.addBucketsUnits(skywarsLootUnitList);
        skywarsLootUnitList.add(experienceBottleUnit);

        return CompletableFuture.completedFuture(skywarsLootUnitList);
    }

    @Override
    public CompletableFuture<List<SkywarsLootUnit>> initProbablyIslandUnits() {
        List<SkywarsLootUnit> skywarsLootUnitList = new ArrayList<>();

        // Tools Units.
        SkywarsLootUnit diamondPickaxeUnit = SkywarsLootUnit.create()
                .setChance(42.5)
                .addItem(new ItemStack(Material.DIAMOND_PICKAXE));

        SkywarsLootUnit diamondAxeUnit = SkywarsLootUnit.create()
                .setChance(60)
                .addItem(new ItemStack(Material.DIAMOND_AXE));

        // Add units to result list.
        this.addPotionsUnits(skywarsLootUnitList);

        skywarsLootUnitList.add(diamondPickaxeUnit);
        skywarsLootUnitList.add(diamondAxeUnit);

        return CompletableFuture.completedFuture(skywarsLootUnitList);
    }

    @Override
    public CompletableFuture<List<SkywarsLootUnit>> initExactlyMiddleUnits() {
        List<SkywarsLootUnit> skywarsLootUnitList = new ArrayList<>();

        // Armor Units.
        SkywarsLootUnit diamondArmorUnit = SkywarsLootUnit.create()
                .addItem(new ItemStack(Material.DIAMOND_HELMET))
                .addItem(new ItemStack(Material.DIAMOND_CHESTPLATE))
                .addItem(new ItemStack(Material.DIAMOND_LEGGINGS))
                .addItem(new ItemStack(Material.DIAMOND_BOOTS));

        // Tools Units.
        SkywarsLootUnit bowWithArrowsUnit = SkywarsLootUnit.create()
                .addItem(ItemUtil.newBuilder(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 5).build())
                .addItem(new ItemStack(Material.ARROW, localRandom.nextBoolean() ? 15 : 20));

        // Add units to result list.
        skywarsLootUnitList.add(diamondArmorUnit);

        this.addBlocksUnits(skywarsLootUnitList);

        this.addToolsUnits(skywarsLootUnitList);
        skywarsLootUnitList.add(bowWithArrowsUnit);

        this.addProjectilesUnits(skywarsLootUnitList);

        this.addBucketsUnits(skywarsLootUnitList);

        return CompletableFuture.completedFuture(skywarsLootUnitList);
    }

    @Override
    public CompletableFuture<List<SkywarsLootUnit>> initProbablyMiddleUnits() {
        List<SkywarsLootUnit> skywarsLootUnitList = new ArrayList<>();

        // Tools Units.
        SkywarsLootUnit diamondPickaxeUnit = SkywarsLootUnit.create()
                .setChance(42.5)
                .addItem(new ItemStack(Material.DIAMOND_PICKAXE));

        SkywarsLootUnit diamondAxeUnit = SkywarsLootUnit.create()
                .setChance(60)
                .addItem(new ItemStack(Material.DIAMOND_AXE));

        // Other Units.
        SkywarsLootUnit magicBoxUnit = SkywarsLootUnit.create()
                .setChance(80)
                .addItem(new MagicBoxItem().getActionItem().getItemStack());

        // Add units to result list.
        skywarsLootUnitList.add(diamondPickaxeUnit);
        skywarsLootUnitList.add(diamondAxeUnit);

        this.addPotionsUnits(skywarsLootUnitList);

        skywarsLootUnitList.add(magicBoxUnit);

        return CompletableFuture.completedFuture(skywarsLootUnitList);
    }
}
