package me.therealmck.PvP;

import me.therealmck.PvP.Duels.Listeners.KillListener;
import me.therealmck.PvP.Items.ControlledBow;
import me.therealmck.PvP.Items.Dash;
import me.therealmck.PvP.Items.Thunderblade;
import me.therealmck.PvP.Listeners.JoinListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {
    public static Plugin instance;
    public static List<Map.Entry<Player, Player>> currentlyInDuel;
    public static Location spawn;

    @Override
    public void onEnable() {
        instance = this;
        currentlyInDuel = new ArrayList<>();
        //todo: set spawn location

        List<Class> listeners = new ArrayList<>(Arrays.asList(ControlledBow.class, Thunderblade.class, Dash.class, KillListener.class, JoinListener.class));

        for (Class c : listeners) {
            try {
                getServer().getPluginManager().registerEvents((Listener) (c.newInstance()), this);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }



    }
}
