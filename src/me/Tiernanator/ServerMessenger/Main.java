package me.Tiernanator.ServerMessenger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.SQL.SQLServer;
import me.Tiernanator.SQL.MySQL.MySQL;
import me.Tiernanator.ServerMessenger.Command.AddMesssage;
import me.Tiernanator.ServerMessenger.Command.ListMessages;
import me.Tiernanator.ServerMessenger.Command.RemoveMessage;
import me.Tiernanator.ServerMessenger.Functions.ServerMessenger;

public class Main extends JavaPlugin {
		
	@Override
	public void onEnable() {
		
		initialiseSQL();
		registerCommands();
		registerTasks();
	}

	@Override
	public void onDisable() {
		
		//close the SQL connection if there are any open
		try {
			getSQL().closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
	}
	
	private void registerCommands() {
		getCommand("addServerMessage").setExecutor(new AddMesssage(this));
		getCommand("listServerMessages").setExecutor(new ListMessages(this));
		getCommand("removeServerMessage").setExecutor(new RemoveMessage(this));
	}

	private void registerTasks() {
		//in runTaskTimer() first number is how long you wait the first time to start it
		// the second is how long between iterations
		ServerMessenger serverMessenger = new ServerMessenger(this);
		serverMessenger.runTaskTimerAsynchronously(this, 0, 12000);
	}
	
	//the SQL connection variable
	private static MySQL mySQL;

	//set up the connection, databases and any tables the plugin uses
	private void initialiseSQL() {
		//connect to the SQL server, the SQLServer Enumerator is a custom enum that contains the 
		// password and what not for the connection, the null is the database, which is handled below
		mySQL = new MySQL(SQLServer.HOSTNAME, SQLServer.PORT, SQLServer.DATABASE,
				SQLServer.USERNAME, SQLServer.PASSWORD);
		
		//SEt up the database if it has never been made before
//		String query = "CREATE DATABASE IF NOT EXISTS servermessenger;";
		//get the connection and execute the query
		Connection connection = null;
		try {
			connection = mySQL.openConnection();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		try {
//			statement.execute(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
		//Start using the new database
//		query = "USE servermessenger;";
		String query = "USE " + SQLServer.DATABASE.getInfo() + ";";
		//execute the query
		statement = null;
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
		
		//Create a table if you need to that holds the messages where each message has it's own unique identifier id
		query = "CREATE TABLE IF NOT EXISTS Messages ( "
				+ "ID int NOT NULL AUTO_INCREMENT, "
				+ "Message varchar(500), "
				+ "PRIMARY KEY (ID)"
				+ ");";
		//same again
		statement = null;
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
	
	//return the SQL variable
	public static MySQL getSQL() {
		return mySQL;
	}
	
	//get the SQL connection
//	public static Connection getSQLConnection() {
//
//		try {
//			if (!getSQL().checkConnection()) {
//			return getSQL().openConnection();
//		} else {
//			return getSQL().getConnection();
//		}
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
		//If there isn't a connection, open one or else return the old one, but connect to the database first
//		Connection connection = null;
//		try {
//			if (!getSQL().checkConnection()) {
//				connection = getSQL().openConnection();
//			} else {
//				connection = getSQL().getConnection();
//			}
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		String query = "USE " + SQLServer.DATABASE.getInfo() + ";";
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
//		return connection;
//	}
}
