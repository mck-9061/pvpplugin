package me.therealmck.PvP.Items;

import me.therealmck.PvP.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Thunderblade extends SpecialItem implements Listener {
    public HashMap<Player, ArmorStand> currentPlayers = new HashMap<>();

    public Thunderblade() {
        super();
        maxDurability = 1562;

        ItemStack it = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta m = it.getItemMeta();
        m.setDisplayName("§6Thunderblade");
        it.setItemMeta(m);

        item = it;

        startCooldownChecking();
        Bukkit.getScheduler().runTaskTimer(Main.instance, () -> {
            List<Player> removeThesePlayers = new ArrayList<>();

            for (Player player : currentPlayers.keySet()) {
                ArmorStand armorStand = currentPlayers.get(player);

                //armorStand.teleport(player.getLocation());

                EulerAngle angle = armorStand.getRightArmPose();
                //System.out.println(angle.getX());

                if (angle.getX() < 3.8) {
                    angle = angle.setX(angle.getX()+0.2);

                    armorStand.setRightArmPose(angle);
                } else if (angle.getX() >= 3.8 && angle.getX() <= 4) {
                    System.out.println("lightning");
                    player.setGravity(false);
                    player.setVelocity(new Vector(0, 0, 0));
                    Location location = player.getLocation().clone();
                    location.setY(location.getBlockY() + 1);
                    player.getWorld().strikeLightningEffect(location);

                    angle = angle.setX(4.1);
                    armorStand.setRightArmPose(angle);

                    Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
                        player.removePassenger(armorStand);

                        armorStand.teleport(player.getLocation());

                        armorStand.setGravity(true);
                        armorStand.setVelocity(new Vector(0, -4, 0));

                        armorStand.setRightArmPose(armorStand.getRightArmPose().setX(4.2));
                    }, 20L);

                    Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
                        player.setGravity(true);
                        player.setVelocity(new Vector(0, -4, 0));
                    }, 23L);


                } else if (angle.getX() < 6 && angle.getX() > 4.15) {
                    angle = angle.setX(angle.getX()+0.2);

                    armorStand.setRightArmPose(angle);
                }




                // Check if player has touched the ground
                Location blockBelow = player.getLocation().clone();
                blockBelow.setY(blockBelow.getBlockY() - 1);

                try {
                    if (!blockBelow.getWorld().getBlockAt(blockBelow).getType().name().contains("AIR") &&
                    angle.getX() > 2) {
                        // Block below isn't air, and as such the player has touched the ground.
                        armorStand.remove();
                        removeThesePlayers.add(player);
                        player.getWorld().strikeLightningEffect(player.getLocation());


                        List<Block> lastIter = new ArrayList<>();

                        lastIter.add(player.getWorld().getBlockAt(blockBelow));
                        World w = player.getWorld();

                        for (int i = 0; i < 6; i++) {
                            Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
                                List<Block> blocksToPush = new ArrayList<>();
                                for (Block b : lastIter) {
                                    Location l = b.getLocation();
                                    blocksToPush.add(w.getBlockAt(l.getBlockX()+1, l.getBlockY(), l.getBlockZ()));
                                    blocksToPush.add(w.getBlockAt(l.getBlockX()-1, l.getBlockY(), l.getBlockZ()));
                                    blocksToPush.add(w.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ()+1));
                                    blocksToPush.add(w.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ()-1));
                                }
                                blocksToPush.remove(w.getBlockAt(blockBelow));
                                lastIter.clear();
                                lastIter.addAll(blocksToPush);
                                for (Block b : blocksToPush) {
                                    BlockData data = b.getBlockData();

                                    FallingBlock block = w.spawnFallingBlock(b.getLocation(), data);
                                    b.setType(Material.AIR);
                                    block.setVelocity(new Vector(0, 0.2, 0));


                                    List<Entity> nearby = block.getNearbyEntities(1.5, 1.5, 1.5);
                                    for (Entity e : nearby) {
                                        if (!(e instanceof LivingEntity) || e == player) continue;
                                        ((LivingEntity) e).damage(10);
                                        e.setVelocity(new Vector(0, 0.5, 0));

                                        System.out.println("Damaged " + e.getName());


                                    }
                                }
                            }, 2L*i);
                        }

                        player.setInvulnerable(false);

                    }
                } catch (Exception ignored) {}
            }

            for (Player p : removeThesePlayers) currentPlayers.remove(p);
        }, 1L, 1L);
    }

    // Right clicking this weapon launches you up.
    // At the peak of the jump, the sword is raised and lightning strikes it.
    // The player then swings the sword down while being forced down.
    // The player then strikes the ground, causing lots of damage with a direct hit.
    // Also AOE damage with blocks around the strike raising up, causing damage.
    // Lightning also strikes where the blade is struck.

    @EventHandler
    public void onSwordRightClick(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (currentPlayers.containsKey(event.getPlayer())) return;

        if (cooldowns.containsKey(event.getPlayer())) {
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Item on cooldown!"));
            return;
        }

        try {
            if (item.getItemMeta().getDisplayName().equals("§6Thunderblade")) {
                Player player = event.getPlayer();
                player.setInvulnerable(true);
                ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                armorStand.setBasePlate(false);
                armorStand.setInvisible(true);
                armorStand.setGravity(false);
                armorStand.setInvulnerable(true);

                armorStand.setRightArmPose(armorStand.getRightArmPose().setX(0));

                player.addPassenger(armorStand);

                EntityEquipment inv = armorStand.getEquipment();
                inv.setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));

                player.setVelocity(new Vector(0, 4, 0));

                currentPlayers.put(player, armorStand);
                if (player.hasPermission("pvp.bypass")) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Cooldown bypassed!"));
                } else {
                    cooldowns.put(player, new AbstractMap.SimpleEntry<>(20, item));
                }

            }
        } catch (Exception ignored) {}
    }
}
