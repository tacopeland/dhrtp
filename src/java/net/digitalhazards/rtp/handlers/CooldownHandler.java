package net.digitalhazards.rtp.handlers;

import net.digitalhazards.rtp.RandomTeleport;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class CooldownHandler {
   private final RandomTeleport plugin;
   private final Map<String, HashMap<UUID, Long>> cooldowns = new HashMap<String, HashMap<UUID, Long>>();

   public CooldownHandler(RandomTeleport plugin) {
      this.plugin = plugin;
   }

   public void startCooldown(Player player, World world) {
      if (!this.cooldowns.containsKey(world.getName())) {
         this.cooldowns.put(world.getName(), new HashMap<UUID, Long>());
      }

      ((Map<UUID, Long>)this.cooldowns.get(world.getName())).put(player.getUniqueId(), System.currentTimeMillis());
   }

   public void clearCooldown(Player player, World world) {
      ((Map<UUID, Long>)this.cooldowns.get(world.getName())).remove(player.getUniqueId());
   }

   public long getTimeLeft(Player player, World world) {
      if (((Map<UUID, Long>)this.cooldowns.get(world.getName())).containsKey(player.getUniqueId())) {
         int cooldownTime = this.plugin.getConfigHandler().getCooldownTime(world);
         long startTime = (System.currentTimeMillis() - (Long)((Map<UUID, Long>)this.cooldowns.get(world.getName())).get(player.getUniqueId())) / 1000L;
         return startTime - (long)cooldownTime;
      } else {
         return 0L;
      }
   }

   public boolean check(Player player, World world) {
      if (!this.cooldowns.containsKey(world.getName())) {
         this.cooldowns.put(world.getName(), new HashMap<UUID, Long>());
      }

      if (((Map<UUID, Long>)this.cooldowns.get(world.getName())).containsKey(player.getUniqueId())) {
         int cooldownTime = this.plugin.getConfigHandler().getCooldownTime(world);
         return ((Long)((Map<UUID, Long>)this.cooldowns.get(world.getName())).get(player.getUniqueId()) - System.currentTimeMillis()) / 1000L >= (long)cooldownTime;
      } else {
         return true;
      }
   }

   public boolean isCooldownsEmpty() {
      return this.cooldowns.isEmpty();
   }
}
