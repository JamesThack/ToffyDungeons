package toffydungeons.toffydungeons.GUIs.DungeonRoomDesign;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import toffydungeons.toffydungeons.API.FileSaving;

import java.util.List;

import static toffydungeons.toffydungeons.GUIs.DungeonMainMenu.createGuiItem;

public class DungeonRoomSelector implements InventoryHolder {

    private Inventory inv;
    private int page;

    public DungeonRoomSelector() {
        this.inv = Bukkit.createInventory(this, 54, "Dungeon Blueprint Editor Page 1");
        this.page = 1;
    }

    public DungeonRoomSelector(int page) {
        this.page = page;
        this.inv = Bukkit.createInventory(this, 54, "Dungeon Blueprint Editor Page " + this.page);
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }

    public int getPage() {
        return page;
    }

    public void initaliseItems() {
        List<String> availavleFiles = FileSaving.filesInDirectory("rooms");
        int count = 0;
        for (int i = 0; i < availavleFiles.size(); i++) {
            if (availavleFiles.get(i).contains(".schematic") && availavleFiles.contains(availavleFiles.get(i).replace(".schematic", ".placement"))) {
                int placement = count;
                while (placement >= 45) {
                    placement -= 45;
                }
                if (count >= (page - 1) * 45 && count < 45 * page) {
                    this.getInventory().setItem(placement, createGuiItem(Material.WOOL, availavleFiles.get(i).replace(".schematic", "")));
                } count +=1;
            }
        }
        this.getInventory().setItem(45, createGuiItem(Material.REDSTONE_BLOCK, "§cClose Menu"));
        this.getInventory().setItem(46, createGuiItem(Material.PAPER, "Previous Page"));
        this.getInventory().setItem(47, createGuiItem(Material.PAPER, "Next Page"));
        this.getInventory().setItem(49, createGuiItem(Material.REDSTONE_TORCH_ON, "§cShift Click To Delete Blueprints"));
    }
}
