package me.goodwilled.legendstrifes;

import com.google.common.base.Enums;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;

import static org.bukkit.scoreboard.DisplaySlot.*;

public class Events implements Listener {
    private Main main;
    Commands cmd;
    PlayerInConfig pcfg;
    public Events(Main main){ this.main = main; }
    private Legend[] legends = {Legend.UNDEAD, Legend.WIZARD, Legend.VIKING, Legend.NINJA};
    private int currentTeam = 0;
    String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Legends" + ChatColor.DARK_GRAY + "] ";
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard sb = manager.getNewScoreboard();
    org.bukkit.scoreboard.Team undead = sb.registerNewTeam("Undead");
    org.bukkit.scoreboard.Team ninja = sb.registerNewTeam("Ninja");
    org.bukkit.scoreboard.Team viking = sb.registerNewTeam("Viking");
    org.bukkit.scoreboard.Team wizard = sb.registerNewTeam("Wizard");
    Objective obj = sb.registerNewObjective("test2", "dummy");

    int apoints = 0;
    int npoints = 0;
    int wpoints = 0;
    int vpoints = 0;


    public Objective getObj(){
        obj.setDisplaySlot(SIDEBAR);
        obj.setDisplayName(ChatColor.DARK_GRAY + "<" + ChatColor.DARK_RED + ChatColor.BOLD + "Legend Scores" + ChatColor.DARK_GRAY + ">");
        return obj;
    }

    public void setScores(){
        if(main.getLegends().legendscfg.get("legends." + Legend.valueOf("UNDEAD").toString().toUpperCase() + ".points") == null){
            main.getLegends().legendscfg.set("legends." + Legend.valueOf("UNDEAD").toString().toUpperCase() + ".points", 0);
        }
        if(main.getLegends().legendscfg.get("legends." + Legend.NINJA + ".points") == null){
            main.getLegends().legendscfg.set("legends." + Legend.NINJA + ".points", 0);
        }
        if(main.getLegends().legendscfg.get("legends." + Legend.valueOf("WIZARD").toString().toUpperCase() + ".points") == null){
            main.getLegends().legendscfg.set("legends." + Legend.valueOf("WIZARD").toString().toUpperCase() + ".points", 0);
        }
        if(main.getLegends().legendscfg.get("legends." + Legend.valueOf("VIKING").toString().toUpperCase() + ".points") == null){
            main.getLegends().legendscfg.set("legends." + Legend.valueOf("VIKING").toString().toUpperCase() + ".points", 0);
        }
        Score aScore = getObj().getScore(ChatColor.BLUE + "Undead:");
        Score nScore = getObj().getScore(ChatColor.DARK_GREEN + "Ninjas:");
        Score wScore = getObj().getScore(ChatColor.DARK_RED + "Wizards:");
        Score vScore = getObj().getScore(ChatColor.GOLD + "Vikings:");
        aScore.setScore((Integer) main.getLegends().legendscfg.get("legends." + Legend.valueOf("UNDEAD").toString().toUpperCase() + ".points"));
        nScore.setScore((Integer) main.getLegends().legendscfg.get("legends." + Legend.NINJA + ".points"));
        wScore.setScore((Integer) main.getLegends().legendscfg.get("legends." + Legend.valueOf("WIZARD").toString().toUpperCase() + ".points"));
        vScore.setScore((Integer) main.getLegends().legendscfg.get("legends." + Legend.valueOf("VIKING").toString().toUpperCase() + ".points"));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String teamName = main.getLegends().legendscfg.getString(p.getUniqueId().toString());
        getObj();
        setScores();
        p.setScoreboard(sb);
        if(main.getLegends().legendscfg.contains(p.getUniqueId().toString())) {
            if(teamName.equals("UNDEAD")) {
                main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.BLUE + "Undead: " + ChatColor.WHITE + p.getName()), 1L);
            }

            if(teamName.equals("NINJA")) {
                main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.DARK_GREEN + "Ninja: " + ChatColor.WHITE + p.getName()), 1L);
                p.setWalkSpeed(0.3f);
                p.setFlySpeed(0.3f);
            }

            if(teamName.equals("WIZARD")) {
                main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.DARK_RED + "Wizard: " + ChatColor.WHITE + p.getName()), 1L);
            }

            if(teamName.equals("VIKING")){
                main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.GOLD + "Viking: " + ChatColor.WHITE + p.getName()), 1L);
            }
            for(Player online: Bukkit.getOnlinePlayers()){
                String oTeamName = (String) main.getLegends().legendscfg.get(online.getUniqueId().toString());
                if(!oTeamName.equals(teamName)){
                    online.sendTitle(ChatColor.DARK_RED + "An enemy emerges...", ChatColor.GOLD + "Defend what's yours.", 25, 60, 25);
                }
            }
        } else if(!main.getLegends().legendscfg.contains(p.getUniqueId().toString()) && !p.getOpenInventory().getTitle().equals("legends")) {
            p.openInventory(cmd.legendsGUI());
        }


    } // onJoin

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        Entity damager = e.getDamager();
        Entity damaged = e.getEntity();
        if(damager instanceof Player && damaged instanceof Player){
            Player pdamager = (Player) damager;
            Player pdamaged = (Player) damaged;
            String drteamName = main.getLegends().legendscfg.getString(pdamager.getUniqueId().toString());
            String ddteamName = main.getLegends().legendscfg.getString(pdamaged.getUniqueId().toString());
            if(drteamName.equalsIgnoreCase(ddteamName)){
                e.setCancelled(true);
                pdamager.sendTitle(ChatColor.DARK_RED + "No!", ChatColor.WHITE + "Do not hit teammates.", 15, 15, 15);
            } else {
                if(drteamName.equalsIgnoreCase("UNDEAD")){
                    if(!pdamaged.hasPotionEffect(PotionEffectType.WITHER)) {
                        pdamaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 150, 1));
                    }

                }
                else if(drteamName.equalsIgnoreCase("NINJA")){
                    if(!pdamager.hasPotionEffect(PotionEffectType.SPEED)) {
                        pdamager.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 2, false, false));
                    }
                }
                else if(drteamName.equalsIgnoreCase("VIKING")){
                   pdamaged.setHealth(pdamaged.getHealth()-1.5);
                }
                else if(drteamName.equalsIgnoreCase("WIZARD")){
                    double dur = 0.5;
                    if(!pdamaged.hasPotionEffect(PotionEffectType.LEVITATION)){
                        pdamaged.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 1));
                        pdamaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 1));
                    }

               }
            }
        }
        if(damager instanceof Player){
            Player p = (Player) e.getDamager();
            String drt = main.getLegends().legendscfg.getString(p.getUniqueId().toString());
            if(drt.equalsIgnoreCase("VIKING")){
                e.setDamage(3);
            }
        }
        if(damager instanceof Zombie || damager instanceof Skeleton || damager instanceof Phantom || damager instanceof Wither || damager instanceof WitherSkeleton){
            if(damaged instanceof Player){
                Player p = (Player) e.getEntity();
                if(main.getLegends().legendscfg.getString(p.getUniqueId().toString()).equalsIgnoreCase("UNDEAD")){
                    e.setCancelled(true);
                }
            }
        }
        if(damager instanceof Player && !(damaged instanceof Player)){
            if(main.getLegends().legendscfg.getString(damager.getUniqueId().toString()).equalsIgnoreCase("UNDEAD")) {
                ((LivingEntity) damaged).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false));
            }
        }
        if(damager instanceof Witch){
            if(damaged instanceof Player){
                Player p = (Player) e.getEntity();
                if(main.getLegends().legendscfg.getString(p.getUniqueId().toString()).equalsIgnoreCase("WIZARD")){
                    e.setCancelled(true);
                }
            }
        }

    } // onEntityDamageByEntityEvent

  /**
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e){
        Player killer = e.getEntity().getKiller();
        Player killed = e.getEntity();
        if(killer instanceof  Player && killed instanceof Player){
            String krteamName = main.getlegends().legendscfg.getString(killer.getUniqueId().toString());
            String kdteamName = main.getlegends().legendscfg.getString(killed.getUniqueId().toString());
            if(krteamName.equalsIgnoreCase("UNDEAD")){
                apoints+=1;
                main.getlegends().legendscfg.set("legends."+ Team.valueOf("UNDEAD") + ".points", apoints);
                main.getlegends().savePlayers();
                setScores();
            }
            if(krteamName.equalsIgnoreCase("NINJA")){
                npoints+=1;
                main.getlegends().legendscfg.set("legends."+ Team.valueOf("NINJA") + ".points", npoints);
                main.getlegends().savePlayers();
                setScores();
            }
            if(krteamName.equalsIgnoreCase("VIKING")){
                vpoints+=1;
                main.getlegends().legendscfg.set("legends."+ Team.valueOf("VIKING").toString().toUpperCase() + ".points", vpoints);
                main.getlegends().savePlayers();
                setScores();
            }
            if(krteamName.equalsIgnoreCase("WIZARD")){
                wpoints+=1;
                main.getlegends().legendscfg.set("legends."+ Team.valueOf("WIZARD").toString().toUpperCase() + ".points", wpoints);
                main.getlegends().savePlayers();
                setScores();
            }
        }
    }
   **/

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals("legends")) {
            e.setCancelled(true);
            switch (e.getSlot()) {
                   case 10:
                       p.sendMessage(prefix + ChatColor.AQUA + "Set your team to " + ChatColor.BLUE + "UNDEAD.");
                       main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.BLUE + "Undead: " + ChatColor.WHITE + p.getName()), 1L);
                       main.getLegends().legendscfg.set(p.getUniqueId().toString(), Legend.UNDEAD);
                       main.getLegends().savePlayers();
                       for(Player online: Bukkit.getOnlinePlayers()){
                           String oTeamName = (String) main.getLegends().legendscfg.get(online.getUniqueId().toString());
                           String teamName = (String) main.getLegends().legendscfg.get(p.getUniqueId().toString());
                           if(!oTeamName.equals(teamName)){
                               online.sendTitle(ChatColor.DARK_RED + "An enemy emerges...", ChatColor.GOLD + "Defend what's yours.", 25, 60, 25);
                           }
                       }
                       p.closeInventory();
                    break;
                    case 12:
                    p.sendMessage(prefix + ChatColor.AQUA + "Set your team to " + ChatColor.DARK_GREEN + "NINJA.");
                    main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.DARK_GREEN + "Ninja: " + ChatColor.WHITE + p.getName()), 1L);
                    main.getLegends().legendscfg.set(p.getUniqueId().toString(), Legend.NINJA);
                    main.getLegends().savePlayers();
                    p.setWalkSpeed(0.3f);
                    p.setFlySpeed(0.3f);
                        for(Player online: Bukkit.getOnlinePlayers()){
                            String oTeamName = (String) main.getLegends().legendscfg.get(online.getUniqueId().toString());
                            String teamName = (String) main.getLegends().legendscfg.get(p.getUniqueId().toString());
                            if(!oTeamName.equals(teamName)){
                                online.sendTitle(ChatColor.DARK_RED + "An enemy emerges...", ChatColor.GOLD + "Defend what's yours.", 25, 60, 25);
                            }
                        }
                    p.closeInventory();
                    break;
                    case 14:
                    p.sendMessage(prefix + ChatColor.AQUA + "Set your team to " + ChatColor.DARK_RED + "WIZARD.");
                    main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.DARK_RED + "Wizard: " + ChatColor.WHITE + p.getName()), 1L);
                    main.getLegends().legendscfg.set(p.getUniqueId().toString(), Legend.WIZARD);
                    main.getLegends().savePlayers();
                        for(Player online: Bukkit.getOnlinePlayers()){
                            String oTeamName = (String) main.getLegends().legendscfg.get(online.getUniqueId().toString());

                            String teamName = (String) main.getLegends().legendscfg.get(p.getUniqueId().toString());
                            if(!oTeamName.equals(teamName)){
                                online.sendTitle(ChatColor.DARK_RED + "An enemy emerges...", ChatColor.GOLD + "Defend what's yours.", 25, 60, 25);
                            }
                        }
                    p.closeInventory();
                    break;
                    case 16:
                    p.sendMessage(prefix + ChatColor.AQUA + "Set your team to " + ChatColor.GOLD + "VIKING.");
                    main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.GOLD + "Viking: " + ChatColor.WHITE + p.getName()), 1L);
                    main.getLegends().legendscfg.set(p.getUniqueId().toString(), Legend.VIKING);
                    main.getLegends().savePlayers();
                        for(Player online: Bukkit.getOnlinePlayers()){
                            String oTeamName = (String) main.getLegends().legendscfg.get(online.getUniqueId().toString());

                            String teamName = (String) main.getLegends().legendscfg.get(p.getUniqueId().toString());
                            if(!oTeamName.equals(teamName)) {
                                online.sendTitle(ChatColor.DARK_RED + "An enemy emerges...", ChatColor.GOLD + "Defend what's yours.", 25, 60, 25);
                            }
                        }
                    p.closeInventory();
                    break;

            }
        }
    }

    @EventHandler
    public void invClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();
        Inventory inv = cmd.legendsGUI();
       if(e.getView().getTitle().equals("legends")){
           if(!main.getLegends().legendscfg.contains(p.getUniqueId().toString())){
               main.getServer().getScheduler().runTaskLater(main, new Runnable() {
                   @Override
                   public void run() {
                        if(!p.getOpenInventory().getTitle().equals("legends")){
                            p.openInventory(inv);
                        }
                   }
               }, 5L);
           } else {
               return;
           }
       }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
       EntityDamageEvent.DamageCause d = e.getCause();
       if(e.getEntity() instanceof Player && d.equals(EntityDamageEvent.DamageCause.FIRE)){
           Player p = (Player) e.getEntity();
       }
    }

    @EventHandler
    public void entityInteract(PlayerInteractEntityEvent e){
       Player p = e.getPlayer();
       Entity ent = e.getRightClicked();
       if(main.getLegends().legendscfg.getString(p.getUniqueId().toString()).equalsIgnoreCase("UNDEAD")){
           if(ent instanceof Zombie || ent instanceof Skeleton || ent instanceof Phantom){
               ent.setPassenger(p);
           }
       }
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e) {
        Entity mob = e.getEntity();
        LivingEntity le = e.getTarget();
        if (le instanceof Player) {
            Player p = (Player) le;
            if (mob instanceof Skeleton || mob instanceof Phantom || mob instanceof Zombie || mob instanceof WitherSkeleton || mob instanceof Wither) {
                if (main.getLegends().legendscfg.getString(p.getUniqueId().toString()).equalsIgnoreCase("UNDEAD")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.FIREBALL) {
            event.setCancelled(true);
        }
    }

  @EventHandler
    public void rightClick(PlayerInteractEvent e){
       Player p = e.getPlayer();
       Action a = e.getAction();
       if(a == Action.RIGHT_CLICK_AIR){
           if(main.getLegends().legendscfg.getString(p.getUniqueId().toString()).equalsIgnoreCase("WIZARD")) {
               p.launchProjectile(Fireball.class);
           }
       }

  }

}
