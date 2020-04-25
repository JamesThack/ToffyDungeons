package toffydungeons.toffydungeons.API;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.FileOutputStream;

public class CalebWorldEditAPI {

    /**
     * This is a static method to make loading world edit schematics easier.
     * @param fileName A string of the exact file name, do not bother with file extension (ExampleRoom not ExampleRoom.schematic)
     * @param location The location to paste
     * @return If the loading was successful
     */
    public static boolean tryLoadSchem(String fileName, Location location, Vector offset) {
        return tryLoadSchem(fileName, location, 0, offset);
    }

    public static void setBlock(Location loc1, Location loc2, Material block) {
        try {
            WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
            EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(loc1.getWorld()), 10000);
            World weWorld = new BukkitWorld(loc1.getWorld());
            Vector pos1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ()); //First corner of your cuboid
            Vector pos2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ()); //Second corner fo your cuboid
            CuboidRegion region = new CuboidRegion(weWorld, pos1, pos2);
            session.setBlocks(region, new BaseBlock(block.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean tryLoadSchem(String fileName, Location location, int rotation, Vector offset) {
        try {
            WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
            File schematic = new File(Bukkit.getPluginManager().getPlugin("ToffyDungeons").getDataFolder() + File.separator + "rooms" + File.separator + fileName + ".schematic");
            EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 10000);
            session.enableQueue();
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
            clipboard.rotate2D(rotation);
            clipboard.paste(session, new Vector(location.getX(), location.getY(), location.getZ())   , true );
            session.disableQueue();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean changeOffset(String fileName, Vector offset) {
        try {
            File schematic = new File(Bukkit.getPluginManager().getPlugin("ToffyDungeons").getDataFolder() + File.separator + "rooms" + File.separator + fileName + ".schematic");
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
            clipboard.setOffset(offset);
            clipboard.saveSchematic(schematic);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean trySaveSchem(Location loc1, Location loc2, String fileName, Vector offset) {
        File schematic = new File(Bukkit.getPluginManager().getPlugin("ToffyDungeons").getDataFolder() + File.separator + "rooms" + File.separator + fileName + ".schematic");
        try {
            World weWorld = new BukkitWorld(loc1.getWorld());
            WorldData worldData = weWorld.getWorldData();
            Vector pos1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ()); //First corner of your cuboid
            Vector pos2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ()); //Second corner fo your cuboid
            CuboidRegion cReg = new CuboidRegion(weWorld, pos1, pos2);
            BlockArrayClipboard clipboard = new BlockArrayClipboard(cReg);
            Extent source = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
            Extent destination = clipboard;
            ForwardExtentCopy copy = new ForwardExtentCopy(source, cReg, clipboard.getOrigin(), destination, pos1);
            copy.setSourceMask(new ExistingBlockMask(source));
            Operations.completeLegacy(copy);
            FileOutputStream stream = new FileOutputStream(schematic);
            ClipboardWriter format =  ClipboardFormat.SCHEMATIC.getWriter(stream);
            format.write(clipboard, worldData);
            format.close();
            stream.close();

            changeOffset(fileName, offset);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static int getWidth(String fileName) {
        try {
            File schematic = new File(Bukkit.getPluginManager().getPlugin("ToffyDungeons").getDataFolder() + File.separator + "rooms" + File.separator + fileName + ".schematic");
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
            return clipboard.getWidth();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getLength(String fileName) {
        try {
            File schematic = new File(Bukkit.getPluginManager().getPlugin("ToffyDungeons").getDataFolder() + File.separator + "rooms" + File.separator + fileName + ".schematic");
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
            return clipboard.getLength();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
