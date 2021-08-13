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
        kit.add(new ItemStack(Material.DIAMOND_HELMET));
        kit.add(new ItemStack(Material.DIAMOND_LEGGINGS));
        kit.add(new ItemStack(Material.DIAMOND_BOOTS));

        kit.add(new ItemStack(Material.DIAMOND_SWORD));
        kit.add(new ItemStack(Material.FIREWORK_ROCKET, 64));
        kit.add(new ItemStack(Material.BOW));
        kit.add(new ItemStack(Material.ARROW, 64));
        kit.add(new ItemStack(Material.SHIELD));

        //todo: set player starting locations and world
    }
}
