package toffydungeons.toffydungeons.API;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the class that organises the layout of every dungeon, it has an arraylist of rooms which each have their neighbouring
 * rooms stored as well as their position. The starting room is the room that the player initialy spawns in.
 */
public class DungeonRoomLayout {

    private ArrayList<DungeonRoom> rooms;
    private DungeonRoom startingRoom;
    private ArrayList<DungeonRoom> builtRooms;
    public String dungeonName;
    private int buildTime;

    public DungeonRoomLayout() {
        this.rooms = new ArrayList<>();
    }

    public ArrayList<DungeonRoom> getRooms() {
        return rooms;
    }

    public void addRoom(DungeonRoom room) {
        if (!validateRoom(room)) {
            this.rooms.add(room);
        }
    }

    public void setStartingRoom(DungeonRoom room) {
        this.startingRoom = room;
        int[] newSides = room.getBlockedSides();
        newSides[2] = 1;
        if (room.getBehind() != null) {
            room.getBehind().setForward(null);
            room.setBehind(null);
        }
        room.setBlockedSides(newSides);
    }

    public DungeonRoom getStartingRoom() {
        return startingRoom;
    }

    public void removeRoomFromPosition(int[] position) {
        for (DungeonRoom room : this.rooms) {
            if (Arrays.equals(room.getPosition(), position) && !this.getStartingRoom().equals(room)) {
                this.safeRemoveRoom(room);
                break;
            }
        }
    }

    public DungeonRoom getRoomFromPosition(int[] position) {
        for (DungeonRoom room : this.rooms) {
            if (Arrays.equals(room.getPosition(), position)) {
                return room;
            }
        } return null;
    }

    public boolean validateRoom(DungeonRoom room) {
        return this.rooms.contains(room);
    }

    public ArrayList<int[]> getPositions() {
        ArrayList<int[]> positions = new ArrayList<>();
        for (DungeonRoom room : rooms) {
            positions.add(room.getPosition());
        }
        return positions;
    }

    public void updateBorders() {
        for (DungeonRoom room : this.rooms) {
            for (DungeonRoom roomToCompare : this.rooms) {
                if (room.getPosition()[0] + 1 == roomToCompare.getPosition()[0] && roomToCompare.getPosition()[1] == room.getPosition()[1] && room.getBlockedSides()[1] ==0 && roomToCompare.getBlockedSides()[3] ==0) {
                    room.setRight(roomToCompare);
                    roomToCompare.setLeft(room);
                } else if (room.getPosition()[0] - 1 == roomToCompare.getPosition()[0] && roomToCompare.getPosition()[1] == room.getPosition()[1] && room.getBlockedSides()[3] ==0 && roomToCompare.getBlockedSides()[1] ==0) {
                    room.setLeft(roomToCompare);
                    roomToCompare.setRight(room);
                } else if (room.getPosition()[1] + 1 == roomToCompare.getPosition()[1] && roomToCompare.getPosition()[0] == room.getPosition()[0] && room.getBlockedSides()[2] ==0 && roomToCompare.getBlockedSides()[0] ==0) {
                    room.setBehind(roomToCompare);
                    roomToCompare.setForward(room);
                } else if (room.getPosition()[1] - 1 == roomToCompare.getPosition()[1] && roomToCompare.getPosition()[0] == room.getPosition()[0] && room.getBlockedSides()[0] ==0 && roomToCompare.getBlockedSides()[2] ==0) {
                    room.setForward(roomToCompare);
                    roomToCompare.setBehind(room);
                }
            }
        }
    }

    public void safeRemoveRoom(DungeonRoom room) {
        if (!this.getStartingRoom().equals(room)) {
            if (room.getForward() != null)
                room.getForward().setBehind(null);
            if (room.getBehind() != null)
                room.getBehind().setForward(null);
            if (room.getLeft() != null)
                room.getLeft().setRight(null);
            if (room.getRight() != null)
                room.getRight().setLeft(null);
            this.rooms.remove(room);
        }
    }

    public void generateBuild(Location location) {
        builtRooms = new ArrayList<>();
        buildTime = 0;
        new GenerateBuild("null", startingRoom, location).run();
    }

