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
        this.additionalData = new ArrayList<String>();
        this.editing = true;
        this.currentOperation = 0;
        this.southDoor = new Location(player.getWorld(), (int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ());
        try {
            for (String line : FileSaving.readLines("rooms" + File.separator + editName + ".placement")) {
                if (line.contains("NORTH:")) {
                    String linex = line.replace("NORTH:", "");
                    this.northDoor = new Location(this.southDoor.getWorld(), (int) this.southDoor.getX() + Integer.valueOf(linex.split(",")[0]), (int)this.southDoor.getY() + Integer.valueOf(linex.split(",")[1]), (int)this.southDoor.getZ() + Integer.valueOf(linex.split(",")[2]));
                } else if (line.contains("EAST:")) {
                    String linex = line.replace("EAST:", "");
                    this.eastDoor = new Location(this.southDoor.getWorld(), (int)this.southDoor.getX() + Integer.valueOf(linex.split(",")[0]), (int)this.southDoor.getY() + Integer.valueOf(linex.split(",")[1]), (int)this.southDoor.getZ() + Integer.valueOf(linex.split(",")[2]));
                } else if  (line.contains("SOUTH")) {
                    String linex = line.replace("SOUTH:", "");
                    this.origin = new Location(this.southDoor.getWorld(), (int)this.southDoor.getX() - Integer.valueOf(linex.split(",")[0]), (int)this.southDoor.getY() - Integer.valueOf(linex.split(",")[1]), (int)this.southDoor.getZ() - Integer.valueOf(linex.split(",")[2]));
                } else if  (line.contains("WEST:")) {
                    String linex = line.replace("WEST:", "");
                    this.westDoor = new Location(this.southDoor.getWorld(), (int)this.southDoor.getX() + Integer.valueOf(linex.split(",")[0]), (int)this.southDoor.getY() + Integer.valueOf(linex.split(",")[1]), (int)this.southDoor.getZ() + Integer.valueOf(linex.split(",")[2]));
                } else if  (line.contains("BORDER:")) {
                    String linex = line.replace("BORDER:", "");
                    this.endPoint = new Location(this.southDoor.getWorld(), (int)this.southDoor.getX() + Integer.valueOf(linex.split(",")[0]), (int)this.southDoor.getY() + Integer.valueOf(linex.split(",")[1]), (int)this.southDoor.getZ() + Integer.valueOf(linex.split(",")[2]));
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
                    getLoc = new Location(southDoor.getWorld(), (int) southDoor.getX() - Integer.valueOf(linex.split(",")[0]), (int) southDoor.getY() - Integer.valueOf(linex.split(",")[1]), (int) southDoor.getZ() - Integer.valueOf(linex.split(",")[2]));
                } else if (direction.equals("left")) {
                    getLoc = new Location(southDoor.getWorld(), (int) southDoor.getX() - Integer.valueOf(linex.split(",")[2]), (int) southDoor.getY() - Integer.valueOf(linex.split(",")[1]), (int) southDoor.getZ() + Integer.valueOf(linex.split(",")[0]));
                } else if (direction.equals("right")) {
                    getLoc = new Location(southDoor.getWorld(), (int) southDoor.getX() + Integer.valueOf(linex.split(",")[2]), (int) southDoor.getY() - Integer.valueOf(linex.split(",")[1]), (int) southDoor.getZ() - Integer.valueOf(linex.split(",")[0]));
                } else if (direction.equals("behind")) {
                    getLoc = new Location(southDoor.getWorld(), (int) southDoor.getX() + Integer.valueOf(linex.split(",")[0]), (int) southDoor.getY() - Integer.valueOf(linex.split(",")[1]), (int) southDoor.getZ() + Integer.valueOf(linex.split(",")[2]));
                }
                reurnLine += ("," + getLoc.getBlockX() + "," + getLoc.getBlockY() + "," + getLoc.getBlockZ());
             } else if  (line.contains("BORDER:")) {
                String linex = line.replace("BORDER:", "");
                 Location getLoc = southDoor;
                 if (direction.equals("null") || direction.equals("forward")) {
                     System.out.println("Here");
                     getLoc = new Location(southDoor.getWorld(), (int) southDoor.getX() + Integer.valueOf(linex.split(",")[0]), (int) southDoor.getY() + Integer.valueOf(linex.split(",")[1]), (int) southDoor.getZ() + Integer.valueOf(linex.split(",")[2]));
                 } else if (direction.equals("left")) {
                     getLoc = new Location(southDoor.getWorld(), (int) southDoor.getX() + Integer.valueOf(linex.split(",")[2]), (int) southDoor.getY() + Integer.valueOf(linex.split(",")[1]), (int) southDoor.getZ() - Integer.valueOf(linex.split(",")[0]));
                 } else if (direction.equals("right")) {
                     getLoc = new Location(southDoor.getWorld(), (int) southDoor.getX() - Integer.valueOf(linex.split(",")[2]), (int) southDoor.getY() + Integer.valueOf(linex.split(",")[1]), (int) southDoor.getZ() + Integer.valueOf(linex.split(",")[0]));
                 } else if (direction.equals("behind")) {
                     getLoc = new Location(southDoor.getWorld(), (int) southDoor.getX() - Integer.valueOf(linex.split(",")[0]), (int) southDoor.getY() + Integer.valueOf(linex.split(",")[1]), (int) southDoor.getZ() - Integer.valueOf(linex.split(",")[2]));
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
        Location loc1 = new Location(origin.getWorld(), Math.min(origin.getX(), endPoint.getX()), Math.min(origin.getY(), endPoint.getY()), Math.min(origin.getZ(), endPoint.getZ()));
        Location loc2 = new Location(origin.getWorld(), Math.max(origin.getX(), endPoint.getX()), Math.max(origin.getY(), endPoint.getY()), Math.max(origin.getZ(), endPoint.getZ()));
        this.safeDoors();
        saveData.add("NORTH:" + (int)(northDoor.getX() - southDoor.getX()) + "," +  (int)(northDoor.getY() - southDoor.getY()) + "," +  (int)(northDoor.getZ() - southDoor.getZ()));
        saveData.add("EAST:" + (int)(eastDoor.getX() - southDoor.getX()) + "," + (int)(eastDoor.getY() - southDoor.getY()) + "," + (int)(eastDoor.getZ() - southDoor.getZ()));
        saveData.add("SOUTH:" + (int)(southDoor.getX() - loc1.getX()) + "," + (int)(southDoor.getY() - loc1.getY()) + "," + (int)(southDoor.getZ() - loc1.getZ()));
        saveData.add("WEST:" + (int)(westDoor.getX() - southDoor.getX()) + "," + (int)(westDoor.getY() -  southDoor.getY()) + "," + (int)(westDoor.getZ() - southDoor.getZ()));
        saveData.add("BORDER:" + (int)(loc2.getX() - southDoor.getX()) + "," + (int)(loc2.getY() -  southDoor.getY()) + "," + (int)(loc2.getZ() - southDoor.getZ()));
        saveData.addAll(additionalData);
        FileSaving.writeFile("rooms" + File.separator + this.name + ".placement", saveData);
        CalebWorldEditAPI.trySaveSchem(loc1, loc2, this.name, new Vector(loc1.getX() - this.southDoor.getX(), loc1.getY() - this.southDoor.getY(),  loc1.getZ() - this.southDoor.getZ()));
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
