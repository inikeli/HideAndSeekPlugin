package me.nikita.hideandseek.disguise;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DisguiseManager {

    private final JavaPlugin plugin;
    // Храним UUID игрока -> UUID его блока-маскировки
    private final Map<UUID, UUID> disguisedPlayers = new HashMap<>();

    public DisguiseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public void clearAllDisguises() {
        // 1. Сначала проходим по всем игрокам онлайн и возвращаем им видимость
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setInvisible(false);
            // Снимаем пассажиров (наши блоки)
            player.getPassengers().forEach(player::removePassenger);
        }

        // 2. Очищаем мапу, так как мы всех "размаскировали"
        disguisedPlayers.clear();

        // 3. Ультимативная очистка всех BlockDisplay во всех мирах сервера
        // Это удалит даже те блоки, которые "зависли" после вылета игрока
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (BlockDisplay display : world.getEntitiesByClass(BlockDisplay.class)) {
                // Если ты использовал метадату, можно проверять её здесь,
                // но для команды 'clear' лучше удалить вообще все BlockDisplay
                display.remove();
            }
        }
    }

    public void disguise(Player player, Material material) {
        // 1. Сначала полностью очищаем старое состояние
        removeDisguise(player);

        // 2. Проверка на валидность блока (защита от лодок и табличек)
        if (!material.isBlock()) {
            material = Material.OAK_PLANKS; // Дефолтный блок, если попался плохой материал
        }

        // 3. Подготовка локации
        org.bukkit.Location loc = player.getLocation();
        loc.setYaw(0);
        loc.setPitch(0);

        // 4. Создание блока
        BlockDisplay blockDisplay = (BlockDisplay) player.getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
        blockDisplay.setBlock(Bukkit.createBlockData(material));

        // Настройка трансформации
        float scale = 1.001f;
        Transformation transformation = blockDisplay.getTransformation();
        transformation.getScale().set(scale, scale, scale);
        float offset = -(scale / 2.0f);
        transformation.getTranslation().set(offset, -1.6f, offset); // -1.6 обычно лучше чем -1.8
        blockDisplay.setTransformation(transformation);
        blockDisplay.setBillboard(org.bukkit.entity.Display.Billboard.FIXED);
        blockDisplay.setBrightness(new org.bukkit.entity.Display.Brightness(15, 15));

        // 5. МАГИЯ: Используем планировщик, чтобы посадить пассажира через 1 тик
        // Это гарантирует, что клиент увидит новый блок
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.setInvisible(true);
            player.addPassenger(blockDisplay);
            disguisedPlayers.put(player.getUniqueId(), blockDisplay.getUniqueId());
        }, 1L);
    }

    public void removeDisguise(Player player) {
        UUID displayId = disguisedPlayers.remove(player.getUniqueId());
        if (displayId != null) {
            // Ищем сущность по UUID и удаляем
            org.bukkit.entity.Entity display = Bukkit.getEntity(displayId);
            if (display != null) {
                display.remove();
            }
        }
        player.setInvisible(false);
        player.getPassengers().forEach(player::removePassenger);
    }
}