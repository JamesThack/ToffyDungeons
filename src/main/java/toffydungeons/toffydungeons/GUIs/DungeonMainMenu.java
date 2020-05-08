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
import toffydungeons.toffydungeons.GUIs.DungeonLayout.DungeonCreationMenu;
import toffydungeons.toffydungeons.GUIs.DungeonRoomDesign.DungeonRoomSelector;

import java.util.ArrayList;
import java.util.Arrays;

public class DungeonMainMenu implements InventoryHolder, Listener {

    private Inventory inventory;

    public DungeonMainMenu() {
        this.inventory = Bukkit.createInventory(this, 54, "Toffy Dungeons");
    }

    public void initaliseItems() {
        this.inventory.setItem(10, createGuiItem(Material.GOLD_PICKAXE, "§6Dungeon Editor"));
        this.inventory.setItem(12, createGuiItem(Material.EMERALD_BLOCK, "§2Dungeon Creator"));
        this.inventory.setItem(14, createGuiItem(Material.PAPER, "§3Blueprint Designer"));
        this.inventory.setItem(16, createGuiItem(Material.WOOD_DOOR, "§2Blueprint Creator"));
        this.inventory.setItem(28, createGuiItem(Material.MOB_SPAWNER, "§cActive Dungeons"));
        this.inventory.setItem(45, createGuiItem(Material.REDSTONE_BLOCK, "§cClose Menu"));
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }


    public static ItemStack createGuiItem(Material material, int materialData, String name, String...lore) {
        ItemStack item = new ItemStack(material, 1, (short) materialData);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metalore = new ArrayList<String>(Arrays.asList(lore));
        meta.setLore(metalore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createGuiItem(Material material, String name, String...lore) {
        return createGuiItem(material, 0 , name, lore);
    }

    @EventHandler
    public void onOpen(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("Toffy Dungeons")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.GOLD_PICKAXE)) {
                DungeonSelectionMenu menu = new DungeonSelectionMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            }if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                DungeonCreationMenu menu = new DungeonCreationMenu();
                menu.updateLayout();
                e.getWhoClicked().openInventory(menu.getInventory());
            }
            if (e.getCurrentItem() != null &&  e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                e.getWhoClicked().closeInventory();
            }

            if (e.getCurrentItem() != null &&  e.getCurrentItem().getType().equals(Material.PAPER)) {
                DungeonRoomSelector selector = new DungeonRoomSelector();
                selector.initaliseItems();
                e.getWhoClicked().openInventory(selector.getInventory());
            }
            if (e.getCurrentItem() != null &&  e.getCurrentItem().getType().equals(Material.WOOD_DOOR)) {
                e.getWhoClicked().closeInventory();
                Bukkit.dispatchCommand(e.getWhoClicked(), "tdungeon roomcreate");
            }

            if (e.getCurrentItem() != null &&  e.getCurrentItem().getType().equals(Material.MOB_SPAWNER)) {
                ActiveDungeonMenu selector = new ActiveDungeonMenu();
                selector.initialiseItems();
                e.getWhoClicked().openInventory(selector.getInventory());
            }
        }
    }
}
