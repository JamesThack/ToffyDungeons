package toffydungeons.toffydungeons.API;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public static ArrayList<String> readLines(String file) {
        File fileToRead = new File(dataSource + file);
        ArrayList<String> lines = new ArrayList<>();
        try {
            String readLine;
            BufferedReader reader = new BufferedReader(new FileReader(fileToRead));
            while ((readLine = reader.readLine()) != null) {
                lines.add(readLine);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static boolean folderContainsFile(String directory, String check) {
        List<String> totalFiles = filesInDirectory(directory);
        for (String str : totalFiles) {
            if (str.equals(check))
                return true;
        }return false;
    }
}
