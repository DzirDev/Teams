package me.goodwilled.legendstrifes;

import net.luckperms.api.model.group.Group;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.codehaus.plexus.util.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import static org.bukkit.scoreboard.DisplaySlot.*;

public class Events implements Listener {
    private Main main;
    Commands cmd;
    public Events(Main main){ this.main = main; }
    private Team[] teams = {Team.KNIGHT, Team.TAMER, Team.ARCHER, Team.MAGE};
    private int currentlegend = 0;
    String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "legends" + ChatColor.DARK_GRAY + "] ";
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard sb = manager.getNewScoreboard();
    org.bukkit.scoreboard.Team KNIGHT = sb.registerNewTeam("KNIGHT");
    org.bukkit.scoreboard.Team MAGE = sb.registerNewTeam("MAGE");
    org.bukkit.scoreboard.Team ARCHER = sb.registerNewTeam("ARCHER");
    org.bukkit.scoreboard.Team TAMER = sb.registerNewTeam("TAMER");
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

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        e.setCancelled(true);
        Player p = e.getPlayer();
        TextComponent name = new TextComponent(p.getDisplayName() + ColourUtils.colour("&8 \u00BB &r" + e.getMessage()));
        HashMap<Player, String> pstring = new HashMap<>();
        if(!main.getlegends().legendscfg.contains(p.getUniqueId().toString())) {
        main.getlegends().legendscfg.set(p.getUniqueId().toString(), Team.CIVILIAN);
        }
        switch(Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString()))){
            case KNIGHT:
                pstring.put(p, ColourUtils.colour("&9&o" + StringUtils.capitalise(
                        Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString())).toString().toLowerCase())) + " ");
                break;
            case MAGE:
                pstring.put(p, ColourUtils.colour("&2&o" + StringUtils.capitalise(
                        Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString())).toString().toLowerCase())) + " ");
                break;
            case TAMER:
                pstring.put(p, ColourUtils.colour("&4&o" + StringUtils.capitalise(
                        Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString())).toString().toLowerCase())) + " ");
                break;
            case ARCHER:
                pstring.put(p, ColourUtils.colour("&6&o" + StringUtils.capitalise(
                        Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString())).toString().toLowerCase())) + " ");
                break;
            case CIVILIAN:
                pstring.put(p, ColourUtils.colour("&7&o" + StringUtils.capitalise(
                        Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString())).toString().toLowerCase())) + " ");
                break;
        }
        TextComponent comp2 = new TextComponent(pstring.get(p));
        HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(playerGroupName(p)));
        Text text = new Text(ColourUtils.colour(Arrays.stream(Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString())).getDescription()
        ).collect(Collectors.joining("\n"))));
        HoverEvent he2 = new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
        name.setHoverEvent(he);
        comp2.setHoverEvent(he2);
        for(Player online: Bukkit.getOnlinePlayers()){
            online.spigot().sendMessage(comp2, name);
        }
        Bukkit.getLogger().info(ColourUtils.colour(playerGroupName(p)) + " " +  p.getName() + ": " + e.getMessage());
    }

    public void setScores(){
        if(main.getlegends().legendscfg.get("legends." + Team.valueOf("KNIGHT").toString().toUpperCase() + ".points") == null){
            main.getlegends().legendscfg.set("legends." + Team.valueOf("KNIGHT").toString().toUpperCase() + ".points", 0);
        }
        if(main.getlegends().legendscfg.get("legends." + Team.MAGE + ".points") == null){
            main.getlegends().legendscfg.set("legends." + Team.MAGE + ".points", 0);
        }
        if(main.getlegends().legendscfg.get("legends." + Team.valueOf("TAMER").toString().toUpperCase() + ".points") == null){
            main.getlegends().legendscfg.set("legends." + Team.valueOf("TAMER").toString().toUpperCase() + ".points", 0);
        }
        if(main.getlegends().legendscfg.get("legends." + Team.valueOf("ARCHER").toString().toUpperCase() + ".points") == null){
            main.getlegends().legendscfg.set("legends." + Team.valueOf("ARCHER").toString().toUpperCase() + ".points", 0);
        }
        Score aScore = getObj().getScore(ChatColor.BLUE + "KNIGHTS:");
        Score nScore = getObj().getScore(ChatColor.DARK_GREEN + "MAGES:");
        Score wScore = getObj().getScore(ChatColor.DARK_RED + "TAMERS:");
        Score vScore = getObj().getScore(ChatColor.GOLD + "ARCHERS:");
        aScore.setScore((Integer) main.getlegends().legendscfg.get("legends." + Team.valueOf("KNIGHT").toString().toUpperCase() + ".points"));
        nScore.setScore((Integer) main.getlegends().legendscfg.get("legends." + Team.MAGE + ".points"));
        wScore.setScore((Integer) main.getlegends().legendscfg.get("legends." + Team.valueOf("TAMER").toString().toUpperCase() + ".points"));
        vScore.setScore((Integer) main.getlegends().legendscfg.get("legends." + Team.valueOf("ARCHER").toString().toUpperCase() + ".points"));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String legendName = main.getlegends().legendscfg.getString(p.getUniqueId().toString());
        getObj();
        setScores();
        p.setScoreboard(sb);
        if(main.getlegends().legendscfg.contains(p.getUniqueId().toString())) {
            if(legendName.equals("KNIGHT")) {
                //main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.BLUE + "KNIGHT: " + ChatColor.WHITE + p.getName()), 1L);
            }

            if(legendName.equals("MAGE")) {
                //main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.DARK_GREEN + "MAGE: " + ChatColor.WHITE + p.getName()), 1L);
                p.setWalkSpeed(0.3f);
                p.setFlySpeed(0.3f);
            }

            if(legendName.equals("TAMER")) {
                //main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.DARK_RED + "TAMER: " + ChatColor.WHITE + p.getName()), 1L);
            }

            if(legendName.equals("ARCHER")){
                //main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.GOLD + "ARCHER: " + ChatColor.WHITE + p.getName()), 1L);
            }
            for(Player online: Bukkit.getOnlinePlayers()){
                String olegendName = (String) main.getlegends().legendscfg.get(online.getUniqueId().toString());
                if(!olegendName.equals(legendName)){
                    online.sendTitle(ChatColor.DARK_RED + "An enemy emerges...", ChatColor.GOLD + "Defend what's yours.", 25, 60, 25);
                }
            }
        } else if(!main.getlegends().legendscfg.contains(p.getUniqueId().toString()) && !p.getOpenInventory().getTitle().equals("legends")) {
            TextComponent message = new TextComponent("Looks like you haven't selected a class! Click me to do so!");
            message.setColor(ChatColor.GREEN);
            message.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click me to select a class.").create()));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/legends"));
            p.spigot().sendMessage(message);
        }


    } // onJoin

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        Entity damager = e.getDamager();
        Entity damaged = e.getEntity();
        if(damager instanceof Player && damaged instanceof Player){
            Player pdamager = (Player) damager;
            Player pdamaged = (Player) damaged;
            String drlegendName = main.getlegends().legendscfg.getString(pdamager.getUniqueId().toString());
            String ddlegendName = main.getlegends().legendscfg.getString(pdamaged.getUniqueId().toString());
            if(drlegendName.equalsIgnoreCase(ddlegendName)){
                e.setCancelled(true);
                pdamager.sendTitle(ChatColor.DARK_RED + "No!", ChatColor.WHITE + "Do not hit legendmates.", 15, 15, 15);
            } else {
                if(Team.valueOf(main.getlegends().legendscfg.getString(pdamager.getUniqueId().toString())) == Team.TAMER){
                    if(!pdamaged.hasPotionEffect(PotionEffectType.WITHER)) {
                        pdamaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 150, 1));
                    }

                }
                else if(Team.valueOf(main.getlegends().legendscfg.getString(pdamager.getUniqueId().toString())) == Team.MAGE){
                    if(!pdamager.hasPotionEffect(PotionEffectType.SPEED)) {
                        pdamager.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 2, false, false));
                    }
                }
                else if(Team.valueOf(main.getlegends().legendscfg.getString(pdamager.getUniqueId().toString())) == Team.ARCHER){
                   pdamaged.setHealth(pdamaged.getHealth()-1.5);
                }
                else if(Team.valueOf(main.getlegends().legendscfg.getString(pdamager.getUniqueId().toString())) == Team.KNIGHT){
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
            if(Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString())) == Team.ARCHER){
                e.setDamage(3);
            }
        }
        if(damager instanceof Zombie || damager instanceof Skeleton || damager instanceof Phantom || damager instanceof Wither || damager instanceof WitherSkeleton){
            if(damaged instanceof Player){
                Player p = (Player) e.getEntity();
                if(Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString())) == Team.KNIGHT){
                    e.setCancelled(true);
                }
            }
        }
        if(damager instanceof Player && !(damaged instanceof Player)){
            Player p = (Player) e.getEntity();
            if(Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString())) == Team.MAGE) {
                ((LivingEntity) damaged).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false));
            }
        }
        if(damager instanceof Witch){
            if(damaged instanceof Player){
                Player p = (Player) e.getEntity();
                if(Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString())) == Team.ARCHER){
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
            String krlegendName = main.getlegends().legendscfg.getString(killer.getUniqueId().toString());
            String kdlegendName = main.getlegends().legendscfg.getString(killed.getUniqueId().toString());
            if(krlegendName.equalsIgnoreCase("KNIGHT")){
                apoints+=1;
                main.getlegends().legendscfg.set("legends."+ legend.valueOf("KNIGHT") + ".points", apoints);
                main.getlegends().savePlayers();
                setScores();
            }
            if(krlegendName.equalsIgnoreCase("MAGE")){
                npoints+=1;
                main.getlegends().legendscfg.set("legends."+ legend.valueOf("MAGE") + ".points", npoints);
                main.getlegends().savePlayers();
                setScores();
            }
            if(krlegendName.equalsIgnoreCase("ARCHER")){
                vpoints+=1;
                main.getlegends().legendscfg.set("legends."+ legend.valueOf("ARCHER").toString().toUpperCase() + ".points", vpoints);
                main.getlegends().savePlayers();
                setScores();
            }
            if(krlegendName.equalsIgnoreCase("TAMER")){
                wpoints+=1;
                main.getlegends().legendscfg.set("legends."+ legend.valueOf("TAMER").toString().toUpperCase() + ".points", wpoints);
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
                       p.sendMessage(prefix + ChatColor.AQUA + "Set your legend to " + ChatColor.BLUE + "KNIGHT.");
                       //main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.BLUE + "KNIGHT: " + ChatColor.WHITE + p.getName()), 1L);
                       main.getlegends().legendscfg.set(p.getUniqueId().toString(), Team.KNIGHT.toString());
                       main.getlegends().savePlayers();
                       for(Player online: Bukkit.getOnlinePlayers()){
                           Team oTeam =  Team.valueOf(main.getlegends().legendscfg.getString(online.getUniqueId().toString()));
                           Team teamName = Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString()));
                           if(!oTeam.equals(teamName)){
                               online.sendTitle(ChatColor.DARK_RED + "An enemy emerges...", ChatColor.GOLD + "Defend what's yours.", 25, 60, 25);
                           }
                       }
                       p.closeInventory();
                    break;
                    case 12:
                    p.sendMessage(prefix + ChatColor.AQUA + "Set your legend to " + ChatColor.DARK_GREEN + "MAGE.");
                  //  main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.DARK_GREEN + "MAGE: " + ChatColor.WHITE + p.getName()), 1L);
                    main.getlegends().legendscfg.set(p.getUniqueId().toString(), Team.MAGE.toString());
                    main.getlegends().savePlayers();
                    p.setWalkSpeed(0.3f);
                    p.setFlySpeed(0.3f);
                        for(Player online: Bukkit.getOnlinePlayers()){
                            Team oTeam =  Team.valueOf(main.getlegends().legendscfg.getString(online.getUniqueId().toString()));
                            Team teamName = Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString()));
                            if(!oTeam.equals(teamName)){
                                online.sendTitle(ChatColor.DARK_RED + "An enemy emerges...", ChatColor.GOLD + "Defend what's yours.", 25, 60, 25);
                            }
                        }
                    p.closeInventory();
                    break;
                    case 14:
                    p.sendMessage(prefix + ChatColor.AQUA + "Set your legend to " + ChatColor.DARK_RED + "TAMER.");
                   // main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.DARK_RED + "TAMER: " + ChatColor.WHITE + p.getName()), 1L);
                    main.getlegends().legendscfg.set(p.getUniqueId().toString(), Team.ARCHER.toString());
                    main.getlegends().savePlayers();
                        for(Player online: Bukkit.getOnlinePlayers()){
                            Team oTeam =  Team.valueOf(main.getlegends().legendscfg.getString(online.getUniqueId().toString()));
                            Team teamName = Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString()));
                            if(!oTeam.equals(teamName)){
                                online.sendTitle(ChatColor.DARK_RED + "An enemy emerges...", ChatColor.GOLD + "Defend what's yours.", 25, 60, 25);
                            }
                        }
                    p.closeInventory();
                    break;
                    case 16:
                    p.sendMessage(prefix + ChatColor.AQUA + "Set your legend to " + ChatColor.GOLD + "ARCHER.");
                   // main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> p.setPlayerListName(ChatColor.GOLD + "ARCHER: " + ChatColor.WHITE + p.getName()), 1L);
                    main.getlegends().legendscfg.set(p.getUniqueId().toString(), Team.ARCHER.toString());
                    main.getlegends().savePlayers();
                        for(Player online: Bukkit.getOnlinePlayers()){
                            Team oTeam =  Team.valueOf(main.getlegends().legendscfg.getString(online.getUniqueId().toString()));
                            Team teamName = Team.valueOf(main.getlegends().legendscfg.getString(p.getUniqueId().toString()));
                            if(!oTeam.equals(teamName)){
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
           if(!main.getlegends().legendscfg.contains(p.getUniqueId().toString())){
               main.getServer().getScheduler().runTaskLater(main, () -> {
                    if(!p.getOpenInventory().getTitle().equals("legends")){
                        p.openInventory(inv);
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
    public void entityTame(EntityTameEvent e){
       Player p = (Player) e.getOwner();
       Entity ent = e.getEntity();
        if(!(main.getlegends().legendscfg.getString(p.getUniqueId().toString()) == null)){
            if(!main.getlegends().legendscfg.getString(p.getUniqueId().toString()).equalsIgnoreCase("TAMER") || !p.hasPermission("legends.bypass.tame")){
                e.setCancelled(true);
                p.sendMessage(ChatColor.DARK_RED + "You must be a Tamer to breed/tame this animal.");
            }
        }
    }

    @EventHandler
    public void entityBreed(EntityBreedEvent e){
        Player p = (Player) e.getBreeder();
        Entity ent = e.getEntity();
        if(!(main.getlegends().legendscfg.getString(p.getUniqueId().toString()) == null)){
            if(!main.getlegends().legendscfg.getString(p.getUniqueId().toString()).equalsIgnoreCase("TAMER") || !p.hasPermission("legends.bypass.tame")){
                e.setCancelled(true);
                p.sendMessage(ChatColor.DARK_RED + "You must be a Tamer to breed/tame this animal.");
            }
        }
    }


    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(!(p.hasPermission("legends.bypass.craft") || main.getlegends().legendscfg.getString(p.getUniqueId().toString()).equalsIgnoreCase("KNIGHT"))){
            if (Material.STONE_SWORD.equals(e.getRecipe().getResult())) {
                p.sendMessage(ChatColor.RED + "&4You must be a Knight to craft this item.");
                e.setCancelled(true);
            }
            if (Material.WOODEN_SWORD.equals(e.getRecipe().getResult())) {
                p.sendMessage(ChatColor.RED + "&4You must be a Knight to craft this item.");
                e.setCancelled(true);
            }
            if (Material.DIAMOND_SWORD.equals(e.getRecipe().getResult())) {
                p.sendMessage(ChatColor.RED + "&4You must be a Knight to craft this item.");
                e.setCancelled(true);
            }
            if (Material.IRON_SWORD.equals(e.getRecipe().getResult())) {
                p.sendMessage(ChatColor.RED + "&4You must be a Knight to craft this item.");
                e.setCancelled(true);
            }
            if (Material.GOLDEN_SWORD.equals(e.getRecipe().getResult())) {
                p.sendMessage(ChatColor.RED + "&4You must be a Knight to craft this item.");
                e.setCancelled(true);
            }
            if (Material.NETHERITE_SWORD.equals(e.getRecipe().getResult())) {
                p.sendMessage(ChatColor.RED + "&4You must be a Knight to craft this item.");
                e.setCancelled(true);
            }
        }

        if(!(p.hasPermission("legends.bypass.craft") || main.getlegends().legendscfg.getString(p.getUniqueId().toString()).equalsIgnoreCase("ARCHER"))){
            if (Material.BOW.equals(e.getRecipe().getResult())) {
                p.sendMessage(ChatColor.RED + "You must be an Archer to craft this item.");
                e.setCancelled(true);
            }
            if (Material.CROSSBOW.equals(e.getRecipe().getResult())) {
                p.sendMessage(ChatColor.RED + "&4You must be an Archer to craft this item.");
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.FIREBALL) {
            event.setCancelled(true);
        }
    }

    private String playerGroupName(Player p){
        Group g = main.getAPI().getGroupManager().getGroup(main.getAPI().getUserManager().getUser(p.getUniqueId()).getPrimaryGroup());
        return ColourUtils.colour(g.getFriendlyName());
    }

}
