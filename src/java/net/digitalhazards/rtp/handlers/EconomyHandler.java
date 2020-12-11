package net.digitalhazards.rtp.handlers;

import net.digitalhazards.rtp.RandomTeleport;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EconomyHandler {
   private final RandomTeleport plugin;

   public EconomyHandler(RandomTeleport plugin) {
      this.plugin = plugin;
   }

   public boolean check(Player player, double amount) {
      return this.plugin.getEconomy().has(player, amount);
   }

   public void remove(Player player, double amount) {
      Economy economy = this.plugin.getEconomy();
      economy.withdrawPlayer(player, amount);
      double remaining = economy.getBalance(player);
      player.sendMessage(ChatColor.AQUA + "" + economy.format(amount) + ChatColor.DARK_AQUA + " was removed from your account for teleporting!\n" + ChatColor.DARK_AQUA + "You have " + ChatColor.AQUA + economy.format(remaining) + ChatColor.DARK_AQUA + " left in your account.");
   }
}
