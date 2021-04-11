package me.Tiernanator.ServerMessenger.Command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Tiernanator.ServerMessenger.ServerMessengerMain;
import me.Tiernanator.ServerMessenger.Functions.ServerMessenger;
import me.Tiernanator.Utilities.Colours.Colour;
import me.Tiernanator.Utilities.Colours.MessageColourer;

public class ListMessages implements CommandExecutor {

	@SuppressWarnings("unused")
	private ServerMessengerMain plugin;
	
	private ChatColor good = Colour.GOOD.getColour();
	private ChatColor informative = Colour.INFORMATIVE.getColour();
	private ChatColor bad = Colour.BAD.getColour();
	
	public ListMessages(ServerMessengerMain main) {
		plugin = main;
	}

	/* Allows a player to add a message to be broadcasted periodically server wide */
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		List<String> allMessages = ServerMessenger.getAllMessages();

		if(allMessages == null || allMessages.isEmpty()) {
			sender.sendMessage(bad + "There are no Server Messages.");
			return false;
		}
		
		sender.sendMessage(good + "The Server Messages are:");
		for(int i = 0; i < allMessages.size(); i++) {
			
			String messageUnformatted = allMessages.get(i);
			String message = MessageColourer.parseMessage(messageUnformatted, ChatColor.WHITE);
			
			String index = Integer.toString(i + 1);
			
			sender.sendMessage(informative + " - " + index + ": " + ChatColor.RESET + message);
		}
		
		return true;
		
	}
}
