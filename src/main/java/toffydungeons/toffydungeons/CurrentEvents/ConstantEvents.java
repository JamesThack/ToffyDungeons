package toffydungeons.toffydungeons.CurrentEvents;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import toffydungeons.toffydungeons.API.BoundingBox;
import toffydungeons.toffydungeons.API.FileSaving;

import java.io.File;
import java.util.ArrayList;

public class ConstantEvents implements Listener {

    /**
     * This is the events class that constantly checks for player information related to a dungeon (are they in one?
     * where are they in a dungeon?)
     */

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
//            System.out.println(directions[0] + "," + directions[1] + "," + directions[2]);
            if (!FileSaving.folderContainsFile("active_dungeons", "noread.check")) {
                String currentDun = getCurrentRoom(e.getPlayer());
                ArrayList<String> lines = FileSaving.readLines("rooms" + File.separator + currentDun + ".placement");
                for (int x = 0; x < lines.size(); x++) {
                    if (lines.get(x).contains(".trap")) {
                        String pos1 = (lines.get(x + 1).replace("REG1:", ""));
                        int[] trapCorner1 = new int[]{Integer.valueOf(pos1.split(",")[0]), Integer.valueOf(pos1.split(",")[1]), Integer.valueOf(pos1.split(",")[2])};
//                        System.out.println(trapCorner1[0] + "," + trapCorner1[1] + "," + trapCorner1[2]);
                        String pos2 = (lines.get(x + 2).replace("REG2:", ""));
                        int[] trapCorner2 = new int[]{Integer.valueOf(pos2.split(",")[0]), Integer.valueOf(pos2.split(",")[1]), Integer.valueOf(pos2.split(",")[2])};
//                        System.out.println(trapCorner2[0] + "," + trapCorner2[1] + "," + trapCorner2[2]);
                        if ((directions[0] >= Math.min(trapCorner1[0], trapCorner2[0]) && directions[1] >= Math.min(trapCorner1[1], trapCorner2[1]) && directions[2] >= Math.min(trapCorner1[2], trapCorner2[2]))
                        && (directions[0] <= Math.max(trapCorner1[0], trapCorner2[0]) && directions[1] <= Math.max(trapCorner1[1], trapCorner2[1]) && directions[2] <= Math.max(trapCorner1[2], trapCorner2[2]) )  ) {
                            String pos3 = (lines.get(x + 3).replace("HAPPEN:", ""));
                            int[] spawnCorner = new int[]{Integer.valueOf(pos3.split(",")[0]), Integer.valueOf(pos3.split(",")[1]), Integer.valueOf(pos3.split(",")[2])};
                            e.getPlayer().getWorld().getBlockAt(locFromCoords(e.getPlayer(), spawnCorner)).setType(Material.STONE);
//                            MobTrap trap = new MobTrap(lines.get(x).replace(".trap", ""));
//                            EntityType type = EntityType.valueOf(trap.getMobType());
//                            if (e.getPlayer().getWorld().getNearbyEntities(locFromCoords(e.getPlayer(), spawnCorner), 1,1,1).size() < 1) {
//                                LivingEntity mob = (LivingEntity) e.getPlayer().getWorld().spawnEntity(locFromCoords(e.getPlayer(), spawnCorner), type);
//                                mob.setMaxHealth(trap.getHealth());
//                                mob.setHealth(trap.getHealth());
//                                if (trap.getMobName() != null) {
//                                    mob.setCustomName(trap.getMobName());
//                                    mob.setCustomNameVisible(true);
//                                }
                            }
                        }
                    }
                }
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

    private String getCurrentRoom(Player player) {
        if (!FileSaving.folderContainsFile("active_dungeons", "noread.check")) {
            for (String fileCur : FileSaving.filesInDirectory("active_dungeons")) {
                for (String curLine : FileSaving.readLines("active_dungeons" + File.separator + fileCur)) {
                    if (curLine.split(",").length == 11) {
                        String[] splitLine = curLine.split(",");
                        Location loc1 = new Location(Bukkit.getWorld(splitLine[1]), Integer.valueOf(splitLine[5]), Integer.valueOf(splitLine[6]), Integer.valueOf(splitLine[7]));
                        Location loc2 = new Location(Bukkit.getWorld(splitLine[1]), Integer.valueOf(splitLine[8]), Integer.valueOf(splitLine[9]), Integer.valueOf(splitLine[10]));
                        BoundingBox box = new BoundingBox(loc1, loc2);
                        if (box.entityInBox(player)) {
                            return(curLine.split(",")[0]);
                        }
                    }
                }
            }
        } return "";
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

    private Location locFromCoords(Player player, int[] coords) {
        for (String fileCur : FileSaving.filesInDirectory("active_dungeons")) {
            for (String curLine : FileSaving.readLines("active_dungeons" + File.separator + fileCur)) {
                if (curLine.split(",").length == 11) {
                    String[] splitLine = curLine.split(",");
                    Location loc1 = new Location(Bukkit.getWorld(splitLine[1]), Integer.valueOf(splitLine[5]), Integer.valueOf(splitLine[6]), Integer.valueOf(splitLine[7]));
                    Location loc2 = new Location(Bukkit.getWorld(splitLine[1]), Integer.valueOf(splitLine[8]), Integer.valueOf(splitLine[9]), Integer.valueOf(splitLine[10]));
                    Location spawnLoc = new Location(Bukkit.getWorld(splitLine[1]), Integer.valueOf(splitLine[2]), Integer.valueOf(splitLine[3]), Integer.valueOf(splitLine[4]));
                    BoundingBox box = new BoundingBox(loc1, loc2);
                    if (box.entityInBox(player)) {
                        if (getDirection(loc1, loc2).equals("posx")) {
                            return spawnLoc.add(coords[2], coords[1], -coords[0]);
                        } else if (getDirection(loc1, loc2).equals("negx")) {
                            return spawnLoc.add(-coords[2], coords[1], coords[0]);
                        } else if (getDirection(loc1, loc2).equals("posz")) {
                            return spawnLoc.add(coords[0], coords[1], coords[2]);
                            //return new int[]{player.getLocation().getBlockX() - Integer.valueOf(splitLine[2]), player.getLocation().getBlockY() - Integer.valueOf(splitLine[3]), player.getLocation().getBlockZ() - Integer.valueOf(splitLine[4])};
                        } else if (getDirection(loc1, loc2).equals("negz")) {
                            return spawnLoc.add(-coords[0], coords[1], -coords[2]);
                           // return new int[]{Integer.valueOf(splitLine[2]) - player.getLocation().getBlockX(), player.getLocation().getBlockY() - Integer.valueOf(splitLine[3]), Integer.valueOf(splitLine[4]) -  player.getLocation().getBlockZ()};
                        }

                    }
                }
            }
        } return null;
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
