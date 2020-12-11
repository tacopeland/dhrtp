package net.digitalhazards.rtp.handlers;

import net.digitalhazards.rtp.RandomTeleport;
import java.util.List;

import org.bukkit.Bukkit;
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

	public World getRTPWorld() {
		String name = this.plugin.getConfig().getString("world");
		return Bukkit.getServer().getWorld(name);
	}

	public int getMaxX() {
		return (int)this.plugin.getConfig().getDouble("max-x", 1000.0D);
	}

	public int getMaxZ() {
		return (int)this.plugin.getConfig().getDouble("max-z", 1000.0D);
	}

	public boolean isCooldownEnabled() {
		return this.getCooldownTime() != 0;
	}

	public int getCooldownTime() {
		return this.plugin.getConfig().getInt("cooldown", 0);
	}
}
