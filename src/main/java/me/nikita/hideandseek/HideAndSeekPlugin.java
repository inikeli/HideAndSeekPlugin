package me.nikita.hideandseek;

import me.nikita.hideandseek.disguise.DisguiseManager;
import org.bukkit.plugin.java.JavaPlugin;
import me.nikita.hideandseek.listener.TransformListener;

public class HideAndSeekPlugin extends JavaPlugin {

    private static HideAndSeekPlugin instance;
    private DisguiseManager disguiseManager;

    @Override
    public void onEnable() {
        instance = this;

        // ИСПРАВЛЕНИЕ: Передаем 'this' (экземпляр плагина) в конструктор
        disguiseManager = new DisguiseManager(this);

        getServer().getPluginManager().registerEvents(
                new TransformListener(disguiseManager), this);

        getLogger().info("HideAndSeek plugin enabled");
    }

    public static HideAndSeekPlugin getInstance() {
        return instance;
    }

    // Хорошей практикой будет добавить геттер для менеджера
    public DisguiseManager getDisguiseManager() {
        return disguiseManager;
    }
}