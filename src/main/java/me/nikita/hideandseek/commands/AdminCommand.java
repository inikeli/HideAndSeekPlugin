package me.nikita.hideandseek.commands;

import me.nikita.hideandseek.disguise.DisguiseManager;
import me.nikita.hideandseek.item.MagicStick;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
        if (args.length>0 && args[0].equalsIgnoreCase("help")){
            sender.sendMessage("§eИспользуйту /has clear для очистки сущностией, /has get - для выдачи палочки превращения себе, /has giveall - для выдачи палочки превращения всем. Фри платина");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("clear")) {
            disguiseManager.clearAllDisguises();
            sender.sendMessage("§a[!] Все маскировки были принудительно удалены.");
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("get")){
            if (sender instanceof Player player){
                player.getInventory().addItem(MagicStick.create());
                player.sendMessage("§eВы получиди палочку");

            } else {
                sender.sendMessage("§eВы не являетесь игроком");
            }
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("getall")){
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
                onlinePlayer.getInventory().addItem(MagicStick.create());
                onlinePlayer.sendMessage("§eВам выдана палочка");
                return true;
            }
        }
        return true;
    }
}