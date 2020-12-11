package net.digitalhazards.rtp.listeners;

import net.digitalhazards.rtp.RandomTeleport;
import net.digitalhazards.rtp.handlers.ConfigHandler;
import net.digitalhazards.rtp.handlers.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GlobalListener implements Listener {
   private final RandomTeleport plugin;
   private final MessageHandler messageHandler;

   public GlobalListener(RandomTeleport plugin) {
      this.plugin = plugin;
      this.messageHandler = plugin.getMessageHandler();
   }

   private boolean isSign(Block block) {
      return block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN;
   }

   @EventHandler(
      priority = EventPriority.NORMAL
   )
   public void onSignCreate(SignChangeEvent event) {
      Block block = event.getBlock();
      if (this.isSign(block)) {
         Player player = event.getPlayer();
         String line1 = ChatColor.stripColor(event.getLine(0));
         String line2 = ChatColor.stripColor(event.getLine(1));
         if (line1.equalsIgnoreCase("[RandomTP]")) {
            if (!player.hasPermission("randomtp.signs.create") && line1.equalsIgnoreCase("[RandomTP]")) {
               this.messageHandler.error(player, "Insufficient Permissions!", "Permission Needed: randomtp.signs.create");
               event.setCancelled(true);
               event.getBlock().breakNaturally();
            } else if (line2 != null && !line2.isEmpty()) {
               if (Bukkit.getWorld(line2) == null && !line2.equalsIgnoreCase("default")) {
                  this.messageHandler.error(player, "Invalid world!", "The world \"" + line2 + "\" doesn't exist!");
                  player.sendMessage(ChatColor.RED + "Make sure that you spelled the name exactly as on the world folder!");
                  event.setCancelled(true);
                  event.getBlock().breakNaturally();
               } else {
                  event.setLine(0, ChatColor.DARK_AQUA + line1);
                  event.setLine(1, ChatColor.DARK_GRAY + line2);
                  player.sendMessage(ChatColor.DARK_AQUA + "Successfully created RandomTP Sign! \nThis sign will teleport to " + ChatColor.AQUA + line2 + ChatColor.DARK_AQUA + "!");
               }
            } else {
               this.messageHandler.error(player, "Improper Setup!", "Line 1: [RandomTP] \nLine 2: World name (or Default)");
               event.setCancelled(true);
               event.getBlock().breakNaturally();
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.NORMAL,
      ignoreCancelled = true
   )
   public void onPlayerInteract(PlayerInteractEvent event) {
      if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
         if (event.hasBlock()) {
            Block block = event.getClickedBlock();
            if (this.isSign(block)) {
               Sign sign = (Sign)block.getState();
               if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[RandomTP]")) {
                  Player player = event.getPlayer();
                  if (!player.hasPermission("randomtp.signs.use")) {
                     this.messageHandler.error(player, "Insufficient Permissions", "Permission Needed: randomtp.signs.use");
                  } else {
                     ConfigHandler ch = this.plugin.getConfigHandler();
                     String worldName = ChatColor.stripColor(sign.getLine(1));
                     World world = worldName != null && !worldName.isEmpty() && Bukkit.getWorld(worldName) != null ? Bukkit.getWorld(worldName) : event.getClickedBlock().getWorld();
                     if (ch.isWorldRTPEnabled(world)) {
                        this.plugin.teleportPlayer(player, world);
                     }
                  }
               }
            }
         }
      }
   }
}
