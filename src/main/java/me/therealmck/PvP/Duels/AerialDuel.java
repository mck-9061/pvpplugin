package me.therealmck.PvP.Duels;

import me.therealmck.PvP.Items.ControlledBow;
import me.therealmck.PvP.Items.SpecialItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AerialDuel extends Duel {
    public AerialDuel() throws IllegalAccessException, InstantiationException {
        super();
        specialItems.add(ControlledBow.class);
        addSpecialItemsToKit();
        
        kit.add(new ItemStack(Material.ELYTRA));
    }
}
