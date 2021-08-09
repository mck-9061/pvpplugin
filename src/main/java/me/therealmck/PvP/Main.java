package me.therealmck.PvP;

import me.therealmck.PvP.Items.ControlledBow;
import me.therealmck.PvP.Items.Dash;
import me.therealmck.PvP.Items.Thunderblade;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin {
    public static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new ControlledBow(), this);
        getServer().getPluginManager().registerEvents(new Thunderblade(), this);
        getServer().getPluginManager().registerEvents(new Dash(), this);
    }
}
