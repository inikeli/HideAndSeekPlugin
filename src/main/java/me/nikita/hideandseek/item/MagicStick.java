package me.nikita.hideandseek.item;

import me.nikita.hideandseek.HideAndSeekPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.List;

public class MagicStick {

    // Ключ, по которому плагин поймет, что это ТА САМАЯ палка
    private static NamespacedKey key;

    // Инициализируем ключ (нужно вызвать в onEnable)
    public static void init(HideAndSeekPlugin plugin) {
        key = new NamespacedKey(plugin, "magic_transform_stick");
    }

    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§d§lMagic Transform Stick");

            // Добавляем описание (Lore), чтобы выглядело круче
            meta.setLore(List.of(
                    "§7ПКМ по блоку: превратиться",
                    "§7ПКМ в воздух: случайный блок"
            ));

            // Включаем свечение без чар (твой метод)
            meta.setEnchantmentGlintOverride(true);

            // ЗАЩИТА: Добавляем скрытую метку в NBT предмета
            meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

            item.setItemMeta(meta);
        }
        return item;
    }

    // Метод для проверки: "А та ли это палка?"
    public static boolean isMagicStick(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
}