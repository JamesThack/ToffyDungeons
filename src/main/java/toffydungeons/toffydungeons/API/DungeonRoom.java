package toffydungeons.toffydungeons.API;

/**
 * This is the dungeon room class, used by the layout it organises how the dungeon should look. Within the dungeon rooms
 * the schematic is stored as well as the position (relative to the layout) and the neighbouring rooms.
 */
public class DungeonRoom {

    private String schematicFile;
    private int position;
    private DungeonRoom left;
    private DungeonRoom right;
    private DungeonRoom forward;
    private DungeonRoom behind;

    public DungeonRoom(String schematicFile, int position) {
        this.position = position;
        this.schematicFile = schematicFile;
    }

    public int getPosition() {
        return position;
    }

    public DungeonRoom getLeft() {
        return left;
    }

    public void setLeft(DungeonRoom left) {
        this.left = left;
    }

    public DungeonRoom getRight() {
        return right;
    }

    public void setRight(DungeonRoom right) {
        this.right = right;
    }

    public DungeonRoom getForward() {
        return forward;
    }

    public void setForward(DungeonRoom forward) {
        this.forward = forward;
    }

    public DungeonRoom getBehind() {
        return behind;
    }

    public void setBehind(DungeonRoom behind) {
        this.behind = behind;
    }
}
