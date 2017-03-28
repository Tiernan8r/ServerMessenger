package me.Tiernanator.ServerMessenger.Functions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import me.Tiernanator.Colours.MessageColourer;
import me.Tiernanator.ServerMessenger.Main;

public class ServerMessenger extends BukkitRunnable {

	private static Main plugin;

	public ServerMessenger(Main main) {
		plugin = main;
	}
	
	//the command that runs periodically that gets a random message and broadcasts it
	@Override
	public void run() {
		
		String message = getRandomMessage();
		
		if(message == null) {
			return;
		}
		
		Bukkit.getServer().broadcastMessage(message);
		
	}
	
	//Get a random message from the SQL table
	public static String getRandomMessage() {
		
		//the SQL query to execute where you get only 1 random value from the table
		String query = "SELECT Message FROM Messages ORDER BY RAND() LIMIT 1;";

		//get the SQL connection and prepare a SQL statement to execute the query
		Connection connection = null;
		try {
			connection = Main.getSQL().openConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.closeOnCompletion();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//get the result of the query, in this case a random message
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//check if the result contained any data, if not return null
		try {
			if (!resultSet.isBeforeFirst()) {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//select the result of the query
		try {
			resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//get the message from the result
		String message = "";
		try {
			message = resultSet.getString("Message");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//interpret any colour codes and return the message, the WHITE is the default chatcolour for the message
		String formattedMessage = MessageColourer.parseMessage(message, ChatColor.WHITE);

		return formattedMessage;

	}
	
	//adds a message to the SQL table
	public static void addMessage(String message) {
		
		BukkitRunnable runnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				//get the connection and prepare a statement
				Connection connection = Main.getSQL().getConnection();
				PreparedStatement preparedStatement = null;
				try {
					//the Messages is the table to insert into, the Message is the row and you replace the ? with your
					// data value
					preparedStatement = connection.prepareStatement(
							"INSERT INTO Messages (Message) VALUES (?);");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				//set the ? to your message
				try {
					preparedStatement.setString(1, message);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				//insert the data into the table
				try {
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		};
		runnable.runTaskAsynchronously(plugin);
		
//		//get the connection and prepare a statement
//		Connection connection = Main.getSQLConnection();
//		PreparedStatement preparedStatement = null;
//		try {
//			//the Messages is the table to insert into, the Message is the row and you replace the ? with your
//			// data value
//			preparedStatement = connection.prepareStatement(
//					"INSERT INTO Messages (Message) VALUES (?);");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		//set the ? to your message
//		try {
//			preparedStatement.setString(1, message);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		//insert the data into the table
//		try {
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			preparedStatement.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}

	//tests if the SQL table has such a message
	public static boolean messageExists(String message) {
		
		HashMap<Integer, String> allMessages = getAllMessages();
		if(allMessages == null || allMessages.isEmpty()) {
			return false;
		}
		Collection<String> messages = allMessages.values();
		if(messages == null || messages.isEmpty()) {
			return false;
		}
		return messages.contains(message);
		
//		//try to extract the message from the table, if you get a match the message exists and you 
//		// don't need to add it
//		String query = "SELECT * FROM Messages WHERE Message = '" + message + "';";
//
//		Connection connection = Main.getSQL().getConnection();
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		ResultSet resultSet = null;
//		try {
//			resultSet = statement.executeQuery(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		//test if there were any results, if not the message doesn't exist 
//		try {
//			if (!resultSet.isBeforeFirst()) {
//				return false;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			resultSet.next();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		//get the results message
//		String thisMessage = null;
//		try {
//			thisMessage = resultSet.getString("Message");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		//compare the two messages
//		if(thisMessage.equalsIgnoreCase(message)) {
//			return true;
//		} else {
//			return false;
//		}
	}
	
	public static HashMap<Integer, String> getAllMessages() {
		
		String query = "SELECT * FROM Messages;";

		Connection connection = Main.getSQL().getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			if (!resultSet.isBeforeFirst()) {
				return null;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		HashMap<Integer, String> allEntries = new HashMap<Integer, String>();
		
		try {
			while(resultSet.next()) {
				
				int index = 0;
				try {
					index = resultSet.getInt("ID");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				String message = "";
				try {
					message = resultSet.getString("Message");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				allEntries.put(index, message);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allEntries;
	}
	
	public static void removeMessage(int index) {
		
		BukkitRunnable runnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				String query = "DELETE FROM Messages "
						+ "WHERE ID = '" + index + "';"; 
				
				Connection connection = Main.getSQL().getConnection();
				Statement statement = null;
				try {
					statement = connection.createStatement();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					statement.execute(query);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
		runnable.runTaskAsynchronously(plugin);
		
//		String query = "DELETE FROM Messages "
//				+ "WHERE ID = '" + index + "';"; 
//		
//		Connection connection = Main.getSQLConnection();
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			statement.execute(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			statement.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
	}
	
}
