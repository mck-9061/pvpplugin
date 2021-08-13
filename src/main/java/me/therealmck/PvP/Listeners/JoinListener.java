package me.therealmck.PvP.Listeners;

import me.therealmck.PvP.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    // If a player disconnects in spectator, they will remain in spectator.
    // This forces all joining players to survival and also teleports them to spawn.

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        p.setGameMode(GameMode.SURVIVAL);
        p.teleport(Main.spawn);

        //todo: also add them to a config file
        // for storing player data like wins/losses and stuff like that
    }
}
