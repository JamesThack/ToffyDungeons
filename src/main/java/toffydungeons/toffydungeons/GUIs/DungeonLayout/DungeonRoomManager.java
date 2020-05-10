package toffydungeons.toffydungeons.GUIs.DungeonLayout;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.GUIs.DungeonMainMenu;

public class DungeonRoomManager implements InventoryHolder {

    /**
     * This class represents the menu for handling specific rooms in a dungeon layout (links to dungeon layout
     * menu)
     */

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
        int[] itemChecks = new int[]{13,23,31,21};
        for (int i = 0; i < itemChecks.length; i++) {
            ItemStack glass = DungeonMainMenu.createGuiItem(Material.STAINED_GLASS_PANE, 5, "§aOpen Side");
            if (room.getBlockedSides()[i] == 1)
                glass = DungeonMainMenu.createGuiItem(Material.STAINED_GLASS_PANE, 14, "§cClosed Side");
            this.inv.setItem(itemChecks[i], glass); }
        this.inv.setItem(4, DungeonMainMenu.createGuiItem(Material.PAPER, "§6Change Schematic (" + room.getSchematicFile() + ")"));
        if (!layout.getStartingRoom().equals(room))
            this.inv.setItem(22, DungeonMainMenu.createGuiItem(Material.BRICK, "§3Set as starter block"));
        this.inv.setItem(35, DungeonMainMenu.createGuiItem(Material.EMERALD_BLOCK, "§a§lSave"));
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }
}
