package toffydungeons.toffydungeons.GUIs.extendable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import toffydungeons.toffydungeons.API.FileSaving;

import java.util.List;

import static toffydungeons.toffydungeons.GUIs.DungeonMainMenu.createGuiItem;

public class AbstractFileMenu implements InventoryHolder {

    /**
     * A basic class used to create a menu that supports loading files, requires both a directory and a menu title
     * (will load all items in the directory)
     */

    private Inventory inv;
    private int page;
    private String folder;

    public AbstractFileMenu(String title, String folder) {
        this.inv = Bukkit.createInventory(this, 54, title + " Page 1");
        this.page = 1;
        this.folder = folder;
    }

    public AbstractFileMenu(String title, String folder, int page) {
        this.inv = Bukkit.createInventory(this, 54, title + " Page " + page);
        this.page = page;
        this.folder = folder;
    }

    public int getPage() {
        return page;
    }

    public void initialiseItems() {
        List<String> availavleFiles = FileSaving.filesInDirectory(folder);
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

    public void addBonusItem(String title, Material material, int slot) {
        this.getInventory().setItem(slot, createGuiItem(material, title));
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }
}
