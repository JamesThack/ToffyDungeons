package toffydungeons.toffydungeons.GUIs.extendable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class AbstractVanityMenu implements InventoryHolder {

    private Inventory inv;

    public AbstractVanityMenu(String title, int size) {
        this.inv = Bukkit.createInventory(this, size, title);
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }

    public void initaliseItems() {
    }

    public ItemStack createGuiItem(Material material, int materialData, String name, String...lore) {
        ItemStack item = new ItemStack(material, 1, (short) materialData);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metalore = new ArrayList<String>(Arrays.asList(lore));
        meta.setLore(metalore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createGuiItem(Material material, String name, String...lore) {
        return createGuiItem(material, 0, name, lore);
    }
}
