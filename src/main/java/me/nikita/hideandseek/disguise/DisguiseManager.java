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

    public void disguise(Player player, Material material) {
        removeDisguise(player);

        // 1. Подготавливаем локацию: берем координаты игрока, но убираем повороты головы
        org.bukkit.Location loc = player.getLocation();
        loc.setYaw(0);   // Смотрит строго по оси Z
        loc.setPitch(0); // Смотрит строго горизонтально

        // 2. Спавним сущность уже в выровненной локации
        BlockDisplay blockDisplay = (BlockDisplay) player.getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
        blockDisplay.setBlock(Bukkit.createBlockData(material));

        // 3. Настройка трансформации (размер и центровка)
        float scale = 1.001f;
        Transformation transformation = blockDisplay.getTransformation();
        transformation.getScale().set(scale, scale, scale);

        // Сдвигаем на 0.5 блока, чтобы игрок был ровно в центре блока
        float offset = -(scale / 2.0f);
        transformation.getTranslation().set(offset, -1.8f, offset);

        blockDisplay.setTransformation(transformation);

        // 4. Замораживаем поворот блока
        // Billboard.FIXED гарантирует, что блок НЕ будет вращаться вслед за игроком
        blockDisplay.setBillboard(org.bukkit.entity.Display.Billboard.FIXED);

        // Делаем блок ярким, чтобы он не затенялся внутри модельки игрока
        blockDisplay.setBrightness(new org.bukkit.entity.Display.Brightness(15, 15));

        // 5. Привязываем к игроку
        player.setInvisible(true);
        player.addPassenger(blockDisplay);

        disguisedPlayers.put(player.getUniqueId(), blockDisplay.getUniqueId());
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