    public static DungeonRoomLayout deserialise(ArrayList<String> serialisedData) {
        DungeonRoomLayout layout = new DungeonRoomLayout();
        for (String line : serialisedData) {
            if (line.contains("start:")) {
                line = line.substring(6);
                int[] newPos = new int[]{ Integer.valueOf(line.split(",")[0]),Integer.valueOf(line.split(",")[1]) };
                DungeonRoom newRoom = new DungeonRoom("ExampleRoom", newPos);
                newRoom.setBlockedSides(new int[]{Integer.valueOf(line.split(",")[3]),Integer.valueOf(line.split(",")[4]), Integer.valueOf(line.split(",")[5]), Integer.valueOf(line.split(",")[6])});
                layout.addRoom(newRoom);
                newRoom.setSchematicFile(line.split(",")[2]);
                layout.setStartingRoom(newRoom);
            } else if (line.contains("position:")) {
                line = line.substring(9);
                DungeonRoom newRoom = new DungeonRoom("ExampleRoom", new int[]{Integer.valueOf(line.split(",")[0]),Integer.valueOf(line.split(",")[1]) });
                newRoom.setBlockedSides(new int[]{Integer.valueOf(line.split(",")[3]),Integer.valueOf(line.split(",")[4]), Integer.valueOf(line.split(",")[5]), Integer.valueOf(line.split(",")[6])});
                layout.addRoom(newRoom);
                newRoom.setSchematicFile(line.split(",")[2]);
            }
        } layout.updateBorders();
        return layout;
    }

    private boolean isRoomBuild(DungeonRoom room) {
        return builtRooms.contains(room);
    }

    public class GenerateBuild extends BukkitRunnable {

        private String direction;
        private DungeonRoom room;
        private Location coordinates;

        public GenerateBuild(String direction, DungeonRoom room, Location coordinates) {
            this.direction = direction;
            this.room = room;
            this.coordinates = coordinates;
        }

