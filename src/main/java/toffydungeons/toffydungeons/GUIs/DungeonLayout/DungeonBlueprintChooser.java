package toffydungeons.toffydungeons.GUIs.DungeonLayout;

import toffydungeons.toffydungeons.API.DungeonRoom;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.GUIs.DungeonRoomDesign.DungeonRoomSelector;

public class DungeonBlueprintChooser extends DungeonRoomSelector {

    private DungeonRoomLayout layout;
    private DungeonRoom room;

    public DungeonBlueprintChooser(DungeonRoomLayout layout, DungeonRoom room) {
        super("Blueprint Selection", "rooms");
        this.layout = layout;
        this.room = room;
    }

    public DungeonBlueprintChooser(int page, DungeonRoomLayout layout, DungeonRoom room) {
        super("Blueprint Selection", "rooms", page);
        this.layout = layout;
        this.room = room;
        initialiseItems();
    }

    public DungeonRoomLayout getLayout() {
        return layout;
    }

    public DungeonRoom getRoom() {
        return room;
    }

}