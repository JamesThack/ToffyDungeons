package toffydungeons.toffydungeons.CurrentEvents;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import toffydungeons.toffydungeons.API.BoundingBox;
import toffydungeons.toffydungeons.API.FileSaving;

import java.io.File;

public class ConstantEvents implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (playerInDungeon(e.getPlayer())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§c[Toffy Dungeons]: No breaking blocks in a dungeon!");
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (playerInDungeon(e.getPlayer())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§c[Toffy Dungeons]: No placing blocks in a dungeon!");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (playerInDungeon(e.getPlayer())) {
            int[] directions = getRoomLocation(e.getPlayer());
            System.out.println(directions[0] + "," + directions[1] + "," + directions[2]);
        }
    }

    private boolean playerInDungeon(Player player) {
        if (!FileSaving.folderContainsFile("active_dungeons", "noread.check")) {
            for (String fileCur : FileSaving.filesInDirectory("active_dungeons")) {
                for (String curLine : FileSaving.readLines("active_dungeons" + File.separator + fileCur)) {
                    if (curLine.split(",").length == 11) {
                        String[] splitLine = curLine.split(",");
                        Location loc1 = new Location(Bukkit.getWorld(splitLine[1]), Integer.valueOf(splitLine[5]), Integer.valueOf(splitLine[6]), Integer.valueOf(splitLine[7]));
                        Location loc2 = new Location(Bukkit.getWorld(splitLine[1]), Integer.valueOf(splitLine[8]), Integer.valueOf(splitLine[9]), Integer.valueOf(splitLine[10]));
                        BoundingBox box = new BoundingBox(loc1, loc2);
                        if (box.entityInBox(player)) {
                            return true;
                        }
                    }
                }
            }
        } return false;
    }

    private int[] getRoomLocation(Player player) {
        for (String fileCur : FileSaving.filesInDirectory("active_dungeons")) {
            for (String curLine : FileSaving.readLines("active_dungeons" + File.separator + fileCur)) {
                if (curLine.split(",").length == 11) {
                    String[] splitLine = curLine.split(",");
                    Location loc1 = new Location(Bukkit.getWorld(splitLine[1]), Integer.valueOf(splitLine[5]), Integer.valueOf(splitLine[6]), Integer.valueOf(splitLine[7]));
                    Location loc2 = new Location(Bukkit.getWorld(splitLine[1]), Integer.valueOf(splitLine[8]), Integer.valueOf(splitLine[9]), Integer.valueOf(splitLine[10]));
                    BoundingBox box = new BoundingBox(loc1, loc2);
                    if (box.entityInBox(player)) {
                        if (getDirection(loc1, loc2).equals("posx")) {
                            return new int[]{Integer.valueOf(splitLine[4]) - player.getLocation().getBlockZ(), player.getLocation().getBlockY() - Integer.valueOf(splitLine[3]), player.getLocation().getBlockX() - Integer.valueOf(splitLine[2])};
                        } else if (getDirection(loc1, loc2).equals("negx")) {
                            return new int[]{player.getLocation().getBlockZ() - Integer.valueOf(splitLine[4]), player.getLocation().getBlockY() - Integer.valueOf(splitLine[3]), Integer.valueOf(splitLine[2]) - player.getLocation().getBlockX()};
                        } else if (getDirection(loc1, loc2).equals("posz")) {
                            return new int[]{player.getLocation().getBlockX() - Integer.valueOf(splitLine[2]), player.getLocation().getBlockY() - Integer.valueOf(splitLine[3]), player.getLocation().getBlockZ() - Integer.valueOf(splitLine[4])};
                        } else if (getDirection(loc1, loc2).equals("negz")) {
                            return new int[]{Integer.valueOf(splitLine[2]) - player.getLocation().getBlockX(), player.getLocation().getBlockY() - Integer.valueOf(splitLine[3]), Integer.valueOf(splitLine[4]) -  player.getLocation().getBlockZ()};
                        }
                    }
                }
            }
        } return new int[]{0,0,0};
    }

    public static String getDirection(Location loc1, Location loc2) {
        if (loc1.getBlockX() < loc2.getBlockX() && loc1.getBlockZ() < loc2.getBlockZ()) {
            return "posx";
        }
        if (loc1.getBlockX() > loc2.getBlockX() && loc1.getBlockZ() < loc2.getBlockZ()) {
            return "posz";
        }
        if (loc1.getBlockX() > loc2.getBlockX() && loc1.getBlockZ() > loc2.getBlockZ()) {
            return "negx";
        }
        if (loc1.getBlockX() < loc2.getBlockX() && loc1.getBlockZ() > loc2.getBlockZ()) {
            return "negz";
        } else {
            return "null";
        }
    }

}
