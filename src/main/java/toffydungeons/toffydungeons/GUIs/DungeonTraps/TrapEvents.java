package toffydungeons.toffydungeons.GUIs.DungeonTraps;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import toffydungeons.toffydungeons.API.InventoryAPI;
import toffydungeons.toffydungeons.GUIs.DungeonMainMenu;

public class TrapEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof  MobTrap) {
            e.setCancelled(true);
            MobTrap trap = (MobTrap) e.getInventory().getHolder();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.MONSTER_EGG)) {
                InventoryAPI.givePlayerItem( (Player) e.getWhoClicked(), DungeonMainMenu.createGuiItem(Material.RECORD_9, "§dMob Selector " + trap.getName(), "Trap Editor"));
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                trap.saveData();
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.LAPIS_BLOCK)) {
                LivingEntity mob = (LivingEntity) e.getWhoClicked().getWorld().spawnEntity(e.getWhoClicked().getLocation(), EntityType.fromName(trap.getMobType()));
                mob.setMaxHealth(trap.getHealth());
                mob.setHealth(trap.getHealth());
                if (mob.getCustomName() != null) {
                    mob.setCustomName(trap.getMobName());
                    mob.setCustomNameVisible(true);
                }
            }
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            ItemStack held = attacker.getInventory().getItemInMainHand();
            if (held != null && held.getItemMeta().getDisplayName() != null && held.getItemMeta().getLore() != null && held.getType().equals(Material.RECORD_9) && held.getItemMeta().getDisplayName().contains("§dMob Selector") && held.getItemMeta().getLore().contains("Trap Editor")) {
                e.setCancelled(true);
                MobTrap trap = new MobTrap(held.getItemMeta().getDisplayName().replace("§dMob Selector ", ""));
                trap.setMobType(e.getEntityType().name());
                trap.initaliseItems();
                attacker.getInventory().setItem(attacker.getInventory().getHeldItemSlot(), null);
                attacker.openInventory(trap.getInventory());
            }
        }
    }
}
