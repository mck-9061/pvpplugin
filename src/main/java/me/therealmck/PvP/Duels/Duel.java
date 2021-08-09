package me.therealmck.PvP.Duels;

import me.therealmck.PvP.Items.SpecialItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Duel {
    // Represents any Duel. Contains methods for duel management.
    // Should not be used on its own normally. Use a class that extends this.

    public List<Class> specialItems;
    public List<ItemStack> kit;

    public Player player1;
    public Player player2;

    public Duel() {
        specialItems = new ArrayList<>();
        kit = new ArrayList<>();
    }

    public void addSpecialItemsToKit() throws IllegalAccessException, InstantiationException {
        for (Class c : specialItems) {
            kit.add(((SpecialItem) c.newInstance()).item);
        }
    }
}
