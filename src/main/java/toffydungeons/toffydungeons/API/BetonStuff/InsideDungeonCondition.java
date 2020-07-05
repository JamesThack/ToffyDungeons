package toffydungeons.toffydungeons.API.BetonStuff;

import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.Condition;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.utils.PlayerConverter;
import toffydungeons.toffydungeons.CurrentEvents.ConstantEvents;

public class InsideDungeonCondition extends Condition {

    public InsideDungeonCondition(Instruction instruction) {
        super(instruction);
    }

    @Override
    protected Boolean execute(String s) throws QuestRuntimeException {
        Player player = PlayerConverter.getPlayer(s);
        ConstantEvents events = new ConstantEvents();
        return events.playerInDungeon(player);
    }

}
