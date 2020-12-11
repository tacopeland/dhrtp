package net.digitalhazards.rtp.commands;

import net.digitalhazards.rtp.RandomTeleport;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTPCommand implements CommandExecutor {
   private final RandomTeleport plugin;

   public RTPCommand(RandomTeleport plugin) {
      this.plugin = plugin;
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (command.getName().equalsIgnoreCase("randomtp")) {
         if (!(sender instanceof Player)) {
            this.plugin.getMessageHandler().error(sender, "ERROR: A player is expected!", Level.WARNING);
            return true;
         } else {
            Player player = (Player)sender;
            if (!player.hasPermission("randomtp.tp")) {
               this.plugin.getMessageHandler().error(player, "Insufficient permissions!", "Permissions Needed: randomtp.tp");
               return true;
            } else if (args.length > 0) {
               this.plugin.getMessageHandler().error(player, "Invalid Command Syntax!", "Usage: /randomtp");
               return true;
            } else {
               this.plugin.teleportPlayer(player, player.getWorld());
               return true;
            }
         }
      } else {
         return false;
      }
   }
}
