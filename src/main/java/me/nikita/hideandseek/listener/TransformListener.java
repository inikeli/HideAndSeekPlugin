package me.nikita.hideandseek.listener;

import me.nikita.hideandseek.disguise.DisguiseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TransformListener implements Listener {

    private final DisguiseManager disguiseManager;

    public TransformListener(DisguiseManager disguiseManager) {
        this.disguiseManager = disguiseManager;
    }

    @EventHandler
    public void onStickInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // 1. Проверяем, что в руке именно палочка
        if (item == null || item.getType() != Material.STICK) {
            return;
        }

        // 2. Проверяем тип клика (правой кнопкой по воздуху или блоку)
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            // Выбираем случайный материал (или фиксированный для теста)
            Material[] possibleBlocks = {
                    Material.OAK_PLANKS,
                    Material.STONE,
                    Material.CRAFTING_TABLE,
                    Material.DIRT
            };
            Material randomMaterial = possibleBlocks[new java.util.Random().nextInt(possibleBlocks.length)];

            // 3. Вызываем маскировку
            disguiseManager.disguise(player, randomMaterial);

            player.sendMessage("§a[!] Вы превратились в: " + randomMaterial.name());

            // Отменяем событие, чтобы случайно не поставить блок или не открыть инвентарь
            event.setCancelled(true);
        }
    }
}