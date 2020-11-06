package org.jacob.spigot.plugins.nautilus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacob.spigot.plugins.nautilus.commands.FileCommand;
import org.jacob.spigot.plugins.nautilus.listeners.InventoryClickListener;

import java.io.File;
import java.util.*;

public final class Nautilus extends JavaPlugin {
    private static Nautilus instance;

    public static Nautilus getInstance() {
        return instance;
    }

    public static Map<UUID, String> players = new HashMap<UUID, String>();

    public static List<String> text = new ArrayList<>();

    public List<String> execute = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        text.add(".yml");
        text.add(".txt");

        execute.add(".jar");

        //Registering commands
        getCommand("files").setExecutor(new FileCommand());

        //Registering listeners
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InventoryClickListener(), this);

    }

    public static void openDirectoryInventory(File folder, Player player) {

        Inventory inv = Bukkit.createInventory(null, 54, folder.getPath());

        for(File file : Objects.requireNonNull(folder.listFiles())) {

            if(file.isDirectory()) {
                ItemStack stack = new ItemStack(Material.ORANGE_WOOL, 1);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName(file.getName());

                stack.setItemMeta(meta);
                //what does it look like?
                inv.addItem(stack);

            } else if(file.getName().contains(".txt") || file.getName().contains(".yml")) {
                ItemStack stack = new ItemStack(Material.GREEN_WOOL, 1);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName(file.getName());

                stack.setItemMeta(meta);

                inv.addItem(stack);

            } else if(file.getName().contains(".sh")) {
                ItemStack stack = new ItemStack(Material.BLUE_WOOL, 1);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName(file.getName());

                stack.setItemMeta(meta);

                inv.addItem(stack);

            } else {
                ItemStack stack = new ItemStack(Material.LIGHT_GRAY_WOOL, 1);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName(file.getName());

                stack.setItemMeta(meta);

                inv.addItem(stack);
            }
        }
//
        ItemStack back = new ItemStack(Material.BARRIER);

        ItemMeta meta = back.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Previous directory");

        back.setItemMeta(meta);

        inv.setItem(53, back);

        players.put(player.getUniqueId(), folder.getAbsolutePath());

        player.openInventory(inv);

    }
}