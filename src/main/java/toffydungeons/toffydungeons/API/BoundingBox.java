package toffydungeons.toffydungeons.API;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class BoundingBox {

    /**
     * This is a bounding box, essentialy you can construct a 3d box with coordinates and check for things (such as players)
     */
    private Location loc1;
    private Location loc2;

    public BoundingBox(Location loc1, Location loc2) {
        this.loc1 = new Location (loc1.getWorld(), Math.min(loc1.getX(), loc2.getX()), Math.min(loc1.getY(), loc2.getY()), Math.min(loc1.getZ(), loc2.getZ()));
        this.loc2 = new Location (loc1.getWorld(), Math.max(loc1.getX(), loc2.getX()), Math.max(loc1.getY(), loc2.getY()), Math.max(loc1.getZ(), loc2.getZ()));
    }

    public boolean entityInBox(Entity entity) {
        return (this.loc1.getWorld().equals(entity.getWorld()) && entity.getLocation().getBlockX() >= this.loc1.getBlockX() && entity.getLocation().getBlockY() >= this.loc1.getBlockY() && entity.getLocation().getBlockZ() >= this.loc1.getBlockZ() && entity.getLocation().getBlockX() <= this.loc2.getBlockX() && entity.getLocation().getBlockY() <= this.loc2.getBlockY() && entity.getLocation().getBlockZ() <= this.loc2.getBlockZ());
    }
}
