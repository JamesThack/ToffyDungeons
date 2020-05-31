package toffydungeons.toffydungeons.API;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryAPI {

    public static void givePlayerItem(Player player, ItemStack itemStack) {
        for (int x = 0; x < player.getInventory().getSize() - 5; x++) {
            if (player.getInventory().getItem(x) == null) {
                player.getInventory().setItem(x, itemStack);
                return;
            }
        }
        Item item =  player.getWorld().dropItemNaturally(player.getLocation(), player.getInventory().getItem(0));
        item.setPickupDelay(5 * 20);
        player.getInventory().setItem(0, itemStack);
    }
}
