package toffydungeons.toffydungeons.GUIs.DungeonRoomDesign;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import toffydungeons.toffydungeons.DungeonDesign.DungeonRoomDesign;
import toffydungeons.toffydungeons.GUIs.DungeonMainMenu;

public class DungeonRoomWandCustomiser implements InventoryHolder, Listener {

    /**
     * Controls what the dungeon room customisaition menu looks like (when dropping the wand)
     */

    private Inventory inv;
    public DungeonRoomDesign designer;

    public DungeonRoomWandCustomiser(DungeonRoomDesign designer) {
        this.inv = Bukkit.createInventory(this, 27, "Wand Customiser");
        this.designer = designer;
    }

    public void initaliseItems() {
        inv.setItem(4, DungeonMainMenu.createGuiItem(Material.WOOD_DOOR, "§fChoose North Door"));
        inv.setItem(10, DungeonMainMenu.createGuiItem(Material.STICK, "§2Select Borders"));
        inv.setItem(12, DungeonMainMenu.createGuiItem(Material.WOOD_DOOR, "§fChoose West Door"));
        inv.setItem(13, DungeonMainMenu.createGuiItem(Material.PAPER, "§6Save Room"));
        inv.setItem(14, DungeonMainMenu.createGuiItem(Material.WOOD_DOOR, "§fChoose East Door"));
        inv.setItem(16, DungeonMainMenu.createGuiItem(Material.TRIPWIRE_HOOK, "§cTraps"));
        inv.setItem(22, DungeonMainMenu.createGuiItem(Material.WOOD_DOOR, "§fChoose South Door"));
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }


}
