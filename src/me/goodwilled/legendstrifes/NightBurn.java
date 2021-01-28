package me.goodwilled.legendstrifes;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightBurn {
    Main main;
    public NightBurn (Main main) {this.main = main;}
    public void nightBurn(){
        int x = 3*20;
            Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
                for(Player p: Bukkit.getOnlinePlayers()){
                    if(main.getLegends().legendscfg.getString(p.getUniqueId().toString()).equalsIgnoreCase("UNDEAD")){

                                long time = Bukkit.getServer().getWorld(p.getWorld().getName()).getTime();
                                if(time > 0 && time < 12040) {
                                    if(p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
                                        p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                                    }
                                    if (p.getWorld().getHighestBlockYAt(p.getLocation()) < p.getLocation().getY()) {
                                        if (p.getGameMode() == GameMode.SURVIVAL) {
                                            p.setFireTicks(20);
                                        }
                                    } else {
                                        p.setFireTicks(0);
                                    }
                                } else {
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1));
                                }
                            }

                }
            }, 0L, 1L);

        }
    }

