package net.digitalhazards.rtp.commands;

import net.digitalhazards.rtp.RandomTeleport;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTPCommand implements CommandExecutor {
	private final RandomTeleport plugin;

	public RTPCommand(RandomTeleport plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("randomtp")) {
			if (!(sender instanceof Player)) {
				this.plugin.getMessageHandler().error(sender, "A player is expected!", Level.WARNING);
				return true;
			} else {
				Player player = (Player)sender;
				if (!player.hasPermission("randomtp.tp")) {
					this.plugin.getMessageHandler().error(player, "Insufficient permissions!\nPermissions Needed: randomtp.tp");
					return true;
				} else if (args.length > 0) {
					this.plugin.getMessageHandler().error(player, "Invalid Command Syntax!\nUsage: /randomtp");
					return true;
				} else {
					this.plugin.teleportPlayer(player, this.plugin.getConfigHandler().getRTPWorld());
					return true;
				}
			}
		} else {
			return false;
		}
	}
}
