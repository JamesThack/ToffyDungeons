package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.API.FileSaving;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static toffydungeons.toffydungeons.GUIs.DungeonMainMenu.createGuiItem;

public class DungeonCreationMenu implements InventoryHolder {

    private final Inventory inv;
    public DungeonRoomLayout layout;
    public int[] panDistance;

    public DungeonCreationMenu() {
        DungeonRoomLayout layout = new DungeonRoomLayout();
        DungeonRoom start = new DungeonRoom("ExampleRoom", new int[]{4,2});
        layout.addRoom(start);
        layout.setStartingRoom(start);
        this.layout = layout;
        int adder = 1;
        layout.dungeonName = ("Dungeon_" + (FileSaving.filesInDirectory("dungeons").size() + adder));
        while (FileSaving.folderContainsFile("dungeons", layout.dungeonName + ".dungeon")) {
            adder +=1;
            layout.dungeonName = ("Dungeon_" + (FileSaving.filesInDirectory("dungeons").size() + adder));
        }
        inv = Bukkit.createInventory(this, 54, "Dungeon Creation: " +layout.dungeonName);
        this.panDistance = new int[]{0,0};
        FileSaving.saveFile("dungeons", ("dungeons" + File.separator + getDungeonName() +".dungeon"));
        FileSaving.writeFile(("dungeons" + File.separator + getDungeonName() +".dungeon"), serialise());
    }

    public DungeonCreationMenu(DungeonRoomLayout layout, int[] panDistance) {
        inv = Bukkit.createInventory(this, 54, "Dungeon Creation: " + layout.dungeonName);
        this.panDistance = panDistance;
        this.layout = layout;
        this.updateLayout();
    }

    public DungeonCreationMenu(DungeonRoomLayout layout, int[] panDistance, String dungeonName) {
        layout.dungeonName = dungeonName;
        inv = Bukkit.createInventory(this, 54, "Dungeon Creation: " + layout.dungeonName);
        this.panDistance = panDistance;
        this.layout = layout;
        this.updateLayout();
    }

    public String getDungeonName() {
        return layout.dungeonName;
    }

    public void updateLayout() {
        for (int[] current : this.layout.getPositions()) {
            try {
                int x = (((current[1]) - panDistance[1]) * 9);
                int y = (current[0] - panDistance[0]);
                if (y < 9 && y >= 0) {
                    this.getInventory().setItem(x + y , createGuiItem(Material.SMOOTH_BRICK, this.layout.getRoomFromPosition(current).getSchematicFile()));
                    if (Arrays.equals(this.layout.getStartingRoom().getPosition(), current))
                        this.getInventory().setItem(x + y , createGuiItem(Material.BRICK, "Starting Room"));
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
                values.add("start:" + curPosition[0] + "," + curPosition[1] + "," + this.layout.getRoomFromPosition(curPosition).getSchematicFile());
            } else {
                values.add("position:" + curPosition[0] + "," + curPosition[1] + "," + this.layout.getRoomFromPosition(curPosition).getSchematicFile());
            }
        }
        return values;
    }

    public void initaliseItems() {
        this.getInventory().setItem(45, createGuiItem(Material.REDSTONE_BLOCK, "§cClose Menu"));
        this.getInventory().setItem(46, createGuiItem(Material.REDSTONE_TORCH_ON, "§4Delete Dungeon"));
        this.getInventory().setItem(49, createGuiItem(Material.LAPIS_BLOCK, "§6Generate Instantly"));
        this.getInventory().setItem(52, createGuiItem(Material.NAME_TAG, "§9How To Rename", "To rename the dungeon, first save","" +
                "the dungeon then in chat do","" +
                "/tdungeon rename " +  layout.dungeonName+ " (new dungeonName)"));
        this.getInventory().setItem(53, createGuiItem(Material.EMERALD_BLOCK, "§2Save Dungeon"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

}