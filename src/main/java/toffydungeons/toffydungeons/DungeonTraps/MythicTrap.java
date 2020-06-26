package toffydungeons.toffydungeons.DungeonTraps;

import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.Material;
import toffydungeons.toffydungeons.API.FileSaving;
import toffydungeons.toffydungeons.GUIs.extendable.AbstractVanityMenu;

import java.io.File;
import java.util.ArrayList;

public class MythicTrap extends AbstractVanityMenu {

    private String fileName;
    private String mobName = (String) MythicMobs.inst().getMobManager().getMobNames().toArray()[0];
    private String level = "1";

    public MythicTrap() {
        super("Trap Editor", 27);
        int adder = FileSaving.filesInDirectory("traps").size() + 1;
        this.fileName = "Trap_" + adder;
        while (FileSaving.filesInDirectory("traps").contains(this.fileName + ".trap")) {
            adder +=1;
            this.fileName = "Trap_" + adder;
        }
    }

    public MythicTrap(String fileName) {
        super("Trap Editor: " + fileName, 27);
        this.fileName = fileName;
        this.mobName = FileSaving.readLines("traps" + File.separator + fileName + ".trap").get(1).replace("MOB_NAME:", "");
        this.level = FileSaving.readLines("traps" + File.separator + fileName + ".trap").get(2).replace("MOB_LEVEL:", "");
    }

    @Override
    public void initaliseItems() {
        this.getInventory().setItem(10, createGuiItem(Material.LEASH, "Mob Type: " + mobName));
        this.getInventory().setItem(12, createGuiItem(Material.EXP_BOTTLE, "Mob Level: " + level));
        this.getInventory().setItem(26, createGuiItem(Material.EMERALD_BLOCK, "Save Mob"));
    }


    public void save() {
        ArrayList<String> saveData = new ArrayList<>();
        saveData.add("TRAP_TYPE:MYTHIC_TRAP");
        saveData.add("MOB_NAME:" + mobName);
        saveData.add("MOB_LEVEL:" + level);
        FileSaving.saveFile("traps", "traps" + File.separator + fileName +".trap");
        FileSaving.writeFile("traps" + File.separator + fileName + ".trap", saveData);
    }

    public String getFileName() {
        return fileName;
    }

    public String getMobName() {
        return mobName;
    }

    public void setMobName(String mobName) {
        this.mobName = mobName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
