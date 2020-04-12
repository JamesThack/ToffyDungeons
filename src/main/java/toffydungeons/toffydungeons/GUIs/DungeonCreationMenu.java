package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class DungeonCreationMenu implements InventoryHolder, Listener {

    private final Inventory inv;
    private DungeonRoomLayout layout;

    public DungeonCreationMenu() {
        inv = Bukkit.createInventory(this, 54, "Dungeon Creation");
        this.layout = new DungeonRoomLayout();
        DungeonRoom room = new DungeonRoom("ExampleRoom", 22);
        this.layout.addRoom(room);
        this.layout.setStartingRoom(room);
        this.updateLayout();
    }

    public DungeonCreationMenu(DungeonRoomLayout layout) {
        inv = Bukkit.createInventory(this, 54, "Dungeon Creation");
    }

    private void updateLayout() {
        for(int current : this.layout.getPositions()) {
            this.getInventory().setItem(current, createGuiItem(Material.SMOOTH_BRICK, "Room"));
        }
    }

    public static ItemStack createGuiItem(Material material, String name, String...lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metalore = new ArrayList<String>(Arrays.asList(lore));
        meta.setLore(metalore);
        item.setItemMeta(meta);
        return item;
    }

    public void initaliseItems() {
        this.getInventory().setItem(45, createGuiItem(Material.REDSTONE_BLOCK, "§cClose Menu"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(this.getInventory().getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType() != null &&  e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                DungeonSelectionMenu menu = new DungeonSelectionMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            }
        }
    }
}