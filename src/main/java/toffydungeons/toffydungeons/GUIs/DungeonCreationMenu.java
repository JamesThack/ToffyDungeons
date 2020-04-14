package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.API.FileSaving;

import java.io.File;
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

    private DungeonRoomLayout getLayout(DungeonCreationMenu menu) {
        return menu.layout;
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
        this.getInventory().setItem(49, createGuiItem(Material.LAPIS_BLOCK, "§6Generate Instantly"));
        this.getInventory().setItem(53, createGuiItem(Material.EMERALD_BLOCK, "§2Save Dungeon"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void openEmptyInventory(Player player) {
        System.out.println(this.layout.getPositions().size());
        DungeonRoomLayout newLayout = new DungeonRoomLayout();
        DungeonRoom start = new DungeonRoom("ExampleRoom", new int[]{4,2});
        newLayout.addRoom(start);
        newLayout.setStartingRoom(start);
        this.layout = newLayout;
        this.panDistance = new int[]{0,0};
        System.out.println(this.layout.getPositions().size());
        player.openInventory(this.getInventory());
        System.out.println(this.layout.getPositions().size());
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
                System.out.println(getLayout(this).getPositions().size());
                this.panDistance[0] = this.panDistance[0] + (e.getSlot()%9)-4;
                this.panDistance[1] = this.panDistance[1] + ((int)e.getSlot()/9)-2;
                System.out.println(this.layout.getPositions().size());
                DungeonCreationMenu menu = new DungeonCreationMenu(layout, this.panDistance);
                System.out.println(this.layout.getPositions().size());
                menu.initaliseItems();
                System.out.println(this.layout.getPositions().size());
                e.getWhoClicked().openInventory(menu.getInventory());
                System.out.println(this.layout.getPositions().size());
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
                menu.updateLayout();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                DungeonRoomLayout.deserialise(serialise());
                FileSaving.saveFile("dungeons", ("dungeons" + File.separator + "testFile.dungeon"));
                FileSaving.writeFile(("dungeons" + File.separator + "testFile.dungeon"), serialise());
                DungeonRoomLayout layout = new DungeonRoomLayout();
                DungeonRoom start = new DungeonRoom("ExampleRoom", new int[]{4,2});
                layout.addRoom(start);
                layout.setStartingRoom(start);
                this.layout = layout;
                this.panDistance =  new int[]{0,0};
                this.updateLayout();
                DungeonSelectionMenu menu = new DungeonSelectionMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());

            }
        }
    }

}