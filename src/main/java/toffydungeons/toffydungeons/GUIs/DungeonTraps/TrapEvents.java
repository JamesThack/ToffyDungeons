package toffydungeons.toffydungeons.GUIs.DungeonTraps;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import toffydungeons.toffydungeons.API.FileSaving;
import toffydungeons.toffydungeons.API.InventoryAPI;
import toffydungeons.toffydungeons.GUIs.DungeonMainMenu;
import toffydungeons.toffydungeons.GUIs.extendable.AbstractFileMenu;

import java.io.File;

public class TrapEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof  MobTrap) {
            e.setCancelled(true);
            MobTrap trap = (MobTrap) e.getInventory().getHolder();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.MONSTER_EGG)) {
                InventoryAPI.givePlayerItem( (Player) e.getWhoClicked(), DungeonMainMenu.createGuiItem(Material.RECORD_9, "§dMob Selector " + trap.getName(), "Trap Editor", "Hold wand and punch mob type"));
                trap.saveData();
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.WHEAT)) {
                InventoryAPI.givePlayerItem( (Player) e.getWhoClicked(), DungeonMainMenu.createGuiItem(Material.RECORD_9, "§dHealth Selector " + trap.getName(), "Trap Editor", "Hold wand and say health in chat"));
                trap.saveData();
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.NAME_TAG)) {
                InventoryAPI.givePlayerItem( (Player) e.getWhoClicked(), DungeonMainMenu.createGuiItem(Material.RECORD_9, "§dName Selector " + trap.getName(), "Trap Editor"));
                trap.saveData();
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                trap.saveData();
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.LAPIS_BLOCK)) {
                EntityType type = EntityType.valueOf(trap.getMobType());
                LivingEntity mob = (LivingEntity) e.getWhoClicked().getWorld().spawnEntity(e.getWhoClicked().getLocation(), type);
                mob.setMaxHealth(trap.getHealth());
                mob.setHealth(trap.getHealth());
                if (trap.getMobName() != null) {
                    mob.setCustomName(trap.getMobName());
                    mob.setCustomNameVisible(true);
                }
            }
        } else if (e.getInventory().getHolder() instanceof AbstractFileMenu && e.getInventory().getTitle().contains("Current Traps")) {
            e.setCancelled(true);
            AbstractFileMenu holder = (AbstractFileMenu) e.getInventory().getHolder();
            if (e.getCurrentItem() != null &&  e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                DungeonMainMenu main = new DungeonMainMenu();
                main.initaliseItems();
                e.getWhoClicked().openInventory(main.getInventory());
            }
            if (!e.getClick().isShiftClick() && e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.SMOOTH_BRICK)) {
                MobTrap oldTrap = new MobTrap(e.getCurrentItem().getItemMeta().getDisplayName().replace(".trap", ""));
                oldTrap.initaliseItems();
                e.getWhoClicked().openInventory(oldTrap.getInventory());
            }
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.PAPER)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Previous Page") && holder.getPage() > 1) {
                    AbstractFileMenu menu = new AbstractFileMenu("Current Traps", "traps", holder.getPage() - 1);
                    menu.initialiseItems();
                    e.getWhoClicked().openInventory(menu.getInventory());
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Next Page")) {
                    AbstractFileMenu menu = new AbstractFileMenu("Current Traps", "traps", holder.getPage() + 1);
                    menu.initialiseItems();
                    e.getWhoClicked().openInventory(menu.getInventory());
                }
            }
            if (e.getClick().isShiftClick() && e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.SMOOTH_BRICK)) {
                FileSaving.deleteFile("traps" + File.separator +  e.getCurrentItem().getItemMeta().getDisplayName());
                AbstractFileMenu menu = new AbstractFileMenu("Current Traps", "traps", holder.getPage());
                menu.initialiseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            }
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            ItemStack held = attacker.getInventory().getItemInMainHand();
            if (held != null && held.getItemMeta() != null && held.getItemMeta().getLore() != null && held.getType().equals(Material.RECORD_9) && held.getItemMeta().getDisplayName().contains("§dMob Selector") && held.getItemMeta().getLore().contains("Trap Editor")) {
                e.setCancelled(true);
                String name = held.getItemMeta().getDisplayName().replace("§dMob Selector ", "");
                MobTrap trap = new MobTrap(name);
                trap.setMobType(e.getEntityType().name());
                trap.initaliseItems();
                attacker.getInventory().setItem(attacker.getInventory().getHeldItemSlot(), null);
                attacker.openInventory(trap.getInventory());
            }
        }
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        ItemStack held = e.getPlayer().getInventory().getItemInMainHand();
        if (held != null && held.getItemMeta()!= null && held.getItemMeta().getLore() != null && held.getType().equals(Material.RECORD_9) && held.getItemMeta().getLore().contains("Trap Editor")) {
            if (held.getItemMeta().getDisplayName().contains("§dHealth Selector")) {
                e.setCancelled(true);
                try {
                    double newHealth = Double.valueOf(e.getMessage());
                    if (newHealth > 2048 || newHealth < 1) {
                        e.getPlayer().sendMessage("§c[Toffy Dungeons]: Sorry that health value is invalid, please select a health value between 0 and 2048");
                    } else {
                        String name = held.getItemMeta().getDisplayName().replace("§dHealth Selector ", "");
                        MobTrap trap = new MobTrap(name);
                        trap.setHealth(newHealth);
                        trap.initaliseItems();
                        e.getPlayer().openInventory(trap.getInventory());
                        e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().getHeldItemSlot(), null);
                    }
                } catch (NumberFormatException er) {
                    e.getPlayer().sendMessage("§c[Toffy Dungeons]: Please enter a numerical value");
                }
            } else if (held.getItemMeta().getDisplayName().contains("§dName Selector")) {
                e.setCancelled(true);
                String name = held.getItemMeta().getDisplayName().replace("§dName Selector ", "");
                MobTrap trap = new MobTrap(name);
                trap.setMobName(e.getMessage().replace("&", "§"));
                trap.initaliseItems();
                e.getPlayer().openInventory(trap.getInventory());
                e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().getHeldItemSlot(), null);
            }
        }
    }
}
