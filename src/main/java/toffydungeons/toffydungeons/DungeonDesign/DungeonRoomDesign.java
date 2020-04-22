package toffydungeons.toffydungeons.DungeonDesign;

import com.sk89q.worldedit.Vector;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import toffydungeons.toffydungeons.API.CalebWorldEditAPI;
import toffydungeons.toffydungeons.API.FileSaving;

import java.io.File;
import java.util.ArrayList;

public class DungeonRoomDesign {

    private String playerUUID;
    private Location origin;
    private Location endPoint;
    private String name;
    private int currentOperation;
    private Location northDoor;
    private Location eastDoor;
    private Location southDoor;
    private Location westDoor;

    public DungeonRoomDesign(Player player) {
        this.playerUUID = player.getUniqueId().toString();
        this.name= "UNNAMED";
        this.currentOperation = 0;
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
        saveData.add("SOUTH:" + "0,0,0");
        saveData.add("WEST:" + (int)(westDoor.getX() - southDoor.getX()) + "," + (int)(westDoor.getY() -  southDoor.getY()) + "," + (int)(westDoor.getZ() - southDoor.getZ()));
        FileSaving.writeFile("rooms" + File.separator + this.name + ".placement", saveData);


        System.out.println(loc1.toString());
        System.out.println(loc2.toString());

        return CalebWorldEditAPI.trySaveSchem(loc1, loc2, this.name, new Vector(loc1.getX() - this.southDoor.getX(), loc1.getY() - this.southDoor.getY(),  loc1.getZ() - this.southDoor.getZ()));
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
}
