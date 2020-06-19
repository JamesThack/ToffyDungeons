package toffydungeons.toffydungeons.DungeonDesign;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import toffydungeons.toffydungeons.API.BoundingBox;
import toffydungeons.toffydungeons.CurrentEvents.ConstantEvents;
import toffydungeons.toffydungeons.GUIs.DungeonRoomDesign.DungeonRoomWandCustomiser;
import toffydungeons.toffydungeons.GUIs.DungeonTraps.PlaceTrapConstant;
import toffydungeons.toffydungeons.GUIs.extendable.AbstractVanityMenu;

import java.util.ArrayList;
import java.util.List;

public class DungeonDesignEvents implements Listener {

    /**
     * Handles a list of editors that are currently active for all players on the server
     */
    public ArrayList<DungeonRoomDesign> currentEdits;

    public DungeonDesignEvents() {
        this.currentEdits = new ArrayList<>();
    }

    public static boolean compareLocations(Location loc1, Location loc2) {
        if (loc1 !=null && loc2 != null)
            return (loc1.getX() == loc2.getX() && loc1.getY() == loc2.getY() && loc1.getZ() == loc2.getZ());
        return false;
    }

    public void startNewDungeonEditor(Player player) {
        if (getPlayerEditor(player) == null) {
            DungeonRoomDesign design = new DungeonRoomDesign(player);
            this.currentEdits.add(design);
            player.sendMessage("§a[Toffy Dungeons]: You have started creating a new room!");
        } else {
            player.sendMessage("§c[Toffy Dungeons]: You already have an editor open!");
        }
    }

    public void closeEditor(Player player) {
        if (getPlayerEditor(player) != null) {
            this.currentEdits.remove(getPlayerEditor(player));
            player.sendMessage("§a[Toffy Dungeons]: Closed editor WITHOUT SAVING");
        } else {
            player.sendMessage("§c[Toffy Dungeons]: No editor open!");
        }

    }

    public void startNewDungeonEditor(Player player, String editName) {
        if (getPlayerEditor(player) == null) {
            DungeonRoomDesign design = new DungeonRoomDesign(player, editName);
            this.currentEdits.add(design);
            player.sendMessage("§a[Toffy Dungeons]: You have started editing " + editName);
        } else {
            player.sendMessage("§c[Toffy Dungeons]: You already have an editor open!");
        }
    }

    public DungeonRoomDesign getPlayerEditor(Player player) {
        for (DungeonRoomDesign design : currentEdits) {
            if (design.editorContainsPlayer(player))
                return  design;
        } return null;
    }

    public static  int[] getRelativeLocation(DungeonRoomDesign design, Location validLoc) {
        Location origin = design.getOrigin();
        Location endPoint = design.getEndPoint();
        Location loc1 = new Location(origin.getWorld(), Math.min(origin.getX(), endPoint.getX()), Math.min(origin.getY(), endPoint.getY()), Math.min(origin.getZ(), endPoint.getZ()));
        Location loc2 = new Location(origin.getWorld(), Math.max(origin.getX(), endPoint.getX()), Math.max(origin.getY(), endPoint.getY()), Math.max(origin.getZ(), endPoint.getZ()));
        if (ConstantEvents.getDirection(loc1, loc2).equals("posx")) {
            return new int[]{design.getSouthDoor().getBlockZ() - validLoc.getBlockZ(), validLoc.getBlockY() - design.getSouthDoor().getBlockY(), validLoc.getBlockX() - design.getSouthDoor().getBlockX()};
        } else if (ConstantEvents.getDirection(loc1, loc2).equals("negx")) {
            return new int[]{validLoc.getBlockZ() - design.getSouthDoor().getBlockZ(), validLoc.getBlockY() - design.getSouthDoor().getBlockY(), design.getSouthDoor().getBlockX() - validLoc.getBlockX()};
        } else if (ConstantEvents.getDirection(loc1, loc2).equals("posz")) {
            return new int[]{validLoc.getBlockX() - design.getSouthDoor().getBlockX(), validLoc.getBlockY() - design.getSouthDoor().getBlockY(), validLoc.getBlockZ() - design.getSouthDoor().getBlockZ()};
        } else if (ConstantEvents.getDirection(loc1, loc2).equals("negz")) {
            return new int[]{design.getSouthDoor().getBlockX() - validLoc.getBlockX(), validLoc.getBlockY() - design.getSouthDoor().getBlockY(), design.getSouthDoor().getBlockZ() -  validLoc.getBlockZ()};
        }
        return new int[]{0,0,0};
    }

