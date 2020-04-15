package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;

public class DungeonRoomManager implements InventoryHolder {

    private Inventory inv;
    private DungeonRoom room;
    private DungeonRoomLayout layout;

    public DungeonRoomManager(DungeonRoom room, DungeonRoomLayout layout) {
        this.inv = Bukkit.createInventory(this, 36, "Room Manager");
        this.room = room;
        this.layout = layout;
    }

    public DungeonRoom getRoom() {
        return room;
    }

    public DungeonRoomLayout getLayout() {
        return layout;
    }

    public void initialiseItems() {
        this.inv.setItem(4, DungeonMainMenu.createGuiItem(Material.PAPER, "§6Change Schematic (" + room.getSchematicFile() + ")"));
        this.inv.setItem(13, DungeonMainMenu.createGuiItem(Material.THIN_GLASS, "§aOpen Side"));
        this.inv.setItem(21, DungeonMainMenu.createGuiItem(Material.THIN_GLASS, "§aOpen Side"));
        this.inv.setItem(22, DungeonMainMenu.createGuiItem(Material.BRICK, "§3Set as starter block"));
        this.inv.setItem(23, DungeonMainMenu.createGuiItem(Material.THIN_GLASS, "§aOpen Side"));
        this.inv.setItem(31, DungeonMainMenu.createGuiItem(Material.THIN_GLASS, "§aOpen Side"));
        this.inv.setItem(27, DungeonMainMenu.createGuiItem(Material.REDSTONE_BLOCK, "§4§lABORT"));
        this.inv.setItem(35, DungeonMainMenu.createGuiItem(Material.EMERALD_BLOCK, "§a§lSave"));
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }
}
