package me.nikita.hidenseek;

import org.bukkit.plugin.java.JavaPlugin;

public final class HideAndSeekPlugin extends JavaPlugin {

    private GameManager gameManager;
    private BlockDisguiseManager disguiseManager;

    @Override
    public void onEnable() {
        disguiseManager = new BlockDisguiseManager(this);
        gameManager = new GameManager(this, disguiseManager);

        getCommand("has").setExecutor(new GameCommand(gameManager));
        getServer().getPluginManager().registerEvents(new PlayerListener(gameManager, disguiseManager), this);

        getLogger().info("HideAndSeekPlugin включен!");
    }

    @Override
    public void onDisable() {
        gameManager.stopGame();
        getLogger().info("HideAndSeekPlugin выключен!");
    }
}