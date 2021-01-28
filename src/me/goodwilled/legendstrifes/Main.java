package me.goodwilled.legendstrifes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private static Legends legends;
    public static Legends getLegends(){
        return legends;
    }
    private int currentTeam = 0;

    @Override
    public void onEnable(){
        legends = new Legends(this);
        legends.setup();
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            String teamName = getLegends().legendscfg.getString(p.getUniqueId().toString());
            if(teamName != null) {
                if (teamName.equals("UNDEAD")) {
                    getServer().getScheduler().runTaskLaterAsynchronously(this, () -> p.setPlayerListName(ChatColor.BLUE + "Undead: " + ChatColor.WHITE + p.getName()), 1L);
                }

                if (teamName.equals("NINJA")) {
                    getServer().getScheduler().runTaskLaterAsynchronously(this, () -> p.setPlayerListName(ChatColor.DARK_GREEN + "Ninja: " + ChatColor.WHITE + p.getName()), 1L);
                }

                if (teamName.equals("WIZARD")) {
                    getServer().getScheduler().runTaskLaterAsynchronously(this, () -> p.setPlayerListName(ChatColor.DARK_RED + "Wizard: " + ChatColor.WHITE + p.getName()), 1L);
                }

                if (teamName.equals("VIKING")) {
                    getServer().getScheduler().runTaskLaterAsynchronously(this, () -> p.setPlayerListName(ChatColor.GOLD + "Viking: " + ChatColor.WHITE + p.getName()), 1L);
                }
            }
        }
        getServer().getConsoleSender().sendMessage("legends v1.0 has been enabled.");
        createConfig();
        getConfig().options().copyDefaults(true);
        Commands cmd = new Commands(this);
        Events events = new Events(this);
        NightBurn nightBurn = new NightBurn(this);
        getCommand("legends").setExecutor(cmd);
        getServer().getPluginManager().registerEvents(events, this);
        getServer().getPluginManager().registerEvents(events, this);
        events.setScores();
        nightBurn.nightBurn();

        for(Player online: Bukkit.getOnlinePlayers()){
            online.setScoreboard(events.sb);
        }
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage("LegendStrifes v1.0 has been disabled.");
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
                getConfig().set("prefix", "&8[&4Legends&8] ");
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}

