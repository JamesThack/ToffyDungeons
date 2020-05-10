package toffydungeons.toffydungeons.GUIs.DungeonLayout;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.GUIs.DungeonMainMenu;

public class DungeonGenerationMenu implements InventoryHolder {

    /**
     * The menu for choosing what form to spawn a dungeon as
     */

    private Inventory inv;
    public DungeonRoomLayout layout;

    public DungeonGenerationMenu(DungeonRoomLayout layout) {
        this.inv = Bukkit.createInventory(this, 27, "Dungeon Generation");
        this.layout = layout;
    }

    public void initialiseItems() {
        inv.setItem(10, DungeonMainMenu.createGuiItem(Material.LAPIS_BLOCK, "§3Generate At Player"));
        inv.setItem(12, DungeonMainMenu.createGuiItem(Material.EGG, "§6Generate An Egg"));
        inv.setItem(18, DungeonMainMenu.createGuiItem(Material.REDSTONE_BLOCK, "§cClose Menu"));
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }

}
