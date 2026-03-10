package me.nikita.hideandseek.commands;

import me.nikita.hideandseek.disguise.DisguiseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AdminCommand implements CommandExecutor {

    private final DisguiseManager disguiseManager;

    public AdminCommand(DisguiseManager disguiseManager) {
        this.disguiseManager = disguiseManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("has.admin")) {
            sender.sendMessage("§cУ вас нет прав!");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("clear")) {
            disguiseManager.clearAllDisguises();
            sender.sendMessage("§a[!] Все маскировки были принудительно удалены.");
            return true;
        }

        sender.sendMessage("§eИспользуйте: /has clear");
        return true;
    }
}