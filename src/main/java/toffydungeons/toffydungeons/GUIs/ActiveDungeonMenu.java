package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import toffydungeons.toffydungeons.API.FileSaving;

import java.util.List;

import static toffydungeons.toffydungeons.GUIs.DungeonMainMenu.createGuiItem;

public class ActiveDungeonMenu implements InventoryHolder {

    private Inventory inv;
    private int page;

    public ActiveDungeonMenu() {
        this.inv = Bukkit.createInventory(this, 54, "Active Dungeons Page 1");
        this.page = 1;
    }

    public ActiveDungeonMenu(int page) {
        this.inv = Bukkit.createInventory(this, 54, "Active Dungeons Page " + page);
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void initialiseItems() {
        List<String> availavleFiles = FileSaving.filesInDirectory("active_dungeons");
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

    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }
}
