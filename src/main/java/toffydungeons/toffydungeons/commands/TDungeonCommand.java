package toffydungeons.toffydungeons.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import toffydungeons.toffydungeons.API.CalebWorldEditAPI;

public class TDungeonCommand implements CommandExecutor {

    private Plugin plugin;

    /**
     * Constructor for command, see description below
     *
     * @param plugin The server plugin
     */
    public TDungeonCommand(Plugin plugin) {
        this.plugin = plugin;
    }

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
        if (CalebWorldEditAPI.tryLoadSchem(args[0], ((Player) sender ).getLocation())) {
            return true;
        }
        sender.sendMessage(ChatColor.RED + "[Toffy Dungeons]: File name " + args[0] + " not recognised!");
        return true;
    }

}
