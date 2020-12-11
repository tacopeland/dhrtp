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

	public TeleportHandler(RandomTeleport plugin, Player player, World world, int xCoord, int zCoord) {
		this.plugin = plugin;
		this.player = player;
		this.world = world;
		this.xCoord = xCoord;
		this.zCoord = zCoord;
	}

	public void teleport() {
		Location location = getLocation();
		if (location == null) {
			plugin.getMessageHandler().error(player, "Failed to find a safe teleport location!");
		} else {
			player.teleport(location);
			plugin.getMessageHandler().message(player, "Successfully teleported!", ChatColor.GREEN);
		}
	}

	protected Location getLocation() {
		int x = this.plugin.getRandom().nextInt(this.xCoord);
		int z = this.plugin.getRandom().nextInt(this.zCoord);
		int searchCount = 0;
		x = this.randomizeType(x);
		z = this.randomizeType(z);
		int y = 250;

		Location orig = new Location(this.world, (double)x, (double)y, (double)z);
		Location location = TeleportUtils.safeizeLocation(orig);

		while (location == null && searchCount < 15) {
			searchCount++;
			x = this.randomizeType(this.plugin.getRandom().nextInt(Math.abs(this.xCoord)));
			z = this.randomizeType(this.plugin.getRandom().nextInt(Math.abs(this.zCoord)));
			orig = new Location(this.world, (double)x, (double)y, (double)z);    
			location = TeleportUtils.safeizeLocation(orig);
		}
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
