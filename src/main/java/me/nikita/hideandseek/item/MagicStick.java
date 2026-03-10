package me.nikita.hideandseek.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MagicStick {

    public static ItemStack create() {

        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§dMagic Transform Stick");
        meta.setEnchantmentGlintOverride(true);

        item.setItemMeta(meta);

        return item;
    }

}