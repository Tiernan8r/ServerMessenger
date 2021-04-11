package me.Tiernanator.ServerMessenger.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Tiernanator.ServerMessenger.Functions.ServerMessenger;

public class Broadcast implements CommandExecutor {

	public Broadcast() {
	}

	/* Allows a player to add a message to be broadcasted periodically server wide */
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		String message = ServerMessenger.getRandomMessage();

		if (message == null) {
			return false;
		}

		Bukkit.getServer().broadcastMessage(message);
		
		return true;
		
	}
}
