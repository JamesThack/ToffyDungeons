package toffydungeons.toffydungeons.DungeonTraps;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import toffydungeons.toffydungeons.GUIs.extendable.AbstractVanityMenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SchematicTrap extends AbstractVanityMenu {

    private String Schematic;

    public SchematicTrap() {
        super("New Schematic Trap", 27);
        for (String cur : getSchematics()) {
            System.out.println(cur);
        }
    }

    @Override
    public void initaliseItems() {
        this.getInventory().setItem(13, createGuiItem(Material.LOG, "Schematic Type:"));
    }

    public ArrayList<String> getSchematics() {
        String dataSource = Bukkit.getPluginManager().getPlugin("ToffyDungeons").getDataFolder() + File.separator + "Anims";
        File fileToRead = new File(dataSource);
        ArrayList<String> lines = new ArrayList<>();
        try {
            String readLine;
            BufferedReader reader = new BufferedReader(new FileReader(fileToRead));
            while ((readLine = reader.readLine()) != null) {
                lines.add(readLine);
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
