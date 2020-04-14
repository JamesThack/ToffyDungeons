package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.API.FileSaving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DungeonSelectionMenu implements InventoryHolder, Listener {

    private final Inventory inv;

    public DungeonSelectionMenu() {
        inv = Bukkit.createInventory(this, 54, "Dungeon Selection");
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
        List<String> availavleFiles = FileSaving.filesInDirectory("dungeons");
            for (int i = 0; i < availavleFiles.size(); i++) {
                this.getInventory().setItem(i, createGuiItem(Material.SMOOTH_BRICK, availavleFiles.get(i)));
        }
        this.getInventory().setItem(45, createGuiItem(Material.REDSTONE_BLOCK, "§cClose Menu"));
        this.getInventory().setItem(53, createGuiItem(Material.EMERALD_BLOCK, "§2Create New Dungeon"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(this.getInventory().getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null &&  e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                DungeonRoomLayout layout = new DungeonRoomLayout();
                DungeonRoom start = new DungeonRoom("ExampleRoom", new int[]{4,2});
                layout.addRoom(start);
                layout.setStartingRoom(start);
                DungeonCreationMenu menuNew = new DungeonCreationMenu(layout, new int[]{0,0});
                menuNew.updateLayout();
                menuNew.openEmptyInventory((Player) e.getWhoClicked());
            }
        }
    }
}
