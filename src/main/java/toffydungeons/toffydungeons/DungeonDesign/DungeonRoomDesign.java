package toffydungeons.toffydungeons.DungeonDesign;

import com.sk89q.worldedit.Vector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import toffydungeons.toffydungeons.API.CalebWorldEditAPI;
import toffydungeons.toffydungeons.API.FileSaving;
import toffydungeons.toffydungeons.GUIs.DungeonTraps.PlaceTrapConstant;

import java.io.File;
import java.util.ArrayList;

public class DungeonRoomDesign {

    /**
     * The individual editor for each player (should only be one per player)
     */

    private String playerUUID;
    private Location origin;
    private Location endPoint;
    private String name;
    private int currentOperation;
    private boolean editing;
    private Location northDoor;
    private Location eastDoor;
    private Location southDoor;
    private Location westDoor;
    private ArrayList<String> additionalData;
    private PlaceTrapConstant constant;

    public DungeonRoomDesign(Player player) {
        this.playerUUID = player.getUniqueId().toString();
        this.name= "UNNAMED";
        this.additionalData = new ArrayList<String>();
        this.editing = false;
        this.currentOperation = 0;
    }

    public DungeonRoomDesign(Player player, String editName) {
        this.playerUUID = player.getUniqueId().toString();
        this.name = editName;
        this.additionalData = new ArrayList<>();
        this.editing = true;
        this.currentOperation = 0;
        this.southDoor = new Location(player.getWorld(),  player.getLocation().getBlockX(), player.getLocation().getBlockY(),  player.getLocation().getBlockZ());
        try {
            for (String line : FileSaving.readLines("rooms" + File.separator + editName + ".placement")) {
                if (line.contains("NORTH:")) {
                    String linex = line.replace("NORTH:", "");
                    this.northDoor = new Location(this.southDoor.getWorld(), (int) this.southDoor.getBlockX() + Integer.valueOf(linex.split(",")[0]), (int)this.southDoor.getBlockY() + Integer.valueOf(linex.split(",")[1]), (int)this.southDoor.getBlockZ() + Integer.valueOf(linex.split(",")[2]));
                } else if (line.contains("EAST:")) {
                    String linex = line.replace("EAST:", "");
                    this.eastDoor = new Location(this.southDoor.getWorld(), (int)this.southDoor.getBlockX() + Integer.valueOf(linex.split(",")[0]), (int)this.southDoor.getBlockY() + Integer.valueOf(linex.split(",")[1]), (int)this.southDoor.getBlockZ() + Integer.valueOf(linex.split(",")[2]));
                } else if  (line.contains("SOUTH")) {
                    String linex = line.replace("SOUTH:", "");
                    this.origin = new Location(this.southDoor.getWorld(), (int)this.southDoor.getBlockX() - Integer.valueOf(linex.split(",")[0]), (int)this.southDoor.getBlockY() - Integer.valueOf(linex.split(",")[1]), (int)this.southDoor.getBlockZ() - Integer.valueOf(linex.split(",")[2]));
                    System.out.println(this.origin.getBlockX() + "," + this.origin.getBlockY() + "," + this.origin.getBlockZ());
                    System.out.println(this.southDoor.getBlockX() + "," + this.southDoor.getBlockY() + "," +  this.southDoor.getBlockZ());
                } else if  (line.contains("WEST:")) {
                    String linex = line.replace("WEST:", "");
                    this.westDoor = new Location(this.southDoor.getWorld(), (int)this.southDoor.getBlockX() + Integer.valueOf(linex.split(",")[0]), (int)this.southDoor.getBlockY() + Integer.valueOf(linex.split(",")[1]), (int)this.southDoor.getBlockZ() + Integer.valueOf(linex.split(",")[2]));
                } else if  (line.contains("BORDER:")) {
                    String linex = line.replace("BORDER:", "");
                    this.endPoint = new Location(this.southDoor.getWorld(), (int)this.southDoor.getBlockX() + Integer.valueOf(linex.split(",")[0]), (int)this.southDoor.getBlockY() + Integer.valueOf(linex.split(",")[1]), (int)this.southDoor.getBlockZ() + Integer.valueOf(linex.split(",")[2]));
                    System.out.println(endPoint.getBlockX() + "," + endPoint.getBlockY() + "," + endPoint.getBlockZ());
                } else {
                    this.additionalData.add(line);
                }
            }
            CalebWorldEditAPI.tryLoadSchem(editName, player.getLocation(), new Vector(0, 0, 0));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage("[Toffy Dungeons]: An error was found when attempting to load the room! Please send the .placement file to a developer if unsure.");
        }
    }

