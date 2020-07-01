package toffydungeons.toffydungeons.API;

/**
 * This is the dungeon room class, used by the layout it organises how the dungeon should look. Within the dungeon rooms
 * the schematic is stored as well as the position (relative to the layout) and the neighbouring rooms.
 */
public class DungeonRoom {

    private String schematicFile;
    private int[] position;
    private DungeonRoom left;
    private DungeonRoom right;
    private DungeonRoom forward;
    private DungeonRoom behind;
    private int[] blockedSides;

    public DungeonRoom(String schematicFile, int[] position) {
        this.position = position;
        this.schematicFile = schematicFile;
        blockedSides = new int[]{0,0,0,0};
    }

    public void setSchematicFile(String schematicFile) {
        this.schematicFile = schematicFile;
    }

    public int[] getBlockedSides() {
        return blockedSides;
    }

    public String serialiseBorders() {
        return ("," + blockedSides[0] + "," + blockedSides[1] + "," + blockedSides[2] + "," + blockedSides[3]);
    }

    public void setBlockedSides(int[] blockedSides) {
        this.blockedSides = blockedSides;
    }

    public String getSchematicFile() {
        return schematicFile;
    }

    public int[] getPosition() {
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

    public boolean checkSides() {
        if (this.getForward() != null && this.getForward().getBlockedSides()[2] == 1) return false;
        if (this.getBehind() != null && this.getBehind().getBlockedSides()[0] == 1) return false;
        if (this.getLeft() != null && this.getLeft().getBlockedSides()[3] == 1) return false;
        if (this.getRight() != null && this.getRight().getBlockedSides()[1] == 1) return false;
        return true;
    }
}
