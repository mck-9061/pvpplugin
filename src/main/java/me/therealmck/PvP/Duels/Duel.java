package me.therealmck.PvP.Duels;

import me.therealmck.PvP.Items.SpecialItem;
import me.therealmck.PvP.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Duel {
    // Represents any Duel. Contains methods for duel management.
    // Should not be used on its own normally. Use a class that extends this.

    public List<Class> specialItems;
    public List<ItemStack> kit;

    public Player player1;
    public Player player2;

    public Location p1start;
    public Location p2start;

    public World world;

    public Duel() {
        specialItems = new ArrayList<>();
        kit = new ArrayList<>();
    }

    public void addSpecialItemsToKit() throws IllegalAccessException, InstantiationException {
        for (Class c : specialItems) {
            kit.add(((SpecialItem) c.newInstance()).item);
        }
    }

    public void beginDuel() {

        Main.currentlyInDuel.add(new AbstractMap.SimpleEntry<>(player1, player2));

        // tp players
        player1.teleport(p1start);
        player2.teleport(p2start);

        // allow pvp after countdown
        world.setPVP(false);
        AtomicInteger count = new AtomicInteger(6);

        new BukkitRunnable() {
            @Override
            public void run() {
                count.getAndDecrement();

                if (count.get() == 0) {
                    player1.sendTitle("Go!", "");
                    player2.sendTitle("Go!", "");

                    world.setPVP(true);

                    cancel();
                }


                player1.sendTitle(String.valueOf(count.get()), "");
                player2.sendTitle(String.valueOf(count.get()), "");
            }
        }.runTaskTimer(Main.instance, 20L, 20L);
    }
}
