package net.digitalhazards.rtp;

import net.digitalhazards.rtp.commands.RTPCommand;
import net.digitalhazards.rtp.handlers.ConfigHandler;
import net.digitalhazards.rtp.handlers.CooldownHandler;
import net.digitalhazards.rtp.handlers.MessageHandler;
import net.digitalhazards.rtp.handlers.TeleportHandler;
import net.digitalhazards.rtp.listeners.GlobalListener;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomTeleport extends JavaPlugin {
   private ConfigHandler configHandler;
   private CooldownHandler cooldownHandler;
   private MessageHandler messageHandler;
   private Random random;

   public void onEnable() {
      this.saveDefaultConfig();
      this.configHandler = new ConfigHandler(this);
      this.cooldownHandler = new CooldownHandler(this);
      this.messageHandler = new MessageHandler();
      this.random = new Random();

      this.registerEvents();
      this.registerCommands();
   }

   private void registerCommands() {
      this.getCommand("randomtp").setExecutor(new RTPCommand(this));
   }

   private void registerEvents() {
      this.getServer().getPluginManager().registerEvents(new GlobalListener(this), this);
   }

   public ConfigHandler getConfigHandler() {
      return this.configHandler;
   }

   public CooldownHandler getCooldownHandler() {
      return this.cooldownHandler;
   }

   public MessageHandler getMessageHandler() {
      return this.messageHandler;
   }

   public Random getRandom() {
      return this.random;
   }

   public void teleportPlayer(Player player, World world) {
      if (this.configHandler.isCooldownEnabled() && !this.cooldownHandler.isCooldownsEmpty()) {
         if (!this.cooldownHandler.check(player) && this.cooldownHandler.getTimeLeft(player) * -1L >= 1L) {
            this.messageHandler.error(player, "You can not teleport yet!\nPlease wait " + this.cooldownHandler.getTimeLeft(player) * -1L + " seconds!");
            return;
         }

         this.messageHandler.message(player, "You can teleport again!", ChatColor.GREEN);
         this.cooldownHandler.clearCooldown(player);
      }

      int maxX = this.configHandler.getMaxX();
      int maxZ = this.configHandler.getMaxZ();
      TeleportHandler teleportHandler = new TeleportHandler(this, player, world, maxX, maxZ);
      teleportHandler.teleport();
      if (this.configHandler.isCooldownEnabled()) {
         this.cooldownHandler.startCooldown(player);
      }

   }

   public static RandomTeleport getPlugin() {
      return (RandomTeleport)Bukkit.getPluginManager().getPlugin("RandomTeleport");
   }
}
