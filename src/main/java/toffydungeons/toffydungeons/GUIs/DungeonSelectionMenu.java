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

public class DungeonSelectionMenu implements InventoryHolder, Listener {

    private final Inventory inv;

    public DungeonSelectionMenu() {
        inv = Bukkit.createInventory(this, 18, "Dungeon Selection");
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
        this.getInventory().setItem(9, createGuiItem(Material.REDSTONE_BLOCK, "§cClose Menu"));
        this.getInventory().setItem(17, createGuiItem(Material.EMERALD_BLOCK, "§2Create New Dungeon"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(this.getInventory().getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                DungeonCreationMenu menu = new DungeonCreationMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            }
        }
    }
}
