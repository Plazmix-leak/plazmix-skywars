package net.plazmix.skywars.item.kit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.plazmix.game.item.GameItem;
import net.plazmix.game.item.GameItemPrice;
import net.plazmix.game.user.GameUser;
import net.plazmix.game.utility.hotbar.GameHotbarBuilder;
import net.plazmix.utility.ItemUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@RequiredArgsConstructor
public enum KitItem {

    SCOUT("Скаут", 20_000, GameHotbarBuilder.newBuilder()
            .addItem(1, ItemUtil.newBuilder(Material.POTION).setAmount(2).addCustomPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20 + 40, 1), true).build()),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRjOWZmMTg0YWU3NjdkM2NiZmQ5YzNhYTJjN2U4OGIxMGY5YjU5MTI5N2ZmNjc2ZGE2MzlmYjQ0NDYyMzhjOCJ9fX0="),

    WOODCUTTER("Эколог", 20_000, GameHotbarBuilder.newBuilder()
            .addItem(1, ItemUtil.newBuilder(Material.IRON_AXE).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DAMAGE_ALL, 2).build())
            .addItem(2, new ItemStack(Material.WOOD, 20, (byte) 2)),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQyZTMxMDg3OWE2NDUwYWY1NjI1YmNkNDUwOTNkZDdlNWQ4ZjgyN2NjYmZlYWM2OWM4MTUzNzc2ODQwNmIifX19"),

    HEALER("Лекарь", 25_000, GameHotbarBuilder.newBuilder()
            .addItem(1, new ItemStack(Material.GOLDEN_CARROT, 3))
            .addItem(2, ItemUtil.newBuilder(Material.POTION).addCustomPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20 + 40, 1), true).build()),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjk2MTc4OGVlMzI2YWFhMmE4ZGY0ZmQ1Njg5YjRkZWY2MWY2ZDFmZjMwODMxOWFlZDEyMTkzYTk0NjUzNTY5OSJ9fX0="),

    ARMORER("Бронник", 20_000, GameHotbarBuilder.newBuilder()
            .addItem(1, new ItemStack(Material.CHAINMAIL_HELMET))
            .addItem(2, new ItemStack(Material.DIAMOND_CHESTPLATE)),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGMyNGE2MTZkZmZhZTg2MmNmYjllYTczMzlkODlmMjA0MDliOWE4ZTM4NTEzODU2YmIxNDE0MjAxNmZmMTZjZSJ9fX0="),

    KNIGHT("Рыцарь", 25_000, GameHotbarBuilder.newBuilder()
            .addItem(1, ItemUtil.newBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 2).build())
            .addItem(2, ItemUtil.newBuilder(Material.GOLD_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5).addEnchantment(Enchantment.DURABILITY, 5).build()),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmE5MTUxYTYyMzIxZWIzNGFlNGIzOWRmMzhjODE5MmVhNTliNGFkZDQ4OGNjYmQyMjI4ZTVjN2JhY2U5YzZhNCJ9fX0="),

    FOOTBALL_PLAYER("Бейсболист", 25_000, GameHotbarBuilder.newBuilder()
            .addItem(1, ItemUtil.newBuilder(Material.WOOD_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.KNOCKBACK, 1).build())
            .addItem(2, ItemUtil.newBuilder(Material.IRON_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build()),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTUzZjIzYTlkZGE5ZTRkZWJhNmNkZjBmODhmZGE4NDEyMjk2NTg2NTliNzFlMzc5OWE2YjU0ZGVmZTkyYzYifX19"),

    FISHER("Рыбак", 25_000, GameHotbarBuilder.newBuilder()
            .addItem(1, ItemUtil.newBuilder(Material.FISHING_ROD).addEnchantment(Enchantment.KNOCKBACK, 1).addEnchantment(Enchantment.DURABILITY, 4).build())
            .addItem(2, ItemUtil.newBuilder(Material.CHAINMAIL_HELMET).addEnchantment(Enchantment.DURABILITY, 2).addEnchantment(Enchantment.OXYGEN, 2).build())
            .addItem(3, ItemUtil.newBuilder(Material.IRON_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.WATER_WORKER, 2).build()),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzOGY2NzM4MDhjZTNjZTc5NTdlYzUxOTlmNTAyZDFmZTFkYWJkMDNlODExZDY4NzZmZjI5NTFlZGUxNGU2NiJ9fX0="),

    FARMER("Фермер", 20_000, GameHotbarBuilder.newBuilder()
            .addItem(1, ItemUtil.newBuilder(Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4).build())
            .addItem(2, new ItemStack(Material.EGG, 32))
            .addItem(3, ItemUtil.newBuilder(Material.POTION).addCustomPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40 * 20 + 40, 2), true).build()),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgwNzI3ZDM5MGMzYjM0ZDgwOWE0ZDU5MDhjNThiNWVkNmE4OTFiOGJkZDBiY2ExNjA2NWNkNTVmMGQ4ZGUwYyJ9fX0="),

    BLACKSMITH("Кузнец", 20_000, GameHotbarBuilder.newBuilder()
            .addItem(1, new ItemStack(Material.ANVIL))
            .addItem(2, ItemUtil.newBuilder(Material.ENCHANTED_BOOK).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DAMAGE_ALL, 1).build())
            .addItem(3, new ItemStack(Material.EXP_BOTTLE, 32))
            .addItem(4, new ItemStack(Material.DIAMOND_HELMET)),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI3Y2Y4YjgyZDI0OTZlMTNhYWI3YjQ2ZTRmZDIxZGQ5YjE2N2ViMmUyZDBmM2JiOGM5NjIwZTUwNDdjOGNmYiJ9fX0="),

    DEMON("Демон", 25_000, GameHotbarBuilder.newBuilder()
            .addItem(1, ItemUtil.newBuilder(Material.IRON_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.THORNS, 1).build())
            .addItem(2, ItemUtil.newBuilder(Material.POTION).addCustomPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60 * 20 + 40, 1), true).build())
            .addItem(3, new ItemStack(Material.NETHERRACK, 20))
            .addItem(4, new ItemStack(Material.FLINT_AND_STEEL)),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU3YjljYWY3NmNmNzdjOGI0MTJmZjQ0OGRjNWM1OTdmNWEzZmQzMDY1YjM4ZWQzMDVmZTUzNTMzNjFhNWMyYSJ9fX0="),

    BOMBER("Подрывник", 20_000, GameHotbarBuilder.newBuilder()
            .addItem(1, new ItemStack(Material.WATER_BUCKET))
            .addItem(2, new ItemStack(Material.TNT, 20))
            .addItem(3, new ItemStack(Material.REDSTONE_BLOCK, 10))
            .addItem(4, new ItemStack(Material.WOOD_PLATE))
            .addItem(5, ItemUtil.newBuilder(Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_FALL, 4).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4).build()),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FmNTk3NzZmMmYwMzQxMmM3YjU5NDdhNjNhMGNmMjgzZDUxZmU2NWFjNmRmN2YyZjg4MmUwODM0NDU2NWU5In19fQ=="),

    SPECIALIST("Специалист", 20_000, GameHotbarBuilder.newBuilder()
            .addItem(1, ItemUtil.newBuilder(Material.DIAMOND_PICKAXE).addEnchantment(Enchantment.DIG_SPEED, 4).addEnchantment(Enchantment.DAMAGE_ALL, 2).addEnchantment(Enchantment.LUCK, 2).build())
            .addItem(2, new ItemStack(Material.COAL_BLOCK, 20)),
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmMGM2YTZmNTUyNWQwODc2NzczNzY4ZTJhN2Y2YmQ0MzYwOGQ4YjE1ZjBlODc4MGY2NGQ1MTJmMjAifX19"),
    ;

    private final String name;
    private final int priceAsCoins;

    private final GameHotbarBuilder builder;

    private final String headTexture;

    public GameItem createGameItem() {
        return new GameItem(this.ordinal(), GameItemPrice.createDefault(priceAsCoins), name, ItemUtil.newBuilder(Material.SKULL_ITEM).setDurability(3).setTextureValue(headTexture).build()) {

            @Override
            protected void onApply(@NonNull GameUser gameUser) {
                builder.setMoveItems(true);
                builder.setAllowInteraction(true);

                builder.build().setHotbarTo(gameUser.getBukkitHandle());
            }
        };
    }
}
