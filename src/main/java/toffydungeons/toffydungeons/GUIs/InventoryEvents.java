package toffydungeons.toffydungeons.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.API.FileSaving;
import toffydungeons.toffydungeons.GUIs.DungeonLayout.DungeonBlueprintChooser;
import toffydungeons.toffydungeons.GUIs.DungeonLayout.DungeonCreationMenu;
import toffydungeons.toffydungeons.GUIs.DungeonLayout.DungeonGenerationMenu;
import toffydungeons.toffydungeons.GUIs.DungeonLayout.DungeonRoomManager;
import toffydungeons.toffydungeons.GUIs.DungeonRoomDesign.DungeonRoomSelector;

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
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                DungeonMainMenu menu = new DungeonMainMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                DungeonCreationMenu menuNew = new DungeonCreationMenu();
                menuNew.updateLayout();
                e.getWhoClicked().openInventory(menuNew.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.SMOOTH_BRICK)) {
                DungeonRoomLayout layout = DungeonRoomLayout.deserialise(FileSaving.readLines("dungeons" + File.separator + e.getCurrentItem().getItemMeta().getDisplayName()));
                DungeonCreationMenu menu = new DungeonCreationMenu(layout, new int[]{0, 0}, e.getCurrentItem().getItemMeta().getDisplayName().replace(".dungeon", ""));
                menu.updateLayout();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.PAPER)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Previous Page") && holder.getPage() > 1) {
                    DungeonSelectionMenu menu = new DungeonSelectionMenu(holder.getPage() - 1);
                    e.getWhoClicked().openInventory(menu.getInventory());
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Next Page")) {
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
            int z = e.getSlot() % 9 + main.panDistance[0];
            int x = (int) e.getSlot() / 9 + main.panDistance[1];
            int[] position = new int[]{z, x};
            if (e.getClick().equals(ClickType.MIDDLE)) {
                main.panDistance[0] = main.panDistance[0] + (e.getSlot() % 9) - 4;
                main.panDistance[1] = main.panDistance[1] + ((int) e.getSlot() / 9) - 2;
                DungeonCreationMenu menu = new DungeonCreationMenu(main.layout, main.panDistance);
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                DungeonSelectionMenu menu = new DungeonSelectionMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.LAPIS_BLOCK)) {
                if (e.getClick().equals(ClickType.SHIFT_LEFT)) {
                    main.layout.generateBuild(e.getWhoClicked().getLocation());
                } else {
                    main.layout.setCachedView(main.panDistance);
                    DungeonGenerationMenu menu = new DungeonGenerationMenu(main.layout);
                    menu.initialiseItems();
                    e.getWhoClicked().openInventory(menu.getInventory());
                }
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.AIR)) {
                DungeonRoom newRoom = new DungeonRoom("ExampleRoom", position);
                for (DungeonRoom selected : main.layout.getRooms()) {
                    if (selected.getPosition()[0] + 1 == newRoom.getPosition()[0] && newRoom.getPosition()[1] == selected.getPosition()[1] && selected.getBlockedSides()[1] == 0 && newRoom.getBlockedSides()[3] == 0) {
                        selected.setRight(newRoom);
                        newRoom.setLeft(selected);
                    } else if (selected.getPosition()[0] - 1 == newRoom.getPosition()[0] && newRoom.getPosition()[1] == selected.getPosition()[1] && selected.getBlockedSides()[3] == 0 && newRoom.getBlockedSides()[1] == 0) {
                        selected.setLeft(newRoom);
                        newRoom.setRight(selected);
                    } else if (selected.getPosition()[1] + 1 == newRoom.getPosition()[1] && newRoom.getPosition()[0] == selected.getPosition()[0] && selected.getBlockedSides()[2] == 0 && newRoom.getBlockedSides()[0] == 0) {
                        selected.setBehind(newRoom);
                        newRoom.setForward(selected);
                    } else if (selected.getPosition()[1] - 1 == newRoom.getPosition()[1] && newRoom.getPosition()[0] == selected.getPosition()[0] && selected.getBlockedSides()[0] == 0 && newRoom.getBlockedSides()[2] == 0) {
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
                FileSaving.writeFile(("dungeons" + File.separator + main.getDungeonName() + ".dungeon"), main.serialise());

            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_TORCH_ON)) {
                FileSaving.deleteFile("dungeons" + File.separator + main.getDungeonName() + ".dungeon");
                DungeonSelectionMenu menu = new DungeonSelectionMenu();
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && (e.getCurrentItem().getType().equals(Material.SMOOTH_BRICK) || e.getCurrentItem().getType().equals(Material.BRICK))) {
                if (e.getClick().equals(ClickType.LEFT)) {
                    main.layout.setCachedView(main.panDistance);
                    DungeonRoomManager manager = new DungeonRoomManager(main.layout.getRoomFromPosition(position), main.layout);
                    manager.initialiseItems();
                    e.getWhoClicked().openInventory(manager.getInventory());
                }
                if (e.getClick().equals(ClickType.RIGHT)) {
                    main.layout.removeRoomFromPosition(position);
                    main.updateLayout();
                    DungeonCreationMenu menu = new DungeonCreationMenu(main.layout, main.panDistance, main.getDungeonName());
                    menu.updateLayout();
                    e.getWhoClicked().openInventory(menu.getInventory());
                }
            }

            /**
             * All of the code for the blueprint selection screen
             */

        } else if (e.getView().getTitle().contains("Blueprint Selection")) {
            e.setCancelled(true);
            DungeonBlueprintChooser chooser = (DungeonBlueprintChooser) e.getInventory().getHolder();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.PAPER)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Previous Page") && chooser.getPage() > 1) {
                    DungeonBlueprintChooser menu = new DungeonBlueprintChooser(chooser.getPage() - 1, chooser.getLayout(), chooser.getRoom());
                    e.getWhoClicked().openInventory(menu.getInventory());
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Next Page")) {
                    DungeonBlueprintChooser menu = new DungeonBlueprintChooser(chooser.getPage() + 1, chooser.getLayout(), chooser.getRoom());
                    e.getWhoClicked().openInventory(menu.getInventory());
                }
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                DungeonRoomManager manny = new DungeonRoomManager(chooser.getRoom(), chooser.getLayout());
                manny.initialiseItems();
                e.getWhoClicked().openInventory(manny.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.WOOL)) {
                chooser.getRoom().setSchematicFile(e.getCurrentItem().getItemMeta().getDisplayName());
                DungeonRoomManager manny = new DungeonRoomManager(chooser.getRoom(), chooser.getLayout());
                manny.initialiseItems();
                e.getWhoClicked().openInventory(manny.getInventory());
            }

            /**
             * All of the code for the room customisation
             */

        } else if (e.getView().getTitle().contains("Room Manager")) {
            e.setCancelled(true);
            DungeonRoomManager manager = (DungeonRoomManager) e.getInventory().getHolder();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.PAPER)) {
                DungeonBlueprintChooser chooser = new DungeonBlueprintChooser(manager.getLayout(), manager.getRoom());
                chooser.initaliseItems();
                e.getWhoClicked().openInventory(chooser.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                DungeonCreationMenu creationMenu = new DungeonCreationMenu(manager.getLayout(), manager.getLayout().getCachedView());
                creationMenu.initaliseItems();
                creationMenu.layout.updateBorders();
                e.getWhoClicked().openInventory(creationMenu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.BRICK)) {
                manager.getLayout().setStartingRoom(manager.getRoom());
                DungeonRoomManager newManny = new DungeonRoomManager(manager.getRoom(), manager.getLayout());
                newManny.initialiseItems();
                e.getWhoClicked().openInventory(newManny.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                int[] magicalList = new int[]{13, 23, 31, 21};
                int[] newBlock = manager.getRoom().getBlockedSides();
                for (int i = 0; i < magicalList.length; i++) {
                    if (magicalList[i] == e.getSlot() && manager.getRoom().getBlockedSides()[i] == 1 && !(manager.getLayout().getStartingRoom().equals(manager.getRoom()) && i == 2)){
                        newBlock[i] = 0;
                        manager.getRoom().setBlockedSides(newBlock);
                        manager.getLayout().updateBorders();
                    } else if (magicalList[i] == e.getSlot() && manager.getRoom().getBlockedSides()[i] == 0) {
                        newBlock[i] = 1;
                        manager.getRoom().setBlockedSides(newBlock);
                        if (manager.getRoom().getForward() != null && i == 0) {
                            manager.getRoom().getForward().setBehind(null);
                            manager.getRoom().setForward(null);
                        } else if (manager.getRoom().getRight() != null && i == 1) {
                            manager.getRoom().getRight().setLeft(null);
                            manager.getRoom().setRight(null);
                        } else if (manager.getRoom().getBehind() != null && i == 2) {
                            manager.getRoom().getBehind().setForward(null);
                            manager.getRoom().setBehind(null);
                        } else if (manager.getRoom().getLeft() != null && i == 3) {
                            manager.getRoom().getLeft().setRight(null);
                            manager.getRoom().setLeft(null); }
                    }
                    DungeonRoomManager newManny = new DungeonRoomManager(manager.getRoom(), manager.getLayout());
                    newManny.initialiseItems();
                    e.getWhoClicked().openInventory(newManny.getInventory());
                }
            }
            /**
             * All the code for the blueprint selection menu
             */
        } else if (e.getView().getTitle().contains("Dungeon Blueprint Editor Page")) {
            e.setCancelled(true);
            DungeonRoomSelector chooser = (DungeonRoomSelector) e.getInventory().getHolder();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.PAPER)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Previous Page") && chooser.getPage() > 1) {
                    DungeonRoomSelector menu = new DungeonRoomSelector(chooser.getPage() - 1);
                    menu.initaliseItems();
                    e.getWhoClicked().openInventory(menu.getInventory());
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Next Page")) {
                    DungeonRoomSelector menu = new DungeonRoomSelector(chooser.getPage() + 1);
                    menu.initaliseItems();
                    e.getWhoClicked().openInventory(menu.getInventory());
                }
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.WOOL) && e.getCurrentItem().getItemMeta() != null && e.getClick().isShiftClick()) {
                FileSaving.deleteFile("rooms" + File.separator + e.getCurrentItem().getItemMeta().getDisplayName() + ".schematic");
                FileSaving.deleteFile("rooms" + File.separator + e.getCurrentItem().getItemMeta().getDisplayName() + ".placement");
                DungeonRoomSelector menu = new DungeonRoomSelector(chooser.getPage());
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.WOOL) && e.getCurrentItem().getItemMeta() != null) {
                Bukkit.dispatchCommand(e.getWhoClicked(), "tdungeon roomedit " + e.getCurrentItem().getItemMeta().getDisplayName());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                DungeonMainMenu mainMenu = new DungeonMainMenu();
                mainMenu.initaliseItems();
                e.getWhoClicked().openInventory(mainMenu.getInventory());
            }

            /**
             * All the code for the active dungeon menu
             */
        } else if (e.getView().getTitle().contains("Active Dungeons Page")) {
            e.setCancelled(true);
            ActiveDungeonMenu chooser = (ActiveDungeonMenu) e.getInventory().getHolder();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.PAPER)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Previous Page") && chooser.getPage() > 1) {
                    ActiveDungeonMenu menu = new ActiveDungeonMenu(chooser.getPage() - 1);
                    menu.initialiseItems();
                    e.getWhoClicked().openInventory(menu.getInventory());
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Next Page")) {
                    ActiveDungeonMenu menu = new ActiveDungeonMenu(chooser.getPage() + 1);
                    menu.initialiseItems();
                    e.getWhoClicked().openInventory(menu.getInventory());
                }
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.SMOOTH_BRICK)) {
                ActiveDungeonInfo mainMenu = new ActiveDungeonInfo(e.getCurrentItem().getItemMeta().getDisplayName().replace(".adungeon", ""));
                mainMenu.initaliseItems();
                e.getWhoClicked().openInventory(mainMenu.getInventory());
            }else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                DungeonMainMenu mainMenu = new DungeonMainMenu();
                mainMenu.initaliseItems();
                e.getWhoClicked().openInventory(mainMenu.getInventory());
            }

            /**
             * All the code for active dungeon editor
             */
        } else if (e.getView().getTitle().contains("Active Dungeon Editor: ")) {
            e.setCancelled(true);
            ActiveDungeonInfo chooser = (ActiveDungeonInfo) e.getInventory().getHolder();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_TORCH_ON)) {
                String dungeon = chooser.getTitle().replace("Active Dungeon Editor: ", "");
                DungeonRoomLayout.unloadRoom(dungeon);
                FileSaving.deleteFile("active_dungeons" + File.separator + dungeon  +".adungeon");
                ActiveDungeonMenu menu = new ActiveDungeonMenu(1);
                menu.initialiseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            }
        }
    }
}
