package toffydungeons.toffydungeons.GUIs.ProceduralGeneration;

import org.bukkit.Material;
import toffydungeons.toffydungeons.GUIs.extendable.AbstractVanityMenu;

public class ProceduralMainMenu extends AbstractVanityMenu {

    public ProceduralMainMenu() {
        super("Procedural Generation", 54);
    }

    @Override
    public void initaliseItems() {
        this.getInventory().setItem(10, createGuiItem(Material.EMERALD, "§aNew Procedural Dungeon Here"));
    }
}