    public static String calculateCoords(String fileName, Location southDoor, String direction) {
        String reurnLine = "";
        for (String line : FileSaving.readLines("rooms" + File.separator + fileName + ".placement")) {
             if  (line.contains("SOUTH")) {
                String linex = line.replace("SOUTH:", "");
                Location getLoc = southDoor;
                if (direction.equals("null") || direction.equals("forward")) {
                    System.out.println("Here");
                    getLoc = new Location(southDoor.getWorld(), (int) southDoor.getBlockX() - Integer.valueOf(linex.split(",")[0]), (int) southDoor.getBlockY() - Integer.valueOf(linex.split(",")[1]), (int) southDoor.getBlockZ() - Integer.valueOf(linex.split(",")[2]));
                } else if (direction.equals("left")) {
                    getLoc = new Location(southDoor.getWorld(), (int) southDoor.getBlockX() - Integer.valueOf(linex.split(",")[2]), (int) southDoor.getBlockY() - Integer.valueOf(linex.split(",")[1]), (int) southDoor.getBlockZ() + Integer.valueOf(linex.split(",")[0]));
                } else if (direction.equals("right")) {
                    getLoc = new Location(southDoor.getWorld(), (int) southDoor.getBlockX() + Integer.valueOf(linex.split(",")[2]), (int) southDoor.getBlockY() - Integer.valueOf(linex.split(",")[1]), (int) southDoor.getBlockZ() - Integer.valueOf(linex.split(",")[0]));
                } else if (direction.equals("behind")) {
                    getLoc = new Location(southDoor.getWorld(), (int) southDoor.getBlockX() + Integer.valueOf(linex.split(",")[0]), (int) southDoor.getBlockY() - Integer.valueOf(linex.split(",")[1]), (int) southDoor.getBlockZ() + Integer.valueOf(linex.split(",")[2]));
                }
                reurnLine += ("," + getLoc.getBlockX() + "," + getLoc.getBlockY() + "," + getLoc.getBlockZ());
             } else if  (line.contains("BORDER:")) {
                String linex = line.replace("BORDER:", "");
                 Location getLoc = southDoor;
                 if (direction.equals("null") || direction.equals("forward")) {
                     System.out.println("Here");
                     getLoc = new Location(southDoor.getWorld(), (int) southDoor.getBlockX() + Integer.valueOf(linex.split(",")[0]), (int) southDoor.getBlockY() + Integer.valueOf(linex.split(",")[1]), (int) southDoor.getBlockZ() + Integer.valueOf(linex.split(",")[2]));
                 } else if (direction.equals("left")) {
                     getLoc = new Location(southDoor.getWorld(), (int) southDoor.getBlockX() + Integer.valueOf(linex.split(",")[2]), (int) southDoor.getBlockY() + Integer.valueOf(linex.split(",")[1]), (int) southDoor.getBlockZ() - Integer.valueOf(linex.split(",")[0]));
                 } else if (direction.equals("right")) {
                     getLoc = new Location(southDoor.getWorld(), (int) southDoor.getBlockX() - Integer.valueOf(linex.split(",")[2]), (int) southDoor.getBlockY() + Integer.valueOf(linex.split(",")[1]), (int) southDoor.getBlockZ() + Integer.valueOf(linex.split(",")[0]));
                 } else if (direction.equals("behind")) {
                     getLoc = new Location(southDoor.getWorld(), (int) southDoor.getBlockX() - Integer.valueOf(linex.split(",")[0]), (int) southDoor.getBlockY() + Integer.valueOf(linex.split(",")[1]), (int) southDoor.getBlockZ() - Integer.valueOf(linex.split(",")[2]));
                 }
                 reurnLine += ("," + getLoc.getBlockX() + "," + getLoc.getBlockY() + "," + getLoc.getBlockZ());
            }
        }
        return reurnLine;
    }

