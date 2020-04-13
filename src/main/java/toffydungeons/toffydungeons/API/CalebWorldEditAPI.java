package toffydungeons.toffydungeons.API;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;

public class CalebWorldEditAPI {

    /**
     * This is a static method to make loading world edit schematics easier.
     * @param fileName A string of the exact file name, do not bother with file extension (ExampleRoom not ExampleRoom.schematic)
     * @param location The location to paste
     * @return If the loading was successful
     */
    public static boolean tryLoadSchem(String fileName, Location location) {
        return tryLoadSchem(fileName, location, 0);
    }

    public static boolean tryLoadSchem(String fileName, Location location, int rotation) {
        try {
            WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
            File schematic = new File(Bukkit.getPluginManager().getPlugin("ToffyDungeons").getDataFolder() + File.separator + "schematics" + File.separator + fileName + ".schematic");
            EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 10000);
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
            clipboard.rotate2D(rotation);
            clipboard.paste(session, new Vector(location.getX(), location.getY(), location.getZ())   , true );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
