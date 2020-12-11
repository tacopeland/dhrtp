package net.digitalhazards.rtp.handlers;

import net.digitalhazards.rtp.RandomTeleport;
import java.util.List;
import java.util.Set;
import org.bukkit.World;

public class ConfigHandler {
   private final RandomTeleport plugin;
   private final List<String> biomeBlacklist;
   private final List<String> blockBlacklist;

   public ConfigHandler(RandomTeleport plugin) {
      this.plugin = plugin;
      this.biomeBlacklist = plugin.getConfig().getStringList("biome-blacklist");
      this.blockBlacklist = plugin.getConfig().getStringList("block-blacklist");
   }

   public List<String> getBiomeBlacklist() {
      return this.biomeBlacklist;
   }

   public List<String> getBlockBlacklist() {
      return this.blockBlacklist;
   }

   public boolean isAutoUpdateEnabled() {
      return this.plugin.getConfig().getBoolean("update.auto-update", false);
   }

   public boolean isWorldRTPEnabled(World world) {
      Set<String> set = this.plugin.getConfig().getConfigurationSection("worlds").getKeys(false);
      return set.contains(world.getName());
   }

   public int getMaxX(World world) {
      return (int)this.plugin.getConfig().getDouble("worlds." + world.getName() + ".max-x", 1000.0D);
   }

   public int getMaxZ(World world) {
      return (int)this.plugin.getConfig().getDouble("worlds." + world.getName() + ".max-z", 1000.0D);
   }

   public double getCost(World world) {
      return this.plugin.getConfig().getDouble("worlds." + world.getName() + ".cost", 0.0D);
   }

   public boolean isCostEnabled(World world) {
      return this.getCost(world) != 0.0D;
   }

   public boolean isCooldownEnabled(World world) {
      return this.getCooldownTime(world) != 0;
   }

   public int getCooldownTime(World world) {
      return this.plugin.getConfig().getInt("worlds." + world.getName() + ".cooldown", 0);
   }
}
