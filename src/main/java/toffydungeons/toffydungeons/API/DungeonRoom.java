package toffydungeons.toffydungeons.API;

import java.io.File;

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

    public DungeonRoom(String schematicFile, int[] position, int rotation) {
        this.position = position;
        this.schematicFile = schematicFile;
        blockedSides = new int[]{0,0,0,0};
        updateBorders(rotation);
    }

    public void updateBorders(int rotation) {
        for (String current : FileSaving.readLines("rooms" + File.separator + schematicFile + ".placement")) {
            String[] split = current.split(",");
            if (current.contains("NORTH") && split[0].replace("NORTH:", "").equals("0") && split[1].equals("0") && split[2].equals("0")) this.blockedSides[0] = 1;
            if (current.contains("EAST") && split[0].replace("EAST:", "").equals("0") && split[1].equals("0") && split[2].equals("0")) this.blockedSides[1] = 1;
            if (current.contains("WEST") && split[0].replace("WEST:", "").equals("0") && split[1].equals("0") && split[2].equals("0")) {
                this.blockedSides[3] = 1;
                break;
            }
        }
        int[] oldBlock = blockedSides.clone();
        if (rotation == 1) blockedSides = new int[]{oldBlock[1], oldBlock[2], oldBlock[3], oldBlock[0]};
        if (rotation == 2) blockedSides = new int[]{oldBlock[2], oldBlock[3], oldBlock[0], oldBlock[1]};
        if (rotation == 3) blockedSides = new int[]{oldBlock[3], oldBlock[0], oldBlock[1], oldBlock[2]};
    }

    public int getOpenSideCount() {
        return 4 - (blockedSides[0] + blockedSides[1] + blockedSides[2] + blockedSides [3]);
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
