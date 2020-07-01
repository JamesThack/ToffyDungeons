package toffydungeons.toffydungeons.DungeonTraps;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import toffydungeons.toffydungeons.GUIs.extendable.AbstractVanityMenu;

public class DungeonTrapType extends AbstractVanityMenu  implements Listener {

    public DungeonTrapType() {
        super("Dungeon Type", 36);
    }

    @Override
    public void initaliseItems() {
        this.getInventory().setItem(10, createGuiItem(Material.MONSTER_EGG, "§cMob Trap"));
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null)
            this.getInventory().setItem(12, createGuiItem(Material.WOOD_AXE, "§eSchematic Trap"));
        if (Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null)
            this.getInventory().setItem(28, createGuiItem(Material.SLIME_BALL, "§aMythic Mob Trap"));
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof DungeonTrapType) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.MONSTER_EGG)) {
                MobTrap mobTrap = new MobTrap();
                mobTrap.initaliseItems();
                e.getWhoClicked().openInventory(mobTrap.getInventory());
            } if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.SLIME_BALL)) {
                MythicTrap trap = new MythicTrap();
                trap.initaliseItems();
                e.getWhoClicked().openInventory(trap.getInventory());
            } if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.WOOD_AXE)) {
                SchematicTrap schemTrap = new SchematicTrap();
                schemTrap.initaliseItems();
                e.getWhoClicked().openInventory(schemTrap.getInventory());
            }
        }
    }
}