package toffydungeons.toffydungeons.GUIs.DungeonLayout;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.API.FileSaving;
import toffydungeons.toffydungeons.GUIs.DungeonMainMenu;

import java.io.File;

public class GenerationEvents implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("Dungeon Generation")) {
            e.setCancelled(true);
            DungeonGenerationMenu dungeonMenu = (DungeonGenerationMenu) e.getInventory().getHolder();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                DungeonCreationMenu menu = new DungeonCreationMenu(dungeonMenu.layout , new int[]{0,0});
                menu.updateLayout();
                e.getWhoClicked().openInventory(menu.getInventory());
             } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.LAPIS_BLOCK)) {
                dungeonMenu.layout.generateBuild(e.getWhoClicked().getLocation());
            } if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EGG)) {
                ItemStack storedSchem = DungeonMainMenu.createGuiItem(Material.EGG, "§6Dungeon Egg: " + dungeonMenu.layout.dungeonName);
                e.getWhoClicked().getInventory().addItem(storedSchem);
            }
        }
    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent e) {
        Player player = (Player) e.getEntity().getShooter();
        String eggName = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
        if (eggName != null && player.getInventory().getItemInMainHand().getType() == Material.EGG && eggName.contains("§6Dungeon Egg: ")) {
            e.getEntity().setCustomName("dungeon.egg." + eggName.replace("§6Dungeon Egg: ", ""));
        }
    }

    @EventHandler
    public void onPlace(PlayerEggThrowEvent e) {
        System.out.println(e.getEgg().getName());
        if (e.getEgg().getName().contains("dungeon.egg.")) {
            System.out.println("2");
            DungeonRoomLayout layout = DungeonRoomLayout.deserialise(FileSaving.readLines("dungeons" + File.separator + e.getEgg().getName().replace("dungeon.egg.", "") + ".dungeon"));
            layout.generateBuild(e.getEgg().getLocation());
        }
    }
}
