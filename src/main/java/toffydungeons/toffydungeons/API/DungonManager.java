//package toffydungeons.toffydungeons.API;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class DungonManager {
//    private static DungonManager instance;
//
//    private final HashMap<String, DungeonRoomLayout> dungonsLayouts;
//
//    public static DungonManager getInstance() {
//        if(instance == null) {
//            instance = new DungonManager();
//        }
//        return instance;
//    }
//
//    private DungonManager() {
//        this.dungonsLayouts = new HashMap<>();
//    }
//
//    public DungeonRoomLayout getDungonsLayout(String name) {
//        dungonsLayouts.get(name);
//    }
//
//    public boolean addDungonsLayout(String name, DungeonRoomLayout dungeonRoomLayout) {
//        if(dungonsLayouts.containsKey(name)){
//            return  false;
//        }
//        return dungonsLayouts.put(name, dungeonRoomLayout) == null;
//    }
//
//    public boolean delDungonsLayout(String name) {
//        return dungonsLayouts.remove(name) != null;
//    }
//
//    public boolean hasDungonsLayout(String name) {
//        return dungonsLayouts.containsKey(name);
//    }
//
//    public List<String> getDungonsLayouts() {
//        return new ArrayList<>(dungonsLayouts.keySet());
//    }
//}