        /**
         * HOLY SHIT this method, I KNOW ITS A MESS OK? This took me a long time to make (at the early hours of the morning)
         * and in all honesty I am scared to optimise it, esentially this is a recursive runnable task (creates new runnables of itself)
         * and it takes a starting room and builds every adjacent room, repeating for every room it constructs (makes the room in front
         * and then does the 3 other directios of that room). It ignores any rooms already created. If any developers want to make this
         * highly inefficient code better please please go ahead but I am sleeping and forgetting I made this.
         */
        @Override
        public void run() {
            if (!isRoomBuild(room)) {
                if (buildTime >= 1) {
                    buildTime -= 1;
                }
                builtRooms.add(room);
                File roomStats = new File(Bukkit.getPluginManager().getPlugin("ToffyDungeons").getDataFolder() + File.separator + "rooms" + File.separator + room.getSchematicFile() + ".placement");
                int[] directions = new int[12];
                GenerateBuild forward = null;
                GenerateBuild right = null;
                GenerateBuild left = null;
                GenerateBuild back = null;
                try {
                    FileReader fr = new FileReader(roomStats);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    int i = 0;
                    while ((line=br.readLine()) != null) {
                        for (String current : new String[]{"NORTH:", "EAST:", "SOUTH:", "WEST:"}) {
                            if (line.contains(current)) {
                                directions[i] = Integer.valueOf(line.split(current)[1].split(",")[0]);
                                directions[i + 1] = Integer.valueOf(line.split(current)[1].split(",")[1]);
                                directions[i + 2] = Integer.valueOf(line.split(current)[1].split(",")[2]);
                                i +=3;
                            }
                        }
                    }
                    if (direction.equals("forward") || direction.equals("null")) {
                        CalebWorldEditAPI.tryLoadSchem(room.getSchematicFile(), coordinates);
                        if (room.getForward() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() + directions[0]);
                            newCoordinates.setY(newCoordinates.getY() + directions[1]);
                            newCoordinates.setZ(newCoordinates.getZ() + directions[2]);
                            forward = new GenerateBuild("forward", room.getForward(), newCoordinates);
                        }
                        if (room.getBehind() != null && !direction.equals("forward")) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() + 1 );
                            newCoordinates.setZ(newCoordinates.getZ() + directions[6]);
                            back = new GenerateBuild("behind", room.getBehind(), newCoordinates);
                        }
                        if (room.getRight() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() + directions[3]);
                            newCoordinates.setY(newCoordinates.getY() + directions[4]);
                            newCoordinates.setZ(newCoordinates.getZ() + directions[5]);
                            right = new GenerateBuild("right", room.getRight(), newCoordinates);
                        }
                        if (room.getLeft() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() + directions[9]);
                            newCoordinates.setY(newCoordinates.getY() + directions[10]);
                            newCoordinates.setZ(newCoordinates.getZ() + directions[11]);
                            left = new GenerateBuild("left", room.getLeft(), newCoordinates);
                        }
                    }
                    if (direction.equals("behind")) {
                        CalebWorldEditAPI.tryLoadSchem(room.getSchematicFile(), coordinates, 180);
                        if (room.getBehind() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() - directions[0]);
                            newCoordinates.setY(newCoordinates.getY() + directions[1]);
                            newCoordinates.setZ(newCoordinates.getZ() - directions[2]);
                            back = new GenerateBuild("behind", room.getBehind(), newCoordinates);
                        }
                        if (room.getRight() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() - directions[9]);
                            newCoordinates.setY(newCoordinates.getY() + directions[10]);
                            newCoordinates.setZ(newCoordinates.getZ() - directions[11]);
                            right = new GenerateBuild("right", room.getRight(), newCoordinates);
                        }
                        if (room.getLeft() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() - directions[3]);
                            newCoordinates.setY(newCoordinates.getY() + directions[4]);
                            newCoordinates.setZ(newCoordinates.getZ() - directions[5]);
                            left = new GenerateBuild("left", room.getLeft(), newCoordinates);
                        }
                    }
                    if (direction.equals("left")) {
                        CalebWorldEditAPI.tryLoadSchem(room.getSchematicFile(), coordinates, 270);
                        CalebWorldEditAPI.tryLoadSchem(room.getSchematicFile(), coordinates, 270);
                        if (room.getForward() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() + directions[5]);
                            newCoordinates.setY(newCoordinates.getY() + directions[4]);
                            newCoordinates.setZ(newCoordinates.getZ() - directions[3]);
                            forward = new GenerateBuild("forward", room.getForward(), newCoordinates);
                        }
                        if (room.getBehind() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() + directions[11] );
                            newCoordinates.setY(newCoordinates.getY() + directions[10]);
                            newCoordinates.setZ(newCoordinates.getZ() - directions[9]);
                            back = new GenerateBuild("behind", room.getBehind(), newCoordinates);
                        }
                        if (room.getLeft() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() + directions[2]);
                            newCoordinates.setY(newCoordinates.getY() + directions[1]);
                            newCoordinates.setZ(newCoordinates.getZ() - directions[0]);
                            left = new GenerateBuild("left", room.getLeft(), newCoordinates);
                        }
                    }
                    if (direction.equals("right")) {
                        CalebWorldEditAPI.tryLoadSchem(room.getSchematicFile(), coordinates, 90);
                        CalebWorldEditAPI.tryLoadSchem(room.getSchematicFile(), coordinates, 90);

                        if (room.getForward() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() - directions[11]);
                            newCoordinates.setY(newCoordinates.getY() + directions[10]);
                            newCoordinates.setZ(newCoordinates.getZ() + directions[9]);
                            forward = new GenerateBuild("forward", room.getForward(), newCoordinates);
                        }

                        if (room.getBehind() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() - directions[5] );
                            newCoordinates.setY(newCoordinates.getY() + directions[4]);
                            newCoordinates.setZ(newCoordinates.getZ() + directions[3]);
                            back = new GenerateBuild("behind", room.getBehind(), newCoordinates);
                        }

                        if (room.getRight() != null) {
                            Location newCoordinates = new Location(coordinates.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());
                            newCoordinates.setX(newCoordinates.getX() - directions[2]);
                            newCoordinates.setY(newCoordinates.getY() + directions[1]);
                            newCoordinates.setZ(newCoordinates.getZ() + directions[0]);
                            right = new GenerateBuild("right", room.getRight(), newCoordinates);
                        }
                    }
                    if (forward != null) {
                        buildTime +=1;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("ToffyDungeons"), forward, 20 * buildTime);
                    }
                    if (back != null) {
                        buildTime +=1;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("ToffyDungeons"), back, 20 * buildTime);
                    }
                    if (right != null) {
                        buildTime +=1;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("ToffyDungeons"), right, 20 * buildTime);
                    }
                    if (left != null) {
                        buildTime +=1;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("ToffyDungeons"), left, 20 * buildTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
