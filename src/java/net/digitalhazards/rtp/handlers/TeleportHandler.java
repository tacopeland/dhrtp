package net.digitalhazards.rtp.handlers;

import net.digitalhazards.rtp.RandomTeleport;
import net.digitalhazards.rtp.utils.TeleportUtils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportHandler {
   private final RandomTeleport plugin;
   private final Player player;
   private final World world;
   private int xCoord = -1;
   private int zCoord = -1;
   private int xF;
   private int yF;
   private int zF;

   public TeleportHandler(RandomTeleport plugin, Player player, World world, int xCoord, int zCoord) {
      this.plugin = plugin;
      this.player = player;
      this.world = world;
      this.xCoord = xCoord;
      this.zCoord = zCoord;
   }

   public void teleport() {
      Location location = this.getLocation();
      if (location == null) {
         this.player.sendMessage(ChatColor.RED + "ERROR: Failed to find a safe teleport location!");
      } else {
         this.player.teleport(location);
         this.player.sendMessage(ChatColor.DARK_AQUA + "Teleported to the location:");
         this.player.sendMessage(ChatColor.DARK_AQUA + "X: " + ChatColor.AQUA + location.getBlockX());
         this.player.sendMessage(ChatColor.DARK_AQUA + "Y: " + ChatColor.AQUA + (int)location.getY());
         this.player.sendMessage(ChatColor.DARK_AQUA + "Z: " + ChatColor.AQUA + location.getBlockZ());
         this.player.sendMessage(ChatColor.DARK_AQUA + "World: " + ChatColor.AQUA + this.world.getName());
         this.player.sendMessage(" ");
      }
   }

   public int getX() {
      return this.xF;
   }

   public int getY() {
      return this.yF;
   }

   public int getZ() {
      return this.zF;
   }

   protected Location getLocation() {
      int x = this.plugin.getRandom().nextInt(this.xCoord);
      int z = this.plugin.getRandom().nextInt(this.zCoord);
      int searchCount = 0;
      x = this.randomizeType(x);
      z = this.randomizeType(z);
      int y = 250;
      System.out.println(String.format("Searching with bounds at x: %d and z: %d...", this.xCoord, this.zCoord));
      System.out.println(String.format("x: %d, y: %d, z: %d", x, y, z));
      Location orig = new Location(this.world, (double)x, (double)y, (double)z);
      System.out.println("Loading chunk...");
      orig.getChunk().load();
      Location location = TeleportUtils.safeizeLocation(orig);
      while (location == null && searchCount < 15) {
    	  searchCount++;
    	  x = this.randomizeType(this.plugin.getRandom().nextInt(Math.abs(this.xCoord)));
    	  y = 250;
          z = this.randomizeType(this.plugin.getRandom().nextInt(Math.abs(this.zCoord)));
          System.out.println(String.format("x: %d, y: %d, z: %d", x, y, z));
          
          orig = new Location(this.world, (double)x, (double)y, (double)z);
          System.out.println("Loading chunk...");
          orig.getChunk().load();
    	  location = TeleportUtils.safeizeLocation(orig);
      }
      System.out.println("Done after " + searchCount + " tries");
      return location;
   }

   protected int randomizeType(int i) {
      int j = this.plugin.getRandom().nextInt(2);
      switch(j) {
      case 0:
         return -i;
      case 1:
         return i;
      default:
         return -1;
      }
   }
}
