package me.Tiernanator.ServerMessenger;

import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.ServerMessenger.Command.AddMesssage;
import me.Tiernanator.ServerMessenger.Command.Broadcast;
import me.Tiernanator.ServerMessenger.Command.ListMessages;
import me.Tiernanator.ServerMessenger.Command.RemoveMessage;
import me.Tiernanator.ServerMessenger.Functions.ServerMessenger;
import me.Tiernanator.Utilities.SQL.SQLServer;

public class ServerMessengerMain extends JavaPlugin {
		
	@Override
	public void onEnable() {
		
		initialiseSQL();
		registerCommands();
		registerTasks();
	}
	
	private void registerCommands() {
		getCommand("addServerMessage").setExecutor(new AddMesssage(this));
		getCommand("listServerMessages").setExecutor(new ListMessages(this));
		getCommand("removeServerMessage").setExecutor(new RemoveMessage(this));
		getCommand("broadcast").setExecutor(new Broadcast());
	}

	private void registerTasks() {
		//in runTaskTimer() first number is how long you wait the first time to start it
		// the second is how long between iterations
		ServerMessenger serverMessenger = new ServerMessenger(this);
		serverMessenger.runTaskTimerAsynchronously(this, 0, 12000);
	}
	
	//set up the connection, databases and any tables the plugin uses
	private void initialiseSQL() {
		
		//Create a table if you need to that holds the messages where each message has it's own unique identifier id
		String query = "CREATE TABLE IF NOT EXISTS Messages ( "
				+ "Message varchar(500)"
				+ ");";
		SQLServer.executeQuery(query);
	}
	
}
