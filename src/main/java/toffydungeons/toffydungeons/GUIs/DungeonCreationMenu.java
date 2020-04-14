package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.API.FileSaving;

import java.util.ArrayList;
import java.util.Arrays;

import static toffydungeons.toffydungeons.GUIs.DungeonMainMenu.createGuiItem;

public class DungeonCreationMenu implements InventoryHolder {

    private final Inventory inv;
    public DungeonRoomLayout layout;
    public int[] panDistance;
    private String dungeonName;

    public DungeonCreationMenu() {
        int adder = 1;
        this.dungeonName = ("Dungeon_" + (FileSaving.filesInDirectory("dungeons").size() + adder));
        while (FileSaving.folderContainsFile("dungeons", this.dungeonName + ".dungeon")) {
            adder +=1;
            this.dungeonName = ("Dungeon_" + (FileSaving.filesInDirectory("dungeons").size() + adder));
        }
        inv = Bukkit.createInventory(this, 54, "Dungeon Creation: " + this.dungeonName);
        DungeonRoomLayout layout = new DungeonRoomLayout();
        DungeonRoom start = new DungeonRoom("ExampleRoom", new int[]{4,2});
        layout.addRoom(start);
        layout.setStartingRoom(start);
        this.layout = layout;
        this.panDistance = new int[]{0,0};
    }

    public DungeonCreationMenu(DungeonRoomLayout layout, int[] panDistance, String dungeonName) {
        this.dungeonName = dungeonName;
        inv = Bukkit.createInventory(this, 54, "Dungeon Creation: " + this.dungeonName);
        this.panDistance = panDistance;
        this.layout = layout;
        this.updateLayout();
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public void updateLayout() {
        for (int[] current : this.layout.getPositions()) {
            try {
                int x = (((current[1]) - panDistance[1]) * 9);
                int y = (current[0] - panDistance[0]);
                if (y < 9 && y >= 0) {
                    this.getInventory().setItem(x + y , createGuiItem(Material.SMOOTH_BRICK, "Room"));
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        }
        this.initaliseItems();
    }

    public ArrayList<String> serialise() {
        ArrayList<String> values = new ArrayList<>();
        for (int[] curPosition : this.layout.getPositions()) {
            if (Arrays.equals(this.layout.getStartingRoom().getPosition(), curPosition)) {
                values.add("start:" + curPosition[0] + "," + curPosition[1]);
            } else {
                values.add("position:" + curPosition[0] + "," + curPosition[1]);
            }
        }
        return values;
    }

    public void initaliseItems() {
        this.getInventory().setItem(45, createGuiItem(Material.REDSTONE_BLOCK, "§cClose Menu"));
        this.getInventory().setItem(46, createGuiItem(Material.REDSTONE_TORCH_ON, "§4Delete Dungeon"));
        this.getInventory().setItem(49, createGuiItem(Material.LAPIS_BLOCK, "§6Generate Instantly"));
        this.getInventory().setItem(53, createGuiItem(Material.EMERALD_BLOCK, "§2Save Dungeon"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

}