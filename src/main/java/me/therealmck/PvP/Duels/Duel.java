package me.therealmck.PvP.Duels;

import me.therealmck.PvP.Items.SpecialItem;
import me.therealmck.PvP.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void beginDuel() throws InstantiationException, IllegalAccessException {

        Main.currentlyInDuel.add(new AbstractMap.SimpleEntry<>(player1, player2));

        // tp players
        player1.teleport(p1start);
        player2.teleport(p2start);

        // allow pvp after countdown
        world.setPVP(false);

        // give both players the kit
        givePlayerKit(player1);
        givePlayerKit(player2);


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

    public void givePlayerKit(Player p) throws InstantiationException, IllegalAccessException {
        PlayerInventory inv = p.getInventory();

        List<Material> boots = new ArrayList<>(Arrays.asList(Material.LEATHER_BOOTS, Material.IRON_BOOTS, Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS));
        List<Material> leggings = new ArrayList<>(Arrays.asList(Material.LEATHER_LEGGINGS, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS));
        List<Material> chestplates = new ArrayList<>(Arrays.asList(Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE, Material.ELYTRA));
        List<Material> helmets = new ArrayList<>(Arrays.asList(Material.LEATHER_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET, Material.TURTLE_HELMET));

        for (ItemStack item : kit) {
            Material m = item.getType();

            if (m.equals(Material.SHIELD)) inv.setItemInOffHand(item.clone());
            else if (boots.contains(m)) inv.setBoots(item.clone());
            else if (leggings.contains(m)) inv.setLeggings(item.clone());
            else if (chestplates.contains(m)) inv.setChestplate(item.clone());
            else if (helmets.contains(m)) inv.setHelmet(item.clone());

            else inv.addItem(item.clone());

        }
    }
}
