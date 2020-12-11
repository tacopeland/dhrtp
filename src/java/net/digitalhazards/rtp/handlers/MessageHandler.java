package net.digitalhazards.rtp.handlers;

import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageHandler {
	private static String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "D" + ChatColor.GRAY + "H" + ChatColor.DARK_GRAY + "] ";
	
   public void message(Player player, String message, ChatColor color) {
      player.sendMessage(prefix + color + message);
   }

   public void message(Player player, String message) {
      player.sendMessage(prefix + message);
   }

   public void error(Player player, String error) {
      player.sendMessage(prefix + ChatColor.RED + "ERROR: " + error);
   }

   public void error(CommandSender sender, String error, Level level) {
      String prefix = "[" + level + "]" + "[RandomTP] ";
      sender.sendMessage(prefix + error);
   }
}
