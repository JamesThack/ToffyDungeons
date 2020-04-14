package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.API.FileSaving;

import java.io.File;

public class InventoryEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        /**
         * Here is the code for dungeon selection
         */

        if (e.getView().getTitle().contains("Dungeon Selection Page")) {
            DungeonSelectionMenu holder = (DungeonSelectionMenu) e.getInventory().getHolder();
            e.setCancelled(true);
            if (e.getCurrentItem() != null &&  e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                DungeonCreationMenu menuNew = new DungeonCreationMenu();
                e.getWhoClicked().openInventory(menuNew.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.SMOOTH_BRICK)) {
                DungeonRoomLayout layout = DungeonRoomLayout.deserialise(FileSaving.readLines("dungeons" + File.separator + e.getCurrentItem().getItemMeta().getDisplayName())) ;
                DungeonCreationMenu menu = new DungeonCreationMenu(layout, new int[]{0,0}, e.getCurrentItem().getItemMeta().getDisplayName().replace(".dungeon", ""));
                System.out.println(menu.getDungeonName());
                menu.updateLayout();
                e.getWhoClicked().openInventory(menu.getInventory());
            }  else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.PAPER )) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Previous Page") && holder.getPage() > 1) {
                    DungeonSelectionMenu menu = new DungeonSelectionMenu(holder.getPage() - 1);
                    e.getWhoClicked().openInventory(menu.getInventory());
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Next Page")){
                    DungeonSelectionMenu menu = new DungeonSelectionMenu(holder.getPage() + 1);
                    e.getWhoClicked().openInventory(menu.getInventory());
                }
            }

            /**
             * Here is the code for dungeon creation
             */

        } else if (e.getView().getTitle().contains("Dungeon Creation")) {
            e.setCancelled(true);
            DungeonCreationMenu main = (DungeonCreationMenu) e.getInventory().getHolder();
            if (e.getClick().equals(ClickType.MIDDLE)) {
                main.panDistance[0] = main.panDistance[0] + (e.getSlot()%9)-4;
                main.panDistance[1] = main.panDistance[1] + ((int)e.getSlot()/9)-2;
                DungeonCreationMenu menu = new DungeonCreationMenu(main.layout, main.panDistance, main.getDungeonName());
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            }
            else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                DungeonSelectionMenu menu = new DungeonSelectionMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.LAPIS_BLOCK)) {
                main.layout.generateBuild(e.getWhoClicked().getLocation());
                DungeonSelectionMenu menu = new DungeonSelectionMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.AIR)) {
                int z = e.getSlot() % 9 + main.panDistance[0];
                int x =  (int) e.getSlot() / 9 + main.panDistance[1];
                int[] position = new int[]{z,x};
                DungeonRoom newRoom = new DungeonRoom("ExampleRoom", position);
                for (DungeonRoom selected : main.layout.getRooms()) {
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
                main.layout.addRoom(newRoom);
                DungeonCreationMenu menu = new DungeonCreationMenu(main.layout, main.panDistance, main.getDungeonName());
                menu.updateLayout();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                DungeonRoomLayout.deserialise(main.serialise());
                FileSaving.saveFile("dungeons", ("dungeons" + File.separator + main.getDungeonName() +".dungeon"));
                FileSaving.writeFile(("dungeons" + File.separator + main.getDungeonName() +".dungeon"), main.serialise());
                DungeonSelectionMenu menu = new DungeonSelectionMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());

            }
        }
    }
}
