package fr.nayz.practice.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {
    public ItemStack item;
    public ItemMeta meta;

    public ItemBuilder(Material material) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        meta.displayName(Component.text(name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(String ...lines) {
        List<Component> components = new ArrayList<Component>();

        for (String line : lines) {
            components.add(Component.text(line));
        }

        meta.lore(components);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setSkullOwner(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        ((SkullMeta) meta).setOwningPlayer(player);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        item.addEnchantment(enchantment, level);
        return this;
    }

    public ItemStack toItem() {
        return item;
    }
}
