package toffydungeons.toffydungeons.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import toffydungeons.toffydungeons.API.CalebWorldEditAPI;

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
        int totalDistance = 0;
        for (String arg : args) {
            Location location = ((Player) sender).getLocation();
            location.setX(location.getX() + totalDistance);
            int distance = CalebWorldEditAPI.tryLoadSchem(arg, location);
            if (distance == -1) {
                sender.sendMessage(ChatColor.RED + "[Toffy Dungeons]: File name " + arg + " not recognised!");
                return true;
            }
            totalDistance += distance;
        }
        return true;
    }

}
