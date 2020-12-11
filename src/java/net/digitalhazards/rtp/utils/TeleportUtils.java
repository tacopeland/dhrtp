package net.digitalhazards.rtp.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import net.digitalhazards.rtp.RandomTeleport;

public final class TeleportUtils {
   private static final Set<Material> UNSAFE_MATERIALS = new HashSet<Material>();

   private TeleportUtils() {
   }

   public static boolean isBlockAboveAir(World world, int x, int y, int z) {
      return y > world.getMaxHeight() || world.getBlockAt(x, y - 1, z).getType().equals(Material.AIR);
   }

   public static boolean isBlockUnsafe(World world, int x, int y, int z) {
      Block block = world.getBlockAt(x, y, z);
      Block below = world.getBlockAt(x, y - 1, z);
      Block above = world.getBlockAt(x, y + 1, z);
      return UNSAFE_MATERIALS.contains(below.getType()) || UNSAFE_MATERIALS.contains(block.getType()) || UNSAFE_MATERIALS.contains(above.getType())
    		  || block.getType().isSolid() || above.getType() != Material.AIR || !below.getType().isSolid()
    		  || y >= 256 || y <= 0;
   }

   public static Location safeizeLocation(Location location) {
      World world = location.getWorld();
      int x = location.getBlockX();
      int y = (int)location.getY();
      int z = location.getBlockZ();
      y = location.getWorld().getHighestBlockYAt(location);
      
      if(RandomTeleport.getPlugin().getConfigHandler().getBiomeBlacklist().contains(location.getBlock().getBiome().toString())) return null;

      while(isBlockAboveAir(world, x, y, z)) {
         --y;
         if (y <= 0) {
            return null;
         }
      }

      if (isBlockUnsafe(world, x, y, z)) { return null;}

      return new Location(world, (double)x + 0.5D, (double)y, (double)z + 0.5D, location.getYaw(), location.getPitch());
   }

   static {
	  List<String> blocks = RandomTeleport.getPlugin().getConfigHandler().getBlockBlacklist();
	  for (String block : blocks) {
		  UNSAFE_MATERIALS.add(Material.valueOf(block));
	  }
   }
}
