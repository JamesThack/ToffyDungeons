package toffydungeons.toffydungeons;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToffyDungeons extends JavaPlugin {

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
            System.out.println("[Toffy Dungeons] §cFATAL ERROR: WORLD EDIT NOT INSTALLED, PLEASE INSTALL WORLD EDIT TO USE THE PLUGIN");
        } else {
            System.out.println("[Toffy Dungeons] Toffy Dungeons version " + this.getServer().getVersion() +" loaded up! ");
        }


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
