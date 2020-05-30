package toffydungeons.toffydungeons.GUIs.DungeonTraps;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import toffydungeons.toffydungeons.GUIs.extendable.AbstractVanityMenu;

public class DungeonTrapType extends AbstractVanityMenu  implements Listener {

    public DungeonTrapType() {
        super("Dungeon Type", 27);
    }

    @Override
    public void initaliseItems() {
        this.getInventory().setItem(10, createGuiItem(Material.MONSTER_EGG, "§cMob Trap"));
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof DungeonTrapType) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.MONSTER_EGG)) {
                MobTrap mobTrap = new MobTrap();
                mobTrap.initaliseItems();
                e.getWhoClicked().openInventory(mobTrap.getInventory());
            }
        }
    }
}