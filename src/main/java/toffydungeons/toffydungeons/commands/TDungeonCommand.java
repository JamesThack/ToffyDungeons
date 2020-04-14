package toffydungeons.toffydungeons.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import toffydungeons.toffydungeons.API.FileSaving;
import toffydungeons.toffydungeons.GUIs.DungeonCreationMenu;
import toffydungeons.toffydungeons.GUIs.DungeonMainMenu;
import toffydungeons.toffydungeons.GUIs.DungeonSelectionMenu;

import java.io.File;

public class TDungeonCommand implements CommandExecutor {

    /**
     * This is the main command when the user does /TDungeon, a list of features (in order) is:
     * Create a new dungeon
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can preform this command");
            return true;
        }
        if (args.length == 0) {
            DungeonMainMenu menu =  new DungeonMainMenu();
            menu.initaliseItems();
            ((Player) sender).openInventory(menu.getInventory());
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("dungeons")) {
            DungeonSelectionMenu menu = new DungeonSelectionMenu();
            menu.initaliseItems();
            ((Player) sender).openInventory(menu.getInventory());
            return true;
        }  else if (args.length == 1 && args[0].equalsIgnoreCase("create")) {
            DungeonCreationMenu menu = new DungeonCreationMenu();
            menu.updateLayout();
            ((Player) sender).openInventory(menu.getInventory());
            return true;
        }   else if (args.length == 3 && args[0].equalsIgnoreCase("rename")) {
            String oldName = args[1];
            String newName = args[2];
            if (newName.contains(".") || oldName.contains(".")) {
                sender.sendMessage("§c[Toffy Dungeons]: Sorry, that name contains illegal characters.");
            } else if (FileSaving.filesInDirectory("dungeons").contains(newName + ".dungeon") ) {
                sender.sendMessage("§c[Toffy Dungeons]: Sorry but that dungeon name is taken.");
            } else if (!FileSaving.filesInDirectory("dungeons").contains(oldName + ".dungeon")) {
                sender.sendMessage("§c[Toffy Dungeons]: Sorry, can not find the specified dungeon.");
            } else {
                try {
                    FileSaving.renameFile(("dungeons" + File.separator + oldName + ".dungeon"), ("dungeons" + File.separator + newName + ".dungeon"));
                    sender.sendMessage("§a[Toffy Dungeons]: The dungeon was successfully renamed!");
                } catch (Exception e) {
                    sender.sendMessage("§cSomething went wrong, please check you typed the name properly or contact a developer.");
                }
            }


            return true;
        }
        return false;
    }

}
