package toffydungeons.toffydungeons.API;

import com.sk89q.worldedit.internal.expression.runtime.For;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileSaving {

    public static String dataSource = Bukkit.getPluginManager().getPlugin("ToffyDungeons").getDataFolder() + File.separator;

    public static void saveFile(String fileDirectory, String file) {
        File saveFile = new File(dataSource + fileDirectory);
        File writeFile = new File(dataSource + file);
        saveFile.mkdirs();
        try {
            writeFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String file, ArrayList<String> data) {
        File writeFile = new File(dataSource + file);
        try {
            FileWriter writer = new FileWriter(writeFile);
            for (String line : data) {
                writer.write(line);
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> filesInDirectory(String directory) {
        File listFiles = new File(dataSource + directory);
        ArrayList<String> files = new ArrayList<>();
        try {
            for (File current : Objects.requireNonNull(listFiles.listFiles())) {
                files.add(current.getName());
            }
        } catch(NullPointerException e) {

        }
        return files;
    }
}
