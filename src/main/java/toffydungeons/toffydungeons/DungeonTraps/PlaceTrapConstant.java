package toffydungeons.toffydungeons.DungeonTraps;

import org.bukkit.Location;
import toffydungeons.toffydungeons.API.BoundingBox;

import java.util.ArrayList;

public class PlaceTrapConstant {

    private Location loc1;
    private Location loc2;
    private Location spawnLoc;
    private String saveName;
    private String trapName;

    public PlaceTrapConstant(String trapName) {
        this.trapName  = trapName;
    }


    public void setLoc1(Location loc1) {
        this.loc1 = loc1;
    }

    public void setLoc2(Location loc2) {
        this.loc2 = loc2;
    }

    public boolean isValid() {
        return(loc1 != null && loc2!= null && spawnLoc != null);
    }

    public String getSaveName() {
        return saveName;
    }

    public Location getLoc1() {
        return loc1;
    }

    public Location getLoc2() {
        return loc2;
    }

    public Location getSpawnLoc() {
        return spawnLoc;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public void setSpawnLoc(Location spawnLoc) {
        this.spawnLoc = spawnLoc;
    }

    public ArrayList<String> saveTrapInfo(ArrayList<String> oldInf) {
        for (int x =0; x < oldInf.size(); x++) {
            if (oldInf.get(x).equals("SAVE_NAME:" + saveName)) {
                oldInf.subList(x, x+5).clear();
            }
        }
        oldInf.add("SAVE_NAME:" + saveName);
        oldInf.add(trapName);
        oldInf.add("REG1:" + BoundingBox.serialiseLocation(loc1));
        oldInf.add("REG2:" + BoundingBox.serialiseLocation(loc2));
        oldInf.add("HAPPEN:" + BoundingBox.serialiseLocation(spawnLoc));
        return oldInf;
    }
}