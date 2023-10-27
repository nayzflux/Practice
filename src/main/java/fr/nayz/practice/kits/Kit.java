package fr.nayz.practice.kits;

import fr.nayz.practice.Practice;
import fr.nayz.commons.accounts.PracticeData;
import fr.nayz.practice.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Kit {
    BUILD_UHC(0, "Build-UHC", Material.LAVA_BUCKET, 0, true, new HashMap<>()),
    CLASSIC(1, "Classic", Material.DIAMOND_SWORD, 4, true, new HashMap<>()),
    NO_DEBUFF(2, "No Debuff", Material.SPLASH_POTION, 1, true, new HashMap<>()),
    SOUP(3, "Soup", Material.MUSHROOM_STEW, 2, true, new HashMap<>()),
    ARCHER(4, "Archer", Material.BOW, 3, true, new HashMap<>()),
    SUMO(5, "Sumo", Material.LEAD, 7, false, new HashMap<>()),
    BOXING(6, "Boxing", Material.DIAMOND_CHESTPLATE, 5, true, new HashMap<>()),
    COMBO(7, "Combo", Material.PUFFERFISH, 6, false, new HashMap<>());

    private int id;
    private String name;
    private Material material;
    private int slot;
    private boolean isRanked;
    private Map<Integer, ItemStack> items;

    Kit(int id, String name, Material material,  int slot, boolean isRanked, Map<Integer, ItemStack> items) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.isRanked = isRanked;
        this.slot = slot;
        this.items = items;
    }

    public static void init() {
        // ==== BUILD UHC ====
        ItemStack diamondSwordSharpness3 = new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3).toItem();
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 6);
        ItemStack ghead = new ItemBuilder(Material.GOLDEN_APPLE).setAmount(3).setName("§6§lGolden Head").toItem();
        ItemStack steak = new ItemStack(Material.COOKED_BEEF, 64);
        ItemStack diamondAxe = new ItemStack(Material.DIAMOND_AXE);
        ItemStack diamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemStack planks = new ItemStack(Material.OAK_PLANKS, 64);
        ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
        ItemStack arrow = new ItemStack(Material.ARROW, 20);
        ItemStack lava = new ItemStack(Material.LAVA_BUCKET);
        ItemStack water = new ItemStack(Material.WATER_BUCKET);
        ItemStack bowPower3 = new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 3).toItem();
        ItemStack diamondHelmet = new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_PROJECTILE, 2).toItem();
        ItemStack diamondLeggings = new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItem();
        ItemStack diamondChestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItem();
        ItemStack diamondBoots = new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_PROJECTILE, 2).toItem();

        BUILD_UHC.items.put(0, diamondSwordSharpness3);
        BUILD_UHC.items.put(1, rod);
        BUILD_UHC.items.put(2, bowPower3);
        BUILD_UHC.items.put(3, steak);
        BUILD_UHC.items.put(4, gapple);
        BUILD_UHC.items.put(5, ghead);
        BUILD_UHC.items.put(6, diamondPickaxe);
        BUILD_UHC.items.put(7, diamondAxe);
        BUILD_UHC.items.put(8, planks);
        BUILD_UHC.items.put(9, arrow);
        BUILD_UHC.items.put(10, cobble);
        BUILD_UHC.items.put(11, water);
        BUILD_UHC.items.put(12, water);
        BUILD_UHC.items.put(13, lava);
        BUILD_UHC.items.put(14, lava);

        BUILD_UHC.items.put(39, diamondHelmet);
        BUILD_UHC.items.put(38, diamondChestplate);
        BUILD_UHC.items.put(37, diamondLeggings);
        BUILD_UHC.items.put(36, diamondBoots);

        // ==== NO DEBUFF ====
        Potion healPotion = new Potion(PotionType.INSTANT_HEAL, 2);
        healPotion.setSplash(true);
        ItemStack healItem = healPotion.toItemStack(1);

        Potion fireResistancePotion = new Potion(PotionType.FIRE_RESISTANCE);
        ItemStack fireResistanceItem = fireResistancePotion.toItemStack(1);

        Potion speedPotion = new Potion(PotionType.SPEED, 2);
        ItemStack speedItem = speedPotion.toItemStack(1);

        ItemStack enderPearl16 = new ItemStack(Material.ENDER_PEARL, 16);
        ItemStack diamondSwordS3FA2U3 = new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3).addEnchant(Enchantment.DURABILITY, 3).addEnchant(Enchantment.FIRE_ASPECT, 2).toItem();

        ItemStack diamondHelmetNoDebuff = new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.DURABILITY, 3).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItem();
        ItemStack diamondLeggingsNoDebuff = new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.DURABILITY, 3).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItem();
        ItemStack diamondChestplateNoDebuff = new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.DURABILITY, 3).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItem();
        ItemStack diamondBootsNoDebuff = new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.DURABILITY, 3).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.PROTECTION_FALL, 4).toItem();

        NO_DEBUFF.items.put(39, diamondHelmetNoDebuff);
        NO_DEBUFF.items.put(38, diamondChestplateNoDebuff);
        NO_DEBUFF.items.put(37, diamondLeggingsNoDebuff);
        NO_DEBUFF.items.put(36, diamondBootsNoDebuff);

        NO_DEBUFF.items.put(0, diamondSwordS3FA2U3);
        NO_DEBUFF.items.put(1, enderPearl16);
        NO_DEBUFF.items.put(2, speedItem);
        NO_DEBUFF.items.put(3, fireResistanceItem);

        NO_DEBUFF.items.put(4, healItem);
        NO_DEBUFF.items.put(5, healItem);
        NO_DEBUFF.items.put(6, healItem);
        NO_DEBUFF.items.put(7, healItem);

        NO_DEBUFF.items.put(8, steak);

        NO_DEBUFF.items.put(9, healItem);
        NO_DEBUFF.items.put(10, healItem);
        NO_DEBUFF.items.put(11, healItem);
        NO_DEBUFF.items.put(12, healItem);
        NO_DEBUFF.items.put(13, healItem);
        NO_DEBUFF.items.put(14, healItem);
        NO_DEBUFF.items.put(15, healItem);
        NO_DEBUFF.items.put(16, healItem);

        NO_DEBUFF.items.put(17, speedItem);

        NO_DEBUFF.items.put(18, healItem);
        NO_DEBUFF.items.put(19, healItem);
        NO_DEBUFF.items.put(20, healItem);
        NO_DEBUFF.items.put(21, healItem);
        NO_DEBUFF.items.put(22, healItem);
        NO_DEBUFF.items.put(23, healItem);
        NO_DEBUFF.items.put(24, healItem);
        NO_DEBUFF.items.put(25, healItem);

        NO_DEBUFF.items.put(26, speedItem);

        NO_DEBUFF.items.put(27, healItem);
        NO_DEBUFF.items.put(28, healItem);
        NO_DEBUFF.items.put(29, healItem);
        NO_DEBUFF.items.put(30, healItem);
        NO_DEBUFF.items.put(31, healItem);
        NO_DEBUFF.items.put(32, healItem);
        NO_DEBUFF.items.put(33, healItem);
        NO_DEBUFF.items.put(34, healItem);

        NO_DEBUFF.items.put(35, speedItem);

        // ==== ARCHER ====
        ItemStack archer = new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_INFINITE, 1).toItem();
        ItemStack arrow1 = new ItemBuilder(Material.ARROW).toItem();

        ARCHER.items.put(0, archer);
        ARCHER.items.put(8, steak);
        ARCHER.items.put(9, arrow1);


        // ==== CLASSIC ====
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack dSword = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack dH = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack dC = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack dL = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack dB = new ItemStack(Material.DIAMOND_BOOTS);
        ItemStack goldenApple8 = new ItemStack(Material.GOLDEN_APPLE, 8);
        ItemStack arrow12 = new ItemStack(Material.ARROW, 12);

        CLASSIC.items.put(0, dSword);
        CLASSIC.items.put(1, bow);
        CLASSIC.items.put(2, goldenApple8);
        CLASSIC.items.put(9, arrow12);

        CLASSIC.items.put(39, dH);
        CLASSIC.items.put(38, dC);
        CLASSIC.items.put(37, dL);
        CLASSIC.items.put(36, dB);
    }

    public void give(Player player) {
        Inventory inv = player.getInventory();

        PracticeData data = Practice.getInstance().findPlayerStatistics(player);

        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            ItemStack item = entry.getValue();
            int slot = entry.getKey();
//            int editedSlot  = data.getInventories().get(isRanked).get(slot);

            inv.setItem(slot, item);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isRanked() {
        return isRanked;
    }

    public int getSlot() {
        return slot;
    }

    public static Optional<Kit> find(Material material) {
        return Arrays.stream(values()).filter(k -> k.getMaterial().equals(material)).findFirst();
    }

    public static Optional<Kit> find(int id) {
        return Arrays.stream(values()).filter(k -> k.getId() == id).findFirst();
    }

    public Map<Integer, ItemStack> getItems() {
        return items;
    }
}
