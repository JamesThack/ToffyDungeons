package toffydungeons.toffydungeons.GUIs.ProceduralGeneration;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import toffydungeons.toffydungeons.API.BoundingBox;
import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.API.FileSaving;
import toffydungeons.toffydungeons.GUIs.DungeonLayout.DungeonCreationMenu;

public class ProceduralEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof ProceduralMainMenu) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.EMERALD)) {
                DungeonRoomLayout layout = genNewRandomLayout(14);
                layout.updateBorders();
                DungeonCreationMenu menu = new DungeonCreationMenu(layout, new int[]{0,0}, "Procedural");
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            }
        }
    }

    private DungeonRoomLayout genNewRandomLayout(int maxRooms) {
        DungeonRoomLayout layout = new DungeonRoomLayout();
        DungeonRoom start = new DungeonRoom(getRandomRoom(4, 4), new int[]{4,2});
        layout.addRoom(start);
        layout.setStartingRoom(start);
        start.setBlockedSides(new int[] {0,0,0,0});
        int[] workFromCoords = new int[]{4,2};
        DungeonRoom cachedDungeonRoom = null;
        int goDir = BoundingBox.getRandomNumber(0,4);
        int strand = 0;
        int corner = 0;
        int i = 0;
        int timeOut = 0;
        while (i <= maxRooms){
            if (timeOut ==6) {
                workFromCoords = layout.getRooms().get(BoundingBox.getRandomNumber(0, layout.getRooms().size())).getPosition();
                timeOut = 0;
            }
            if (BoundingBox.getRandomNumber(0, 6 - corner) == 0) {
                layout.getRoomFromPosition(workFromCoords).setSchematicFile(getRandomRoom(4,4));
                goDir = BoundingBox.getRandomNumber(0,4);
                if (cachedDungeonRoom != null) {
                    int[] block = cachedDungeonRoom.getBlockedSides();
                    block[goDir] = 0;
                    cachedDungeonRoom.setBlockedSides(block);
                }
                corner = 0;
            } else if (BoundingBox.getRandomNumber(0, 8 - strand) == 0) {
                strand = 0;
                goDir = BoundingBox.getRandomNumber(0,4);
                workFromCoords = new int[]{4,2};
            } else {
                if (cachedDungeonRoom != null) {
                    int[] block = cachedDungeonRoom.getBlockedSides();
                    block[goDir] = 0;
                    cachedDungeonRoom.setBlockedSides(block);
                }
            }
            int[] cachedCoords = new int[]{workFromCoords[0], workFromCoords[1]};
            switch(goDir) {
                case 0:
                    workFromCoords = new int[]{workFromCoords[0], workFromCoords[1] - 1};
                    break;
                case 1:
                    workFromCoords = new int[]{workFromCoords[0] + 1, workFromCoords[1]};
                    break;
                case 2:
                    workFromCoords = new int[]{workFromCoords[0], workFromCoords[1] + 1};
                    break;
                case 3:
                    workFromCoords = new int[]{workFromCoords[0] - 1, workFromCoords[1]};
                    break;
            }
            if (layout.getRoomFromPosition(workFromCoords) == null) {
                DungeonRoom add = new DungeonRoom(getRandomRoom(2, 2), workFromCoords);
                add.setBlockedSides(new int[]{1,1,1,1});
                int[] newBlock = add.getBlockedSides();
                newBlock[flipFour(goDir)] = 0;
                add.setBlockedSides(newBlock);
                layout.addRoom(add);
                cachedDungeonRoom = add;
                strand +=1;
                corner +=1;
                i+=1;
            } else {
                workFromCoords = new int[]{cachedCoords[0], cachedCoords[1]};
                timeOut +=1;
            }

        }

        for (DungeonRoom current : layout.getRooms()) {
            if (current.getOpenSideCount() == 1) {
                current.setSchematicFile(getRandomRoom(1, 1));
            }
        }
        return layout;
    }

    private int flipFour(int flip) {
        flip +=2;
        if (flip==4) flip = 0;
        if (flip==5) flip = 1;
        return flip;
    }

    private String getRandomRoom(int min, int max) {
        int rando = BoundingBox.getRandomNumber(0, FileSaving.filesInDirectory("rooms").size());
        for (int timeOut = 0 ; timeOut < 250; timeOut++) {
            DungeonRoom room = new DungeonRoom(FileSaving.filesInDirectory("rooms").get(rando).replace(".placement", "").replace(".schematic", ""), new int[]{0,0}, 1);
            if (room.getOpenSideCount() >=min && room.getOpenSideCount() <=max) return room.getSchematicFile();
        rando = BoundingBox.getRandomNumber(0, FileSaving.filesInDirectory("rooms").size());
    }
        return FileSaving.filesInDirectory("rooms").get(rando).replace(".placement", "").replace(".schematic", "");
    }


}
