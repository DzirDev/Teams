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
        String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Legends" + ChatColor.DARK_GRAY + "] ";
        Player p = (Player) sender;

        return true;
    } // onCommand

    public static Inventory legendsGUI(){
        Inventory inv = Bukkit.createInventory(null, 27, "Legends");
        ItemStack undead = new ItemStack(Material.DIAMOND_BOOTS, 1);
        ItemStack ninjas = new ItemStack(Material.ENDER_PEARL, 1);
        ItemStack warriors = new ItemStack(Material.SHIELD, 1);
        ItemStack vikings = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta aMeta = undead.getItemMeta();
        ItemMeta nMeta = ninjas.getItemMeta();
        ItemMeta wMeta = warriors.getItemMeta();
        ItemMeta vMeta = vikings.getItemMeta();
        ArrayList<String> aLore = new ArrayList<String>();
        ArrayList<String> nLore = new ArrayList<String>();
        ArrayList<String> vLore = new ArrayList<String>();
        ArrayList<String> wLore = new ArrayList<String>();
        aMeta.setDisplayName(ChatColor.BLUE + "Undead");
        nMeta.setDisplayName(ChatColor.DARK_GREEN + "Ninjas");
        wMeta.setDisplayName(ChatColor.DARK_RED + "Wizards");
        vMeta.setDisplayName(ChatColor.GOLD + "Vikings");
        aLore.add("Jump higher when in combat.");
        nLore.add( "Run faster when in combat.");
        vLore.add("Deal more damage when in combat.");
        wLore.add("Take less damage when in combat.");
        aMeta.setLore(aLore);
        nMeta.setLore(nLore);
        wMeta.setLore(wLore);
        vMeta.setLore(vLore);
        undead.setItemMeta(aMeta);
        vikings.setItemMeta(vMeta);
        ninjas.setItemMeta(nMeta);
        warriors.setItemMeta(wMeta);
        inv.setItem(10, undead);
        inv.setItem(12, ninjas);
        inv.setItem(14, warriors);
        inv.setItem(16, vikings);
        return inv;
    }

} // class