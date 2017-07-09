package me.Tiernanator.ServerMessenger.Functions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import me.Tiernanator.Colours.MessageColourer;
import me.Tiernanator.SQL.SQLServer;
import me.Tiernanator.ServerMessenger.ServerMessengerMain;

public class ServerMessenger extends BukkitRunnable {

	@SuppressWarnings("unused")
	private static ServerMessengerMain plugin;

	public ServerMessenger(ServerMessengerMain main) {
		plugin = main;
	}

	// the command that runs periodically that gets a random message and
	// broadcasts it
	@Override
	public void run() {

		String message = getRandomMessage();

		if (message == null) {
			return;
		}

		Bukkit.getServer().broadcastMessage(message);

	}

	// Get a random message from the SQL table
	public static String getRandomMessage() {

		// the SQL query to execute where you get only 1 random value from the
		// table
		String query = "SELECT Message FROM Messages ORDER BY RAND() LIMIT 1;";
		String message = SQLServer.getString(query, "Message");

		// interpret any colour codes and return the message, the WHITE is the
		// default chatcolour for the message
		String formattedMessage = MessageColourer.parseMessage(message,
				ChatColor.WHITE);

		return formattedMessage;

	}

	// adds a message to the SQL table
	public static void addMessage(String message) {

		String statement = "INSERT INTO Messages (Message) VALUES (?);";
		Object[] values = new Object[] {message};
		SQLServer.executePreparedStatement(statement, values);

	}

	// tests if the SQL table has such a message
	public static boolean messageExists(String message) {

		List<String> allMessages = getAllMessages();
		if (allMessages == null || allMessages.isEmpty()) {
			return false;
		}
		return allMessages.contains(message);

	}

	public static List<String> getAllMessages() {

		String query = "SELECT * FROM Messages;";

		List<Object> results = SQLServer.getList(query, "Message");
		if(results == null) {
			return null;
		}
		List<String> allMessages = new ArrayList<String>();
		for(Object result : results) {
			allMessages.add((String) result);
		}
		return allMessages;
		
	}

	public static void removeMessage(int index) {

		List<String> allMessages = getAllMessages();
		String message = allMessages.get(index);
		
		String query = "DELETE FROM Messages WHERE Message = '" + message + "';";
		SQLServer.executeQuery(query);

	}

}
