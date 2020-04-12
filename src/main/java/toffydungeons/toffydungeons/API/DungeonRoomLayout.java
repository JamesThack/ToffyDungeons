package toffydungeons.toffydungeons.API;

import java.util.ArrayList;

/**
 * This is the class that organises the layout of every dungeon, it has an arraylist of rooms which each have their neighbouring
 * rooms stored as well as their position. The starting room is the room that the player initialy spawns in.
 */
public class DungeonRoomLayout {

    private ArrayList<DungeonRoom> rooms;
    private DungeonRoom startingRoom;

    public DungeonRoomLayout() {
        this.rooms = new ArrayList<>();
    }

    public void addRoom(DungeonRoom room) {
        if (!validateRoom(room)) {
            this.rooms.add(room);
        }
    }

    public void setStartingRoom(DungeonRoom room) {
        this.startingRoom = room;
    }

    public DungeonRoom getStartingRoom() {
        return startingRoom;
    }

    public boolean validateRoom(DungeonRoom room) {
        return this.rooms.contains(room);
    }

    public int[] getPositions() {
        int[] positions = new int[rooms.size()];
        for (int i = 0; i< rooms.size(); i++) {
            positions[i] = rooms.get(i).getPosition();
        }
        return positions;
    }

}
