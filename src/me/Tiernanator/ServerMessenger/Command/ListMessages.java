package me.Tiernanator.ServerMessenger.Command;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Colours.MessageColourer;
import me.Tiernanator.ServerMessenger.Main;
import me.Tiernanator.ServerMessenger.Functions.ServerMessenger;

public class ListMessages implements CommandExecutor {

	@SuppressWarnings("unused")
	private Main plugin;
	
	private ChatColor good = Colour.GOOD.getColour();
	private ChatColor informative = Colour.INFORMATIVE.getColour();
	private ChatColor bad = Colour.BAD.getColour();
	
	public ListMessages(Main main) {
		plugin = main;
	}

	/* Allows a player to add a message to be broadcasted periodically server wide */
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		HashMap<Integer, String> allMessages = ServerMessenger.getAllMessages();

		if(allMessages == null || allMessages.isEmpty()) {
			sender.sendMessage(bad + "There are no Server Messages.");
			return false;
		}
		
		sender.sendMessage(good + "The Server Messages are:");
		for(int i : allMessages.keySet()) {
			
			String messageUnformatted = allMessages.get(i);
			String message = MessageColourer.parseMessage(messageUnformatted, ChatColor.WHITE);
			
			String index = Integer.toString(i);
			
			sender.sendMessage(informative + " - " + index + ": " + ChatColor.RESET + message);
		}
		
		return true;
		
	}
}
