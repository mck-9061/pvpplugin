package me.therealmck.PvP.Duels.Listeners;

import me.therealmck.PvP.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillListener implements Listener {
    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player killer = null;
        Player killed = (Player) event.getEntity();
        Map.Entry<Player, Player> entry = null;

        for (Map.Entry<Player, Player> duel : Main.currentlyInDuel) {
            if (duel.getKey().equals(killed)) {
                killer = duel.getValue();
                entry = duel;
                break;
            }

            if (duel.getValue().equals(killed)) {
                killer = duel.getKey();
                entry = duel;
                break;
            }
        }

        if (killer == null) return;

        killed.setHealth(20);
        killed.teleport(killer);


        killed.setGameMode(GameMode.SPECTATOR);

        Main.currentlyInDuel.remove(entry);

        //todo: stats
        killer.sendTitle("You win!", "");
        killed.sendTitle("You lose!", "");

        Player finalKiller = killer;
        Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
            finalKiller.teleport(Main.spawn);
            killed.teleport(Main.spawn);

            killed.setGameMode(GameMode.SURVIVAL);
            finalKiller.setHealth(20);
        }, 100L);
    }
}
