package toffydungeons.toffydungeons.DungeonDesign;

import org.bukkit.Material;
import toffydungeons.toffydungeons.GUIs.extendable.AbstractVanityMenu;

import java.util.ArrayList;

public class TrapSelection extends AbstractVanityMenu {

    private DungeonRoomDesign design;

    public TrapSelection(DungeonRoomDesign design) {
        super("Room Traps", 54);
        this.design = design;
    }

    @Override
    public void initaliseItems() {
        int count = 0;
        if (design.getAdditionalData() != null) {
            ArrayList<String> existingTraps = design.getAdditionalData();
            for (int x = 0; x < existingTraps.size(); x += 5) {
                this.getInventory().setItem(count, createGuiItem(Material.PAPER, "Room Trap: " + existingTraps.get(x).replace("SAVE_NAME:", ""), "Trap Type: " + existingTraps.get(x + 1).replace(".trap", "")));
                count += 1;
            }
        }
        this.getInventory().setItem(53, createGuiItem(Material.EMERALD_BLOCK, "§aCreate New Trap"));
    }
    public DungeonRoomDesign getDesign() {
        return design;
    }
}