    public static Location locFromCoords(int[] coords, DungeonRoomDesign design) {
       Location loc1 = design.getOrigin().clone();
       Location loc2 = design.getEndPoint().clone();
       Location spawnLoc = design.getSouthDoor().clone();
        if (ConstantEvents.getDirection(loc1, loc2).equals("posx")) {
            return spawnLoc.add(coords[2], coords[1], -coords[0]);
        } else if (ConstantEvents.getDirection(loc1, loc2).equals("negx")) {
            return spawnLoc.add(-coords[2], coords[1], coords[0]);
        } else if (ConstantEvents.getDirection(loc1, loc2).equals("posz")) {
            return spawnLoc.add(coords[0], coords[1], coords[2]);
            //return new int[]{player.getLocation().getBlockX() - Integer.valueOf(splitLine[2]), player.getLocation().getBlockY() - Integer.valueOf(splitLine[3]), player.getLocation().getBlockZ() - Integer.valueOf(splitLine[4])};
        } else if (ConstantEvents.getDirection(loc1, loc2).equals("negz")) {
            return spawnLoc.add(-coords[0], coords[1], -coords[2]);
            // return new int[]{Integer.valueOf(splitLine[2]) - player.getLocation().getBlockX(), player.getLocation().getBlockY() - Integer.valueOf(splitLine[3]), Integer.valueOf(splitLine[4]) -  player.getLocation().getBlockZ()};
        } return null;
    }

