package org.jacob.spigot.plugins.nautilus.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jacob.spigot.plugins.nautilus.Nautilus;

import java.io.*;
import java.util.Scanner;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Player p = (Player) event.getWhoClicked();

        if (Nautilus.players.containsKey(p.getUniqueId())) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null) {
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().getType() == Material.BARRIER) {

                try {

                    File file = new File(Nautilus.players.get(p.getUniqueId()));

                    Nautilus.players.put(p.getUniqueId(), file.getParentFile().getName());

                    Nautilus.openDirectoryInventory(file.getParentFile(), p);
                } catch (NullPointerException e) {
                    return;
                }
                return;
            }

            if (event.getCurrentItem().getType() == Material.ORANGE_WOOL) {
                String folder = event.getCurrentItem().getItemMeta().getDisplayName();

                Nautilus.players.put(p.getUniqueId(), Nautilus.players.get(p.getUniqueId()) + "/" + folder);

                File file = new File(Nautilus.players.get(p.getUniqueId()));
                Nautilus.openDirectoryInventory(file, p);
                return;
            }
            p.closeInventory();


            event.setCancelled(true);
            File file = new File(Nautilus.players.get(p.getUniqueId()) + "/" + event.getCurrentItem().getItemMeta().getDisplayName());

            if (file.getName().contains(".sh")) {

                try {

                    p.sendMessage(ChatColor.YELLOW + "Executing Shell: " + ChatColor.GOLD + file.getName());

                    p.sendMessage(ChatColor.YELLOW + "Output: ");

                    String[] command = {"sh", file.getAbsolutePath()};

                    Process process = Runtime.getRuntime().exec(command);

                    process.waitFor();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        p.sendMessage(line);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (file.getName().contains(".jar")) {

                p.sendMessage(ChatColor.YELLOW + "Executing Jar: " + ChatColor.GOLD + file.getName());

                ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath(), " -jar", file.getName());
                pb.directory(file.getParentFile());

                Process proc = pb.start();

                try {
                    proc.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                OutputStreamWriter out = new OutputStreamWriter(proc.getOutputStream());
                boolean cont = true;
                BufferedReader consolein = new BufferedReader(new InputStreamReader(System.in));

                p.sendMessage(ChatColor.YELLOW + "Output: ");

                while (cont) {
                    String temp = consolein.readLine();
                    out.write(temp);
                    p.sendMessage(in.readLine());
                }

                return;
            }

            if (file.getName().contains(".txt") || file.getName().contains(".yml")) {
                String content = "";

                Nautilus.players.remove(p.getUniqueId());

                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    content += reader.nextLine() + " ";
                }

                p.sendMessage(ChatColor.YELLOW + "Now reading: " + ChatColor.GOLD + file.getName());
                p.sendMessage(ChatColor.YELLOW + "Content: ");
                p.sendMessage(content);
                event.setCancelled(true);
                return;
            }

            p.closeInventory();

        }
    }
}
