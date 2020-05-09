package toffydungeons.toffydungeons.GUIs.DungeonRoomDesign;

import org.bukkit.Material;
import toffydungeons.toffydungeons.API.FileSaving;
import toffydungeons.toffydungeons.GUIs.extendable.AbstractFileMenu;

import java.util.List;

import static toffydungeons.toffydungeons.GUIs.DungeonMainMenu.createGuiItem;

public class DungeonRoomSelector extends AbstractFileMenu {

    public DungeonRoomSelector() {
        super("Dungeon Blueprint Editor", "rooms");
    }

    public DungeonRoomSelector(int page) {
        super("Dungeon Blueprint Editor", "rooms", page);
    }

    public DungeonRoomSelector(String title, String folder) {
        super(title, folder);
    }

    public DungeonRoomSelector(String titie, String folder, int page) {
        super(titie, folder, page);
    }

    @Override
    public void initialiseItems() {
        List<String> availavleFiles = FileSaving.filesInDirectory("rooms");
        int count = 0;
        for (int i = 0; i < availavleFiles.size(); i++) {
            if (availavleFiles.get(i).contains(".schematic") && availavleFiles.contains(availavleFiles.get(i).replace(".schematic", ".placement"))) {
                int placement = count;
                while (placement >= 45) {
                    placement -= 45;
                }
                if (count >= (getPage() - 1) * 45 && count < 45 * getPage()) {
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