    public boolean isEditing() {
        return editing;
    }

    public boolean editorContainsPlayer(Player player) {
        return player.getUniqueId().toString().equals(playerUUID);
    }

    public Location getOrigin() {
        return origin;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public Location getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Location endPoint) {
        this.endPoint = endPoint;
    }

    public boolean save() {
        FileSaving.saveFile("rooms", "rooms" + File.separator + this.name + ".placement");
        ArrayList<String> saveData = new ArrayList<>();
        Location loc1 = new Location(origin.getWorld(), Math.min(origin.getBlockX(), endPoint.getBlockX()), Math.min(origin.getBlockY(), endPoint.getBlockY()), Math.min(origin.getBlockZ(), endPoint.getBlockZ()));
        Location loc2 = new Location(origin.getWorld(), Math.max(origin.getBlockX(), endPoint.getBlockX()), Math.max(origin.getBlockY(), endPoint.getBlockY()), Math.max(origin.getBlockZ(), endPoint.getBlockZ()));
        this.safeDoors();
        saveData.add("NORTH:" + (int)(northDoor.getBlockX() - southDoor.getBlockX()) + "," +  (int)(northDoor.getBlockY() - southDoor.getBlockY()) + "," +  (int)(northDoor.getBlockZ() - southDoor.getBlockZ()));
        saveData.add("EAST:" + (int)(eastDoor.getBlockX() - southDoor.getBlockX()) + "," + (int)(eastDoor.getBlockY() - southDoor.getBlockY()) + "," + (int)(eastDoor.getBlockZ() - southDoor.getBlockZ()));
        saveData.add("SOUTH:" + (int)(southDoor.getBlockX() - loc1.getBlockX()) + "," + (int)(southDoor.getBlockY() - loc1.getBlockY()) + "," + (int)(southDoor.getBlockZ() - loc1.getBlockZ()));
        saveData.add("WEST:" + (int)(westDoor.getBlockX() - southDoor.getBlockX()) + "," + (int)(westDoor.getBlockY() -  southDoor.getBlockY()) + "," + (int)(westDoor.getBlockZ() - southDoor.getBlockZ()));
        saveData.add("BORDER:" + (int)(loc2.getBlockX() - southDoor.getBlockX()) + "," + (int)(loc2.getBlockY() -  southDoor.getBlockY()) + "," + (int)(loc2.getBlockZ() - southDoor.getBlockZ()));
        saveData.addAll(additionalData);
        FileSaving.writeFile("rooms" + File.separator + this.name + ".placement", saveData);
        CalebWorldEditAPI.trySaveSchem(loc1, loc2, this.name, new Vector(loc1.getBlockX() - this.southDoor.getBlockX(), loc1.getBlockY() - this.southDoor.getBlockY(),  loc1.getBlockZ() - this.southDoor.getBlockZ()));
        CalebWorldEditAPI.setBlock(loc1, loc2, Material.AIR);
        return true;
    }

    public void setCurrentOperation(int operation) {
        this.currentOperation = operation;
    }

    public int  getCurrentOperation() {
        return this.currentOperation;
    }

    public void setDoor( Location newLoc) {
        switch(this.currentOperation) {
            case(1):
                this.northDoor = newLoc;
                break;
            case(2):
                this.eastDoor = newLoc;
                break;
            case(4):
                this.southDoor = newLoc;
                break;
            case(3):
                this.westDoor = newLoc;
                break;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getSouthDoor() {
        return southDoor;
    }

    public void safeDoors() {
        if (this.northDoor == null)
            this.northDoor = this.southDoor;
        if (this.eastDoor == null)
            this.eastDoor = this.southDoor;
        if (this.westDoor == null)
            this.westDoor = this.southDoor;

    }

    public PlaceTrapConstant getConstant() {
        return constant;
    }

    public void setConstant(PlaceTrapConstant constant) {
        this.constant = constant;
    }

    public ArrayList<String> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(ArrayList<String> additionalData) {
        this.additionalData = additionalData;
    }
}
