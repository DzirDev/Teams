package me.goodwilled.legendstrifes;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private static Teams teams;
    public static Teams getlegends(){
        return teams;
    }
    private int currentlegend = 0;

    @Override
    public void onEnable(){
        teams = new Teams(this);
        teams.setup();
        getServer().getConsoleSender().sendMessage("legends v1.0 has been enabled.");
        createConfig();
        getConfig().options().copyDefaults(true);
        Commands cmd = new Commands(this);
        Events events = new Events(this);
        getCommand("legends").setExecutor(cmd);
        getServer().getPluginManager().registerEvents(events, this);
        events.setScores();

        for(Player online: Bukkit.getOnlinePlayers()){
            online.setScoreboard(events.sb);
        }
    }

    public LuckPerms getAPI(){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
        }
        return provider.getProvider();
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage("legendstrifes v1.0 has been disabled.");
    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
                getConfig().set("prefix", "&8[&4legends&8] ");
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}

