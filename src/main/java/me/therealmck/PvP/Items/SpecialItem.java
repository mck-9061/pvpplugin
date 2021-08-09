package me.therealmck.PvP.Items;

import me.therealmck.PvP.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class SpecialItem {
    public ItemStack item;
    public int maxDurability;
    public HashMap<Entity, Map.Entry<Integer, ItemStack>> cooldowns = new HashMap<>();

    public SpecialItem() {

    }

    public void startCooldownChecking() {
        Bukkit.getScheduler().runTaskTimer(Main.instance, () -> {
            List<Entity> remove = new ArrayList<>();
            HashMap<Entity, Map.Entry<Integer, ItemStack>> add = new HashMap<>();

            for (Entity player : cooldowns.keySet()) {
                Player p = (Player) player;
                if (cooldowns.get(player).getKey() == 0) {
                    ItemStack bow = cooldowns.get(player).getValue();

                    Damageable meta = (Damageable) bow.getItemMeta();
                    meta.setDamage(0);
                    bow.setItemMeta((ItemMeta) meta);

                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Item recharged!"));

                    remove.add(player);
                }
                else {
                    int i = cooldowns.get(player).getKey() - 1;
                    ItemStack bow = cooldowns.get(player).getValue();
                    remove.add(player);
                    add.put(player, new AbstractMap.SimpleEntry<>(i, bow));

                    Damageable meta = (Damageable) bow.getItemMeta();

                    // todo: change 20 here to a variable if different weapons need different cooldowns
                    meta.setDamage((maxDurability / 20)*i);
                    bow.setItemMeta((ItemMeta) meta);
                }
            }


            for (Entity e : remove) {
                cooldowns.remove(e);
            }

            for (Entity e : add.keySet()) {
                cooldowns.put(e, add.get(e));
            }

        }, 20L, 20L);
    }
}
