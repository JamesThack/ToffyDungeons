package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import toffydungeons.toffydungeons.API.FileSaving;

import java.util.List;

import static toffydungeons.toffydungeons.GUIs.DungeonMainMenu.createGuiItem;

public class DungeonSelectionMenu implements InventoryHolder {

    private final Inventory inv;
    private int page;

    public DungeonSelectionMenu() {
        inv = Bukkit.createInventory(this, 54, "Dungeon Selection Page 1");
        this.page = 1;
    }

    public DungeonSelectionMenu(int page) {
        this.page = page;
        inv = Bukkit.createInventory(this, 54, "Dungeon Selection Page " + page);
        initaliseItems();
    }

    public int getPage() {
        return page;
    }

    public void initaliseItems() {
        List<String> availavleFiles = FileSaving.filesInDirectory("dungeons");
            for (int i = 0 ; i < availavleFiles.size(); i++) {
                int placement = i;
                while (placement >= 45) {
                    placement -= 45;
                }
                if (i >= (page - 1) * 45 && i < 45 * page) {
                    this.getInventory().setItem(placement, createGuiItem(Material.SMOOTH_BRICK, availavleFiles.get(i)));
                }
        }
        this.getInventory().setItem(45, createGuiItem(Material.REDSTONE_BLOCK, "§cClose Menu"));
        this.getInventory().setItem(46, createGuiItem(Material.PAPER, "Previous Page"));
        this.getInventory().setItem(47, createGuiItem(Material.PAPER, "Next Page"));
        this.getInventory().setItem(53, createGuiItem(Material.EMERALD_BLOCK, "§2Create New Dungeon"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
