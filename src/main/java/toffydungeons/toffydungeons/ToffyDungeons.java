package toffydungeons.toffydungeons;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import toffydungeons.toffydungeons.CurrentEvents.ConstantEvents;
import toffydungeons.toffydungeons.DungeonDesign.DungeonDesignEvents;
import toffydungeons.toffydungeons.GUIs.DungeonLayout.GenerationEvents;
import toffydungeons.toffydungeons.GUIs.DungeonMainMenu;
import toffydungeons.toffydungeons.DungeonTraps.DungeonTrapType;
import toffydungeons.toffydungeons.DungeonTraps.TrapEvents;
import toffydungeons.toffydungeons.GUIs.InventoryEvents;
import toffydungeons.toffydungeons.GUIs.ProceduralGeneration.ProceduralEvents;
import toffydungeons.toffydungeons.commands.TDungeonCommand;

public final class ToffyDungeons extends JavaPlugin {

    /**
     * This is all of the code that runs at the start of the plugin.
     */
    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
            System.out.println("[Toffy Dungeons]: §cFATAL ERROR: WORLD EDIT NOT INSTALLED, PLEASE INSTALL WORLD EDIT TO USE THE PLUGIN");
        } else {
            System.out.println("[Toffy Dungeons]: Toffy Dungeons version " + this.getServer().getVersion() +" loaded up! ");

            DungeonDesignEvents designEvents = new DungeonDesignEvents();

            getServer().getPluginManager().registerEvents(new GenerationEvents(), this);
            getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
            getServer().getPluginManager().registerEvents(new DungeonMainMenu(), this);
            getServer().getPluginManager().registerEvents(new ConstantEvents(), this);
            getServer().getPluginManager().registerEvents(new DungeonTrapType(), this);
            getServer().getPluginManager().registerEvents(new TrapEvents(), this);
            getServer().getPluginManager().registerEvents(new ProceduralEvents(), this);
            getServer().getPluginManager().registerEvents(designEvents, this);

            this.getCommand("TDungeon").setExecutor(new TDungeonCommand(designEvents));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
