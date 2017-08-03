package me.Tiernanator.ServerMessenger.Command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Tiernanator.ServerMessenger.ServerMessengerMain;
import me.Tiernanator.ServerMessenger.Functions.ServerMessenger;
import me.Tiernanator.Utilities.Colours.Colour;
import me.Tiernanator.Utilities.Colours.MessageColourer;

public class AddMesssage implements CommandExecutor {

	@SuppressWarnings("unused")
	private ServerMessengerMain plugin;

	private ChatColor good = Colour.GOOD.getColour();
	private ChatColor warning = Colour.WARNING.getColour();
	
	public AddMesssage(ServerMessengerMain main) {
		plugin = main;
	}

	/* Allows a player to add a message to be broadcasted periodically server wide */
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		//check if the player gave a message
		if(args.length < 1) {
			sender.sendMessage(warning + "Please specify a message.");
			return false;
		}
		
		//create the message from all parts of the command's message (it comes as an array)
		String message = "";
		for(int i = 0; i < args.length; i++) {
			message += args[i];
			
			//add a space between each word but not at the end of the sentence
			if(i < args.length - 1) {
				message += " ";
			}
		}
		//test if the message has already been added
		if(ServerMessenger.messageExists(message)) {
			sender.sendMessage(warning + "That message is already a Server message!");
			return false;
		}
		//add the colour and inform the player by showing them the formatted message (colour codes interpreted)
		ServerMessenger.addMessage(message);
		String colouredMessage = MessageColourer.parseMessage(message, ChatColor.WHITE);
		
		sender.sendMessage(good + "The message: " + colouredMessage + good + " has been saved for broadcasting.");
		return true;
		
	}
}
