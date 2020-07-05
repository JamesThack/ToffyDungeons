package toffydungeons.toffydungeons.API.BetonStuff;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.QuestEvent;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.utils.LocationData;
import pl.betoncraft.betonquest.utils.PlayerConverter;
import toffydungeons.toffydungeons.API.DungeonRoomLayout;
import toffydungeons.toffydungeons.API.FileSaving;

import java.io.File;

public class DungeonEvent extends QuestEvent {

    private DungeonRoomLayout dunName;
    private LocationData spawnLoc;
    private boolean enter;

    public DungeonEvent(Instruction instruction) throws InstructionParseException {
        super(instruction);
        if (!FileSaving.folderContainsFile("dungeons", instruction.getPart(1) + ".dungeon"))
            throw new InstructionParseException("Enter a valid dungeon name");
        dunName = new DungeonRoomLayout().deserialise(FileSaving.readLines("dungeons" + File.separator + instruction.getPart(1) + ".dungeon"));
        spawnLoc = instruction.getLocation(instruction.getPart(2));
        enter = instruction.hasArgument("-e");
    }

    @Override
    protected Void execute(String s) throws QuestRuntimeException {
        Player player = PlayerConverter.getPlayer(s);
        dunName.generateBuild(spawnLoc.getLocation(s));
        if (!enter) return null;
        Location loccer = spawnLoc.getLocation(s).add(0, 1, 0);
        player.teleport(loccer);
        return null;

        }
}

