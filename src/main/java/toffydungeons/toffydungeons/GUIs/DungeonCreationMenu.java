package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
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
        DungeonRoomLayout layout = new DungeonRoomLayout();
        DungeonRoom start = new DungeonRoom("ExampleRoom", 22);
        layout.addRoom(start);
        layout.setStartingRoom(start);
        this.layout = layout;
        this.updateLayout();
    }

    public DungeonCreationMenu(DungeonRoomLayout layout) {
        inv = Bukkit.createInventory(this, 54, "Dungeon Creation");
        this.layout = layout;
        this.updateLayout();
    }

    public static ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metalore = new ArrayList<String>(Arrays.asList(lore));
        meta.setLore(metalore);
        item.setItemMeta(meta);
        return item;
    }

    private void updateLayout() {
        for (int current : this.layout.getPositions()) {
            this.getInventory().setItem(current, createGuiItem(Material.SMOOTH_BRICK, "Room"));
        }
    }

    public void initaliseItems() {
        this.getInventory().setItem(45, createGuiItem(Material.REDSTONE_BLOCK, "§cClose Menu"));
        this.getInventory().setItem(49, createGuiItem(Material.LAPIS_BLOCK, "§6Generate Instantly"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    /**
     * Another chonky method, so some explaination is required! This handles the room placement, if an empty space
     * is clicked then a room is generated there. When placed the room looks at its position and sees if there are
     * any adjacent rooms. Of course the adjacent rooms are needed for construction.
     * @param e
     */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(this.getInventory().getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                DungeonSelectionMenu menu = new DungeonSelectionMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.LAPIS_BLOCK)) {
                layout.generateBuild(e.getWhoClicked().getLocation());
                DungeonRoomLayout layout = new DungeonRoomLayout();
                DungeonRoom start = new DungeonRoom("ExampleRoom", 22);
                layout.addRoom(start);
                layout.setStartingRoom(start);
                this.layout = layout;
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.AIR)) {
                DungeonRoom newRoom = new DungeonRoom("ExampleRoom", e.getSlot());
                for (DungeonRoom selected : layout.getRooms()) {
                    if (selected.getPosition() - 9 == newRoom.getPosition()) {
                        newRoom.setBehind(selected);
                        selected.setForward(newRoom);
                    } else if (selected.getPosition() + 9 == newRoom.getPosition()) {
                        newRoom.setForward(selected);
                        selected.setBehind(newRoom);
                    } else if (selected.getPosition() + 1 == newRoom.getPosition()) {
                        newRoom.setLeft(selected);
                        selected.setRight(newRoom);

                    } else if (selected.getPosition() - 1 == newRoom.getPosition()) {
                        newRoom.setRight(selected);
                        selected.setLeft(newRoom);
                    }
                }
                layout.addRoom(newRoom);
                DungeonCreationMenu menu = new DungeonCreationMenu(layout);
                menu.initaliseItems();
                menu.getInventory().setItem(e.getSlot(), createGuiItem(Material.SMOOTH_BRICK, "Room"));
                e.getWhoClicked().openInventory(menu.getInventory());
            }
        }
    }

}