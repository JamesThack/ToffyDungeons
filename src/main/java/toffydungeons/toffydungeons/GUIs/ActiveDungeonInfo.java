package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ActiveDungeonInfo implements InventoryHolder {

    private Inventory inv;

    public ActiveDungeonInfo(String title) {
        this.inv = Bukkit.createInventory(this, 27, "Active Dungeon Editor: " + title);
    }

    public void initaliseItems() {
        this.inv.setItem(10, DungeonMainMenu.createGuiItem(Material.REDSTONE_TORCH_ON, "§0DELETE DUNGEON"));
    }

    public String getTitle() {
        return this.inv.getTitle();
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }
}
