package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.API.FileSaving;

import java.util.List;

import static toffydungeons.toffydungeons.GUIs.DungeonMainMenu.createGuiItem;

public class DungeonBlueprintChooser implements InventoryHolder {

    private final Inventory inv;
    private int page;
    private DungeonRoomLayout layout;
    private DungeonRoom room;

    public DungeonBlueprintChooser(DungeonRoomLayout layout, DungeonRoom room) {
        inv = Bukkit.createInventory(this, 54, "Blueprint Selection Page 1");
        this.page = 1;
        this.layout = layout;
        this.room = room;
    }

    public DungeonBlueprintChooser(int page, DungeonRoomLayout layout, DungeonRoom room) {
        this.page = page;
        inv = Bukkit.createInventory(this, 54, "Blueprint Selection Page " + page);
        this.layout = layout;
        this.room = room;
        initaliseItems();
    }

    public DungeonRoomLayout getLayout() {
        return layout;
    }

    public DungeonRoom getRoom() {
        return room;
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
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}