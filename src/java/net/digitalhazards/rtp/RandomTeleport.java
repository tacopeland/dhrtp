package net.digitalhazards.rtp;

import net.digitalhazards.rtp.commands.RTPCommand;
import net.digitalhazards.rtp.handlers.ConfigHandler;
import net.digitalhazards.rtp.handlers.CooldownHandler;
import net.digitalhazards.rtp.handlers.EconomyHandler;
import net.digitalhazards.rtp.handlers.MessageHandler;
import net.digitalhazards.rtp.handlers.TeleportHandler;
import net.digitalhazards.rtp.listeners.GlobalListener;
import java.util.Iterator;
import java.util.Random;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomTeleport extends JavaPlugin {
   private ConfigHandler configHandler;
   private CooldownHandler cooldownHandler;
   private EconomyHandler economyHandler;
   private MessageHandler messageHandler;
   private Economy economy;
   private Random random;

   public void onEnable() {
      this.saveDefaultConfig();
      this.configHandler = new ConfigHandler(this);
      this.cooldownHandler = new CooldownHandler(this);
      this.economyHandler = new EconomyHandler(this);
      this.messageHandler = new MessageHandler();
      this.random = new Random();
      boolean costEnabled = false;
      Iterator<String> worlds = this.getConfig().getConfigurationSection("worlds").getKeys(false).iterator();

      while(worlds.hasNext()) {
         String key = (String)worlds.next();
         if (this.getConfig().getDouble("worlds." + key + ".cost", 0.0D) != 0.0D) {
            costEnabled = true;
            break;
         }
      }

      if (!this.setupEconomy() && costEnabled) {
         this.setEnabled(false);
         this.getLogger().severe("Vault is not installed and enabled! Disabling plugin...");
      } else {
         this.registerEvents();
         this.registerCommands();
      }
   }

   private void registerCommands() {
      this.getCommand("randomtp").setExecutor(new RTPCommand(this));
   }

   private void registerEvents() {
      this.getServer().getPluginManager().registerEvents(new GlobalListener(this), this);
   }

   private boolean setupEconomy() {
      if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
         return false;
      } else {
         RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
         if (rsp == null) {
            return false;
         } else {
            this.economy = (Economy)rsp.getProvider();
            return this.economy != null;
         }
      }
   }

   public Economy getEconomy() {
      return this.economy;
   }

   public ConfigHandler getConfigHandler() {
      return this.configHandler;
   }

   public CooldownHandler getCooldownHandler() {
      return this.cooldownHandler;
   }

   public EconomyHandler getEconomyHandler() {
      return this.economyHandler;
   }

   public MessageHandler getMessageHandler() {
      return this.messageHandler;
   }

   public Random getRandom() {
      return this.random;
   }

   public void teleportPlayer(Player player, World world) {
      if (this.configHandler.isCooldownEnabled(world) && !this.cooldownHandler.isCooldownsEmpty()) {
         if (!this.cooldownHandler.check(player, world) && this.cooldownHandler.getTimeLeft(player, world) * -1L >= 1L) {
            this.messageHandler.error(player, "You can not teleport yet!", "Please wait " + this.cooldownHandler.getTimeLeft(player, world) * -1L + " seconds!");
            return;
         }

         this.messageHandler.message(player, "You can teleport again!", ChatColor.GREEN);
         this.cooldownHandler.clearCooldown(player, world);
      }

      if (this.configHandler.isCostEnabled(world)) {
         double cost = this.configHandler.getCost(world);
         if (!this.economyHandler.check(player, cost)) {
            this.messageHandler.error(player, "Insufficient Funds!", "You need at least " + cost + " to teleport!");
            return;
         }

         this.economyHandler.remove(player, cost);
      }

      int maxX = this.configHandler.getMaxX(world);
      int maxZ = this.configHandler.getMaxZ(world);
      TeleportHandler teleportHandler = new TeleportHandler(this, player, world, maxX, maxZ);
      teleportHandler.teleport();
      if (this.configHandler.isCooldownEnabled(world)) {
         this.cooldownHandler.startCooldown(player, world);
      }

   }

   public static RandomTeleport getPlugin() {
      return (RandomTeleport)Bukkit.getPluginManager().getPlugin("RandomTeleport");
   }
}
