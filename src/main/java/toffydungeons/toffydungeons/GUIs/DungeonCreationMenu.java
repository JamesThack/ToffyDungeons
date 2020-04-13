package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
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
    private int[] panDistance;

    public DungeonCreationMenu() {
        inv = Bukkit.createInventory(this, 54, "Dungeon Creation");
        DungeonRoomLayout layout = new DungeonRoomLayout();
        DungeonRoom start = new DungeonRoom("ExampleRoom", new int[]{4,2});
        layout.addRoom(start);
        layout.setStartingRoom(start);
        this.layout = layout;
        this.panDistance = new int[]{0,0};
        this.updateLayout();
    }

    public DungeonCreationMenu(DungeonRoomLayout layout, int[] panDistance) {
        inv = Bukkit.createInventory(this, 54, "Dungeon Creation");
        this.panDistance = panDistance;
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
            if (e.getClick().equals(ClickType.MIDDLE)) {
                this.panDistance[0] = this.panDistance[0] + (e.getSlot()%9)-4;
                this.panDistance[1] = this.panDistance[1] + ((int)e.getSlot()/9)-2;
                DungeonCreationMenu menu = new DungeonCreationMenu(layout, this.panDistance);
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            }
            else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                DungeonSelectionMenu menu = new DungeonSelectionMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.LAPIS_BLOCK)) {
                layout.generateBuild(e.getWhoClicked().getLocation());
                DungeonRoomLayout layout = new DungeonRoomLayout();
                DungeonRoom start = new DungeonRoom("ExampleRoom",  new int[]{4,2} );
                layout.addRoom(start);
                layout.setStartingRoom(start);
                this.layout = layout;
                this.panDistance = new int[]{0,0};
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.AIR)) {
                int z = e.getSlot() % 9 + this.panDistance[0];
                int x =  (int) e.getSlot() / 9 + this.panDistance[1];
                int[] position = new int[]{z,x};
                System.out.println("New point is " + position[0] + "," + position[1]);
                DungeonRoom newRoom = new DungeonRoom("ExampleRoom", position);
                for (DungeonRoom selected : layout.getRooms()) {
                    if (selected.getPosition()[0] + 1 == newRoom.getPosition()[0] && newRoom.getPosition()[1] == selected.getPosition()[1]) {
                        selected.setRight(newRoom);
                        newRoom.setLeft(selected);
                    } else if (selected.getPosition()[0] - 1 == newRoom.getPosition()[0] && newRoom.getPosition()[1] == selected.getPosition()[1]) {
                        selected.setLeft(newRoom);
                        newRoom.setRight(selected);
                    } else if (selected.getPosition()[1] + 1 == newRoom.getPosition()[1] && newRoom.getPosition()[0] == selected.getPosition()[0]) {
                        selected.setBehind(newRoom);
                        newRoom.setForward(selected);
                    } else if (selected.getPosition()[1] - 1 == newRoom.getPosition()[1] && newRoom.getPosition()[0] == selected.getPosition()[0]) {
                        selected.setForward(newRoom);
                        newRoom.setBehind(selected);
                    }
                }
                layout.addRoom(newRoom);
                DungeonCreationMenu menu = new DungeonCreationMenu(layout, this.panDistance);
                menu.initaliseItems();
                menu.updateLayout();
                e.getWhoClicked().openInventory(menu.getInventory());
            }
        }
    }

}