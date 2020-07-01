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
                DungeonRoomLayout layout = genNewRandomLayout(12);
                layout.updateBorders();
//                layout.generateBuild(e.getWhoClicked().getLocation());
                DungeonCreationMenu menu = new DungeonCreationMenu(layout, new int[]{0,0}, "Procedural");
                menu.initaliseItems();
                e.getWhoClicked().openInventory(menu.getInventory());
            }
        }
    }

    private DungeonRoomLayout genNewRandomLayout(int maxRooms) {
        DungeonRoomLayout layout = new DungeonRoomLayout();
        DungeonRoom start = new DungeonRoom(getRandomRoom(), new int[]{4,2});
        layout.addRoom(start);
        layout.setStartingRoom(start);
        for (int i = 0; i < maxRooms; i++) {
            boolean found = false;
            while (!found) {
                DungeonRoom buildOff = layout.getRooms().get(BoundingBox.getRandomNumber(0, layout.getRooms().size()));
                int curTry = BoundingBox.getRandomNumber(0, 3);
                for (int tries = 0; tries < 4; tries++) {
                    if (!checkSpace(layout, buildOff, curTry)) {
                        curTry += 1;
                        if (curTry == 4) curTry = 0;
                    } else {
                        found = true;
                        break;
                    }
                }
            }
        }
        return layout;
    }

    private String getRandomRoom() {
        int rando = BoundingBox.getRandomNumber(0, FileSaving.filesInDirectory("rooms").size());
        return FileSaving.filesInDirectory("rooms").get(rando).replace(".placement", "").replace(".schematic", "");
    }

    private boolean checkSpace(DungeonRoomLayout layout , DungeonRoom room, int side) {
        DungeonRoom checker = room.getForward();
        int[] newPos = new int[]{room.getPosition()[0], room.getPosition()[1] - 1};
        switch(side) {
            case 1:
                newPos = new int[]{room.getPosition()[0] - 1, room.getPosition()[1]};
                checker = room.getRight();
                break;
            case 2:
                newPos = new int[]{room.getPosition()[0], room.getPosition()[1] + 1};
                checker = room.getBehind();
                break;
            case 3:
                newPos = new int[]{room.getPosition()[0] + 1, room.getPosition()[1]};
                checker = room.getRight();
                break;
        }
        if (newPos[0] == 4 && newPos[1] == 3) {
        }
        if (checker == null && layout.getRoomFromPosition(newPos) == null) {
            int other = side + 2;
            if (other ==4) other = 0;
            if (other ==5) other = 1;
            DungeonRoom addRoom = new DungeonRoom(getRandomRoom(), newPos);
            addRoom.setForward(layout.getRoomFromPosition(new int[]{addRoom.getPosition()[0], addRoom.getPosition()[1] - 1}));
            addRoom.setRight(layout.getRoomFromPosition(new int[]{addRoom.getPosition()[0] - 1, addRoom.getPosition()[1]}));
            addRoom.setBehind(layout.getRoomFromPosition(new int[]{addRoom.getPosition()[0], addRoom.getPosition()[1] + 1}));
            addRoom.setLeft(layout.getRoomFromPosition(new int[]{addRoom.getPosition()[0] + 1, addRoom.getPosition()[1]}));
            if (room.getBlockedSides()[side] == 0 && addRoom.getBlockedSides()[other] == 0 && addRoom.checkSides()) {
                layout.addRoom(addRoom);
                layout.updateBorders();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