    @EventHandler
    public void onSpeak(AsyncPlayerChatEvent e) {
        ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
        if (itemInMainHand != null && itemInMainHand.getType().equals(Material.RECORD_9) && getPlayerEditor(e.getPlayer()) != null) {
            DungeonRoomDesign editor = getPlayerEditor(e.getPlayer());
            if (editor.getCurrentOperation() == 5 && editor.getConstant() != null) {
                e.setCancelled(true);
                for (String current : editor.getAdditionalData()) {
                    if(current.contains("SAVE_NAME:") && current.replace("SAVE_NAME", "").contains(e.getMessage().split(" ")[0].replace(".", "  "))) {
                        e.getPlayer().sendMessage("§c[Toffy Dungeons]: That trap name is taken for this room");
                        return;
                    }
                }
                editor.getConstant().setSaveName(e.getMessage().split(" ")[0].replace(".", "  "));
                e.getPlayer().sendMessage("§a[Toffy Dungeons]: Changed trap name");
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        DungeonRoomDesign designer = this.getPlayerEditor(e.getPlayer());
        if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.RECORD_9) && designer != null) {
            e.setCancelled(true);
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && !compareLocations(designer.getOrigin(), e.getClickedBlock().getLocation()) && designer.getCurrentOperation() == 0) {
                designer.setOrigin(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage("§a[Toffy Dungeons]: Changed point 1");
            } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !compareLocations(e.getClickedBlock().getLocation(), designer.getEndPoint()) && designer.getCurrentOperation() == 0) {
                designer.setEndPoint(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage("§a[Toffy Dungeons]: Changed point 2");
            } else if (designer.getCurrentOperation() > 0 &&designer.getCurrentOperation() < 5 && e.getHand().equals(EquipmentSlot.HAND)) {
                designer.setDoor(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage("§a[Toffy Dungeons]: Updated door position!");
            } else if (designer.getCurrentOperation() == 5) {
                if (e.getPlayer().isSneaking() && e.getHand().equals(EquipmentSlot.HAND)) {
                    designer.getConstant().setSpawnLoc(e.getClickedBlock().getLocation());
                    e.getPlayer().sendMessage("§a[Toffy Dungeons]: Updated trap spawn");
                } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    designer.getConstant().setLoc1(e.getClickedBlock().getLocation());
                    e.getPlayer().sendMessage("§a[Toffy Dungeons]: Updated trap region point 1");
                }else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getHand().equals(EquipmentSlot.HAND)) {
                    designer.getConstant().setLoc2(e.getClickedBlock().getLocation());
                    e.getPlayer().sendMessage("§a[Toffy Dungeons]: Updated trap region point 2");
                }
            }

        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        DungeonRoomDesign designer = this.getPlayerEditor(e.getPlayer());
        if (e.getItemDrop().getItemStack().getType().equals(Material.RECORD_9) && designer != null) {
            if (designer.getConstant() != null && designer.getConstant().isValid()) {
                if (designer.getConstant().getSaveName() !=  null) {
                    designer.setAdditionalData(designer.getConstant().saveTrapInfo(designer.getAdditionalData()));
                    e.getPlayer().sendMessage("§a[Toffy Dungeons]: Finished adding the trap");
                    e.setCancelled(true);
                    designer.setCurrentOperation(0);
                    designer.setConstant(null);
                } else {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("§c[Toffy Dungeons]: Please type a valid name for the trap");
                }
            } else if (designer.getConstant() != null && designer.getConstant().isValid()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§c[Toffy Dungeons]: Please select a valid activation area and spawn location");
            } else {
                e.setCancelled(true);
                DungeonRoomWandCustomiser customiser = new DungeonRoomWandCustomiser(designer);
                customiser.initaliseItems();
                e.getPlayer().openInventory(customiser.getInventory());
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("Wand Customiser")) {
            e.setCancelled(true);
            DungeonRoomWandCustomiser customiser = (DungeonRoomWandCustomiser) e.getInventory().getHolder();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.STICK)) {
                customiser.designer.setCurrentOperation(0);
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().sendMessage("§a[Toffy Dungeons]: Select borders (left click point 1, right click point 2)");
            }
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.PAPER)) {
                if (customiser.designer.getName().equals("UNNAMED")) {
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage("§c[Toffy Dungeons]: Choose room name with /tdungeon roomname (name)");
                } else if (customiser.designer.getSouthDoor() == null) {
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage("§c[Toffy Dungeons]: Please select a south door (this is the rooms entrance)");
                } else {
                    if (customiser.designer.getOrigin() != null && customiser.designer.getEndPoint() != null) {
                        customiser.designer.save();
                        this.currentEdits.remove(customiser.designer);
                        e.getWhoClicked().closeInventory();
                        if (!customiser.designer.isEditing())
                            e.getWhoClicked().sendMessage("§a[Toffy Dungeons]: Created new dungeon room: " + customiser.designer.getName());
                        else
                            e.getWhoClicked().sendMessage("§a[Toffy Dungeons]: Finished editing dungeon room: " + customiser.designer.getName());
                    } else {
                        e.getWhoClicked().sendMessage("§c[Toffy Dungeons]: Please select 2 points for the region!");
                    }
                }
            }
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.WOOD_DOOR)) {
                String[] possibilities = new String[]{"North", "East", "West", "South"};
                for (int i = 0; i <possibilities.length; i++) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(possibilities[i])) {
                        customiser.designer.setCurrentOperation(i + 1);
                        e.getWhoClicked().sendMessage("§a[Toffy Dungeons]: Click with the wand where you would like to locate the " + possibilities[i] + " door");
                        e.getWhoClicked().closeInventory();
                    }
                }
            }
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.TRIPWIRE_HOOK)) {
                TrapSelection chooser = new TrapSelection(customiser.designer);
                chooser.initaliseItems();
                e.getWhoClicked().openInventory(chooser.getInventory());
            }

        } else if (e.getInventory().getHolder() instanceof TrapRoomChooser) {
            e.setCancelled(true);
            DungeonRoomDesign design = ((TrapRoomChooser) e.getInventory().getHolder()).getDesign();
            TrapRoomChooser chooser = (TrapRoomChooser) e.getInventory().getHolder();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.SMOOTH_BRICK) && e.getCurrentItem().getItemMeta() != null) {
                design.setCurrentOperation(5);
                PlaceTrapConstant constant = new PlaceTrapConstant(e.getCurrentItem().getItemMeta().getDisplayName());
                design.setConstant(constant);
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().sendMessage("§a[Toffy Dungeons]: Started placing a trap, left/right click to set activation region and sneak click to set trigger location");
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK) && e.getCurrentItem().getItemMeta() != null) {
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.PAPER)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Previous Page") && chooser.getPage() > 1) {
                    TrapRoomChooser menu = new TrapRoomChooser(chooser.getPage() - 1, chooser.getDesign());
                    menu.initialiseItems();
                    e.getWhoClicked().openInventory(menu.getInventory());
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Next Page")) {
                    TrapRoomChooser menu = new TrapRoomChooser(chooser.getPage() + 1, chooser.getDesign());
                    menu.initialiseItems();
                    e.getWhoClicked().openInventory(menu.getInventory());
                }
            }
        } else if (e.getInventory().getHolder() instanceof AbstractVanityMenu && e.getInventory().getTitle().contains("Room Traps")) {
            e.setCancelled(true);
            TrapSelection selector = (TrapSelection) e.getInventory().getHolder();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                TrapRoomChooser chooser = new TrapRoomChooser(selector.getDesign());
                chooser.initialiseItems();
                e.getWhoClicked().openInventory(chooser.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.PAPER) && e.getClick().isShiftClick()) {
                ArrayList<String> additionalData = selector.getDesign().getAdditionalData();
                for (int x = 0; x < additionalData.size(); x++) {
                    if (additionalData.get(x).replace("SAVE_NAME:", "").equals(e.getCurrentItem().getItemMeta().getDisplayName().replace("Room Trap: ", ""))) {
                        additionalData.subList(x, x+5).clear();
                    }
                } TrapSelection newMenu = new TrapSelection(selector.getDesign());
                newMenu.initaliseItems();
                e.getWhoClicked().openInventory(newMenu.getInventory());
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.PAPER) && !e.getClick().isShiftClick()) {
                ArrayList<String> additionalData = selector.getDesign().getAdditionalData();
                for (int x = 0; x < additionalData.size(); x++) {
                    if (additionalData.get(x).replace("SAVE_NAME:", "").equals(e.getCurrentItem().getItemMeta().getDisplayName().replace("Room Trap: ", ""))) {
                        List<String> alpha = additionalData.subList(x, x+5);
                        PlaceTrapConstant constant = new PlaceTrapConstant(alpha.get(1));
                        constant.setSaveName(e.getCurrentItem().getItemMeta().getDisplayName().replace("Room Trap: ", ""));
                        constant.setLoc1(BoundingBox.deseraliseLocation(alpha.get(2).replace("REG1:", "")));
                        constant.setLoc2(BoundingBox.deseraliseLocation(alpha.get(3).replace("REG2:", "")));
                        constant.setSpawnLoc(BoundingBox.deseraliseLocation(alpha.get(4).replace("HAPPEN:", "")));
                        selector.getDesign().setConstant(constant);
                        selector.getDesign().setCurrentOperation(5);
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage("§a[Toffy Dungeons]: Started editing room trap");
                    }
                }
            }
        }
    }
}
