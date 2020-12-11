package net.digitalhazards.rtp.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public final class TeleportUtils {
   private static final Set<Material> UNSAFE_MATERIALS = new HashSet<Material>();
   public static final int RADIUS = 3;
   public static final TeleportUtils.Vector3D[] VOLUME;

   private TeleportUtils() {
   }

   public static boolean isBlockAboveAir(World world, int x, int y, int z) {
      return y > world.getMaxHeight() || world.getBlockAt(x, y - 1, z).getType().equals(Material.AIR);
   }

   public static boolean isBlockUnsafe(World world, int x, int y, int z) {
      Block block = world.getBlockAt(x, y, z);
      Block below = world.getBlockAt(x, y - 1, z);
      Block above = world.getBlockAt(x, y + 1, z);
      System.out.println(String.format("BLOCK x: %d, y: %d, z: %d", x, y, z));
      System.out.println(String.format("MATERIAL: below: %s, block: %s, above: %s", below.getType(), block.getType(), above.getType()));
      System.out.println(String.format("SOLID: below: %s, block: %s, above: %s", below.getType().isSolid(), block.getType().isSolid(), above.getType().isSolid()));
      return UNSAFE_MATERIALS.contains(below.getType()) || UNSAFE_MATERIALS.contains(block.getType()) || UNSAFE_MATERIALS.contains(above.getType())
    		  || block.getType().isSolid() || above.getType() != Material.AIR || !below.getType().isSolid()
    		  || y >= 256 || y <= 0;
   }

   public static Location safeizeLocation(Location location) {
      World world = location.getWorld();
      int x = location.getBlockX();
      int y = (int)location.getY();
      int z = location.getBlockZ();
      int origX = x;
      int origY = y;
      int origZ = z;
      y = location.getWorld().getHighestBlockYAt(location);

      while(isBlockAboveAir(world, x, y, z)) {
         --y;
         if (y <= 0) {
            y = origY;
            break;
         }
      }
      
      if (UNSAFE_MATERIALS.contains(new Location(world, x, y-1, z).getBlock().getType())) {
    	  return null;
      }
      
      for(int i = 0; isBlockUnsafe(world, x, y, z); z = origZ + VOLUME[i].z) {
    	  ++i;
    	  if (i >= VOLUME.length) {
    		  x = origX;
    		  y = origY + 3;
    		  z = origZ;
    		  break;
    	  }

         x = origX + VOLUME[i].x;
         y = origY + VOLUME[i].y;
      }

      if (isBlockUnsafe(world, x, y, z)) { return null;}


      return new Location(world, (double)x + 0.5D, (double)y, (double)z + 0.5D, location.getYaw(), location.getPitch());
   }

   static {
      UNSAFE_MATERIALS.add(Material.LAVA);
      UNSAFE_MATERIALS.add(Material.STATIONARY_LAVA);
      UNSAFE_MATERIALS.add(Material.FIRE);
      UNSAFE_MATERIALS.add(Material.WATER);
      UNSAFE_MATERIALS.add(Material.STATIONARY_WATER);


      List<TeleportUtils.Vector3D> pos = new ArrayList<TeleportUtils.Vector3D>();

      for(int x = -3; x <= 3; ++x) {
         for(int y = -3; y <= 3; ++y) {
            for(int z = -3; z <= 3; ++z) {
               pos.add(new TeleportUtils.Vector3D(x, y, z));
            }
         }
      }

      /*
      Collections.sort(pos, new Comparator<TeleportUtils.Vector3D>() {
         public int compare(TeleportUtils.Vector3D a, TeleportUtils.Vector3D b) {
            return a.x * a.x + a.y * a.y + a.z * a.z - (b.x * b.x + b.y * b.y + b.z * b.z);
         }
      });
      */
      
      VOLUME = (TeleportUtils.Vector3D[])pos.toArray(new TeleportUtils.Vector3D[0]);
   }

   public static class Vector3D {
      public int x;
      public int y;
      public int z;

      public Vector3D(int x, int y, int z) {
         this.x = x;
         this.y = y;
         this.z = z;
      }
   }
}
