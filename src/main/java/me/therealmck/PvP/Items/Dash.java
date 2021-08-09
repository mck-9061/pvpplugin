package me.therealmck.PvP.Items;

import me.therealmck.PvP.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dash extends SpecialItem implements Listener {
    public HashMap<Player, Boolean> canUse = new HashMap<>();

    public Dash() {
        super();

        ItemStack it = new ItemStack(Material.BOOK);
        ItemMeta m = it.getItemMeta();
        m.setDisplayName("§6Dash");
        it.setItemMeta(m);

        item = it;

        //todo: loop players to check if they can be recharged
        Bukkit.getScheduler().runTaskTimer(Main.instance, () -> {
            List<Player> setToTrue = new ArrayList<>();

            for (Player player : canUse.keySet()) {
                if (!canUse.get(player)) {
                    Location below = player.getLocation().clone();
                    below.setY(below.getBlockY()-1);

                    if (!below.getBlock().getType().name().contains("AIR")) {
                        setToTrue.add(player);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Dash recharged!"));
                    }
                }
            }

            for (Player p : setToTrue) canUse.replace(p, true);
        }, 0L, 1L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        canUse.put(event.getPlayer(), false);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        canUse.remove(event.getPlayer());
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        ItemStack used = event.getItem();

        Player player = event.getPlayer();

        if (used == null) return;

        if (used.getItemMeta().getDisplayName().equals("§6Dash")) {
            if (canUse.get(player)) {
                canUse.replace(player, false);

                if (!player.hasPermission("pvp.infinite")) {
                    used.setAmount(used.getAmount() - 1);
                }
                
                player.setVelocity(player.getLocation().getDirection().normalize().multiply(4));
                player.getWorld().playSound(player.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_1, 1f, 1f);

                Bukkit.getScheduler().runTaskLater(Main.instance, () -> player.setVelocity(player.getVelocity().normalize()), 10L);
            } else {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Touch ground to recharge"));
            }
        }
    }
}
