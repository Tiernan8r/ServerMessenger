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

public class RemoveMessage implements CommandExecutor {

	@SuppressWarnings("unused")
	private Main plugin;
	
	private ChatColor good = Colour.GOOD.getColour();
	private ChatColor warning = Colour.WARNING.getColour();
	private ChatColor highlight = Colour.HIGHLIGHT.getColour();
	private ChatColor bad = Colour.BAD.getColour();
	

	public RemoveMessage(Main main) {
		plugin = main;
	}

	/* Allows a player to add a message to be broadcasted periodically server wide */
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		//check if the player gave a message
		if(args.length < 1) {
			sender.sendMessage(warning + "Please specify the Index of the Server Message to remove, use " + highlight + "/listServerMessages " + warning + "if you are unsure of a message's index.");
			return false;
		}
		
		int index = -1;
		
		try {
			index = Integer.parseInt(args[0]);
		} catch (Exception e) {
			sender.sendMessage(bad + "Please enter an index that is actually a number!");
			e.printStackTrace();
			return false;
		}
		
		if(index < 0) {
			sender.sendMessage(warning + "The Message index must be greater than one.");
			return false;
		}
		
		HashMap<Integer, String> allMessages = ServerMessenger.getAllMessages();
		
		String message = "";
		
		if(!(allMessages.keySet().contains(index))) {
			sender.sendMessage(bad + "There is no message with the index " + highlight + args[0] + bad + "!");
			return false;
		}
		
		message = allMessages.get(index);
		message = MessageColourer.parseMessage(message, ChatColor.WHITE);
		
		ServerMessenger.removeMessage(index);
		
		sender.sendMessage(good + "The message " + ChatColor.RESET + message + good + " with index " + highlight + args[0] + good + " has been removed.");
		
		
		return true;
		
	}
}
