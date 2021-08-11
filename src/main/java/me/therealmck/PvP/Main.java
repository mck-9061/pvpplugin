package me.therealmck.PvP;

import me.therealmck.PvP.Duels.Listeners.KillListener;
import me.therealmck.PvP.Items.ControlledBow;
import me.therealmck.PvP.Items.Dash;
import me.therealmck.PvP.Items.Thunderblade;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {
    public static Plugin instance;
    public static List<Map.Entry<Player, Player>> currentlyInDuel;
    public static Location spawn;

    @Override
    public void onEnable() {
        instance = this;
        currentlyInDuel = new ArrayList<>();
        //todo: set spawn location

        getServer().getPluginManager().registerEvents(new ControlledBow(), this);
        getServer().getPluginManager().registerEvents(new Thunderblade(), this);
        getServer().getPluginManager().registerEvents(new Dash(), this);
        getServer().getPluginManager().registerEvents(new KillListener(), this);
    }
}
