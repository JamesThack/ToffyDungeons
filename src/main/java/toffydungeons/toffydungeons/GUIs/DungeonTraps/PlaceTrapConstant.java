package toffydungeons.toffydungeons.GUIs.DungeonTraps;

import org.bukkit.Location;

import java.util.ArrayList;

public class PlaceTrapConstant {

    private int[] loc1;
    private int[] loc2;
    private int[] spawnLoc;
    private Location southDoor;
    private String trapName;

    public PlaceTrapConstant(String trapName) {
        this.trapName  = trapName;
    }

    public void setLoc1(int[] loc1) {
        this.loc1 = loc1;
    }

    public void setLoc2(int[] loc2) {
        this.loc2 = loc2;
    }

    public int[] getLoc1() {
        return loc1;
    }

    public int[]tLoc2() {
        return loc2;
    }

    public int[] getSpawnLoc() {
        return spawnLoc;
    }

    public void setSpawnLoc(int[] spawnLoc) {
        this.spawnLoc = spawnLoc;
    }

    public ArrayList<String> saveTrapInfo(ArrayList<String> oldInf) {
        oldInf.add(trapName);
        oldInf.add("REG1:" + loc1[0] + "," + loc1[1] + "," + loc1[2]);
        oldInf.add("REG2:" + loc2[0] + "," + loc2[1] + "," + loc2[2]);
        oldInf.add("HAPPEN:" + spawnLoc[0] + "," + spawnLoc[1] + "," + spawnLoc[2]);
        return oldInf;
    }
}
