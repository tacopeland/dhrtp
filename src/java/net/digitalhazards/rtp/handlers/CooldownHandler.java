package net.digitalhazards.rtp.handlers;

import net.digitalhazards.rtp.RandomTeleport;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class CooldownHandler {
	private final RandomTeleport plugin;
	private final HashMap<UUID, Long> cooldowns = new HashMap<UUID, Long>();

	public CooldownHandler(RandomTeleport plugin) {
		this.plugin = plugin;
	}

	public void startCooldown(Player player) {
		this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
	}

	public void clearCooldown(Player player) {
		this.cooldowns.remove(player.getUniqueId());
	}

	public long getTimeLeft(Player player) {
		if(player.isOp() || player.hasPermission("randomtp.cooldown.bypass")) {
			return 0L;
		}
		if(this.cooldowns.containsKey(player.getUniqueId())) {
			int cooldownTime = this.plugin.getConfigHandler().getCooldownTime();
			long startTime = (System.currentTimeMillis() - this.cooldowns.get(player.getUniqueId())) / 1000L;
			return startTime - (long)cooldownTime;
		} else {
			return 0L;
		}
	}

	public boolean check(Player player) {
		if (this.cooldowns.containsKey(player.getUniqueId())) {
			if(getTimeLeft(player) >= 0) {
				this.cooldowns.remove(player.getUniqueId());
				return true;
			}
			return false;
		} else {
			return true;
		}
	}

	public boolean isCooldownsEmpty() {
		return this.cooldowns.isEmpty();
	}
}
