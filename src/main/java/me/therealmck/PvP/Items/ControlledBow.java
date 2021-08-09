package me.therealmck.PvP.Items;

import me.therealmck.PvP.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ControlledBow extends SpecialItem implements Listener {
    public HashMap<Entity, Entity> currentlyRiding = new HashMap<>();

    // A bow that, when fired, keeps the user essentially holding onto the arrow, able to steer it's movement. The user
    // can let go at any time and the arrow will keep it's current velocity.

    // Known issues: If bow is dropped, this will all break.
    // Will be fixed when bow is implemented into a gamemode that prevents item dropping.

    public ControlledBow() {
        super();

        maxDurability = 384;
        ItemStack it = new ItemStack(Material.BOW);
        ItemMeta m = it.getItemMeta();
        m.setDisplayName("§6Controlled Bow");
        it.setItemMeta(m);

        item = it;

        Bukkit.getScheduler().runTaskTimer(Main.instance, () -> {
            for (Entity player : currentlyRiding.keySet()) {
                Entity projectile = currentlyRiding.get(player);

                projectile.setVelocity(player.getLocation().getDirection());
            }
        }, 2L, 2L);

        startCooldownChecking();
    }

    @EventHandler
    public void onArrowFired(EntityShootBowEvent event) {
        ItemStack bow = event.getBow();
        Entity projectile = event.getProjectile();
        Entity shotBow = event.getEntity();

        if (projectile instanceof Arrow && shotBow instanceof Player && bow.getItemMeta().getDisplayName().equals("§6Controlled Bow")) {

            if (cooldowns.containsKey(shotBow)) {
                Player player = (Player) shotBow;
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You're on cooldown!", ChatColor.RED));

                event.setCancelled(true);
            } else {
                projectile.addPassenger(shotBow);
                currentlyRiding.put(shotBow, projectile);
                Player player = (Player) shotBow;
                AtomicInteger secondsLeft = new AtomicInteger(5);

                new BukkitRunnable() {
                    public void run() {
                        if (!currentlyRiding.containsKey(shotBow)) cancel();

                        if (secondsLeft.get() == 0) {
                            projectile.removePassenger(shotBow);
                            currentlyRiding.remove(shotBow);
                            if (event.getEntity().hasPermission("pvp.bypass")) {
                                ((Player) event.getEntity()).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Cooldown bypassed!"));
                            } else {
                                cooldowns.put(event.getEntity(), new AbstractMap.SimpleEntry<>(20, bow));
                            }
                            cancel();

                        } else {
                            secondsLeft.getAndDecrement();
                            String message = "";
                            for (int i = 0; i < secondsLeft.get(); i++) message += "|";
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message, ChatColor.GOLD));
                        }
                    }
                }.runTaskTimer(Main.instance, 20L, 20L);
            }
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (!currentlyRiding.containsKey(event.getEntity())) return;
        currentlyRiding.remove(event.getEntity());

        ItemStack bow = null;
        PlayerInventory inv = ((Player) event.getEntity()).getInventory();

        for (ItemStack item : inv) {
            try {
                if (item.getItemMeta().getDisplayName().equals("§6Controlled Bow")) {
                    bow = item;
                    break;
                }
            } catch (Exception ignored) {}
        }

        if (event.getEntity().hasPermission("pvp.bypass")) {
            ((Player) event.getEntity()).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Cooldown bypassed!"));
        } else {
            cooldowns.put(event.getEntity(), new AbstractMap.SimpleEntry<>(20, bow));
        }
    }
}
