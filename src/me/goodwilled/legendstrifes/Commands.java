package me.goodwilled.legendstrifes;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;


public class Commands implements CommandExecutor {

    private Main main;
    public Commands(Main main){ this.main = main; }
    Events e;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "legends" + ChatColor.DARK_GRAY + "] ";
        Player p = (Player) sender;
        if(cmd.getLabel().equalsIgnoreCase("legends")){
            p.openInventory(legendsGUI());
        }
        return true;
    } // onCommand

    public static Inventory legendsGUI(){
        Inventory inv = Bukkit.createInventory(null, 27, "legends");
        ItemStack KNIGHT = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemStack MAGE = new ItemStack(Material.ENDER_PEARL, 1);
        ItemStack TAMER = new ItemStack(Material.COW_SPAWN_EGG, 1);
        ItemStack ARCHER = new ItemStack(Material.BOW, 1);
        ItemMeta aMeta = KNIGHT.getItemMeta();
        ItemMeta nMeta = MAGE.getItemMeta();
        ItemMeta wMeta = TAMER.getItemMeta();
        ItemMeta vMeta = ARCHER.getItemMeta();
        ArrayList<String> aLore = new ArrayList<String>();
        ArrayList<String> nLore = new ArrayList<String>();
        ArrayList<String> vLore = new ArrayList<String>();
        ArrayList<String> wLore = new ArrayList<String>();
        aMeta.setDisplayName(ChatColor.BLUE + "KNIGHT");
        nMeta.setDisplayName(ChatColor.DARK_GREEN + "MAGE");
        wMeta.setDisplayName(ChatColor.DARK_RED + "TAMER");
        vMeta.setDisplayName(ChatColor.GOLD + "ARCHER");
        aLore.add("Ability to craft swords.");
        nLore.add( "Magic.");
        vLore.add("Animal army!");
        wLore.add("Shoot like a Skeleton.");
        aMeta.setLore(aLore);
        nMeta.setLore(nLore);
        wMeta.setLore(wLore);
        vMeta.setLore(vLore);
        KNIGHT.setItemMeta(aMeta);
        ARCHER.setItemMeta(vMeta);
        MAGE.setItemMeta(nMeta);
        TAMER.setItemMeta(wMeta);
        inv.setItem(10, KNIGHT);
        inv.setItem(12, MAGE);
        inv.setItem(14, TAMER);
        inv.setItem(16, ARCHER);
        return inv;
    }

} // class