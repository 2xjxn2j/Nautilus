package org.jacob.spigot.plugins.nautilus.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jacob.spigot.plugins.nautilus.Nautilus;

import java.io.File;

public class FileCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(label.equalsIgnoreCase("files")) {
            if(!(sender instanceof Player)){

                sender.sendMessage(RED + "You must be a player");
                return true;
            }
            Player p = (Player) sender;


            File folder = Nautilus.getInstance().getDataFolder().getParentFile().getAbsoluteFile();

            Nautilus.openDirectoryInventory(folder, p);
        }
        return true;
    }
}
