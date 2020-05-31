package toffydungeons.toffydungeons.GUIs.DungeonTraps;

import org.bukkit.Material;
import toffydungeons.toffydungeons.API.FileSaving;
import toffydungeons.toffydungeons.GUIs.extendable.AbstractVanityMenu;

import java.io.File;
import java.util.ArrayList;

public class MobTrap extends AbstractVanityMenu {

    private String mobType;
    private String name;
    private String mobName;
    private double health;

    public MobTrap() {
        super("New Mob Trap", 27);
        int adder = FileSaving.filesInDirectory("traps").size() + 1;
        this.name = "Trap_" + adder;
        while (FileSaving.filesInDirectory("traps").contains(this.name + ".trap")) {
            adder +=1;
            this.name = "Trap_" + adder;
        }
        this.mobType = "ZOMBIE";
        this.health = 20;
        FileSaving.saveFile("traps", "traps" + File.separator + name + ".trap");
    }

    public MobTrap(String name) {
        super("Trap Editor: " + name, 27);
        this.name = name;
        ArrayList<String> lines = FileSaving.readLines("traps" + File.separator + name + ".trap");
        this.health = 20;
        for (String current : lines) {
            if (current.contains("NAME"))
                this.mobName = current.replace("NAME:", "");
            if (current.contains("MOB_TYPE"))
                this.mobType = current.replace("MOB_TYPE:", "").replace("&", "§");
            if (current.contains("HEALTH"))
                this.health = Double.valueOf(current.replace("HEALTH:", ""));
        }
        if (this.mobType == null)
            this.mobType = "ZOMBIE";
    }

    @Override
    public void initaliseItems() {
        this.getInventory().setItem(10, createGuiItem(Material.MONSTER_EGG, "§cMob Type: " + this.mobType));
        this.getInventory().setItem(12, createGuiItem(Material.WHEAT, "§4Mob Health: " + this.health));
        if (this.mobName != null)
            this.getInventory().setItem(14, createGuiItem(Material.NAME_TAG, "§dMob Name: " + this.mobName));
        if (this.mobName == null)
            this.getInventory().setItem(14, createGuiItem(Material.NAME_TAG, "§dSet Mob Name"));
        this.getInventory().setItem(25, createGuiItem(Material.LAPIS_BLOCK, "§2Spawn Mob"));
        this.getInventory().setItem(26, createGuiItem(Material.EMERALD_BLOCK, "§aSave Mob"));
    }

    public String getMobType() {
        return mobType;
    }

    public void setMobType(String mobType) {
        this.mobType = mobType;
    }

    public String getMobName() {
        return mobName;
    }

    public void setMobName(String mobName) {
        this.mobName = mobName;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void saveData() {
        ArrayList<String> dataToWrite = new ArrayList<>();
        dataToWrite.add("MOB_TYPE:" + mobType);
        dataToWrite.add("HEALTH:" + health);
        if (mobName != null) {
            dataToWrite.add("NAME:" + mobName);
        }
        FileSaving.writeFile("traps" + File.separator + name +  ".trap", dataToWrite);
    }
}
