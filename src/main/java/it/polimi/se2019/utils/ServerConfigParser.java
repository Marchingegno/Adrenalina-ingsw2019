package it.polimi.se2019.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ServerConfigParser {

	private static final String PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "resources" + File.separator + "server-config.json";
	private static boolean triedParsing = false;
	private static ServerConfig serverConfig;


	public static long getWaitingTimeInLobbyMs() {
		// Parse the file if not already parsed.
		if (!triedParsing)
			parseConfig();

		// Return the config or a default value.
		if (serverConfig != null) {
			return serverConfig.getWaitingTimeInLobbyMs();
		} else {
			Utils.logWarning("Returned the default value for waiting time in lobby.");
			return 5000L;
		}
	}

	public static long getTurnTimeLimitMs() {
		// Parse the file if not already parsed.
		if(!triedParsing)
			parseConfig();

		// Return the config or a default value.
		if(serverConfig != null) {
			return serverConfig.getTurnTimeLimitMs();
		} else {
			Utils.logWarning("Returned the default value for move time limit.");
			return 20000L;
		}
	}

	public static String getHost() {
		// Parse the file if not already parsed.
		if (!triedParsing)
			parseConfig();

		// Return the config or a default value.
		if (serverConfig != null) {
			return serverConfig.getHost();
		} else {
			Utils.logWarning("Returned the default value for the host.");
			return "localhost";
		}
	}

	public static int getRmiPort() {
		// Parse the file if not already parsed.
		if (!triedParsing)
			parseConfig();

		// Return the config or a default value.
		if (serverConfig != null) {
			return serverConfig.getRmiPort();
		} else {
			Utils.logWarning("Returned the default value for RMI port.");
			return 1099;
		}
	}

	public static int getSocketPort() {
		// Parse the file if not already parsed.
		if (!triedParsing)
			parseConfig();

		// Return the config or a default value.
		if (serverConfig != null) {
			return serverConfig.getSocketPort();
		} else {
			Utils.logWarning("Returned the default value for Socket port.");
			return 12345;
		}
	}

	private static void parseConfig() {
		try(Reader reader = new FileReader(PATH)) {
			Gson gson = new GsonBuilder().create();
			serverConfig = gson.fromJson(reader, ServerConfig.class);
		} catch (IOException | JsonParseException e) {
			Utils.logError("Cannot parse server-config.json.", e);
		} finally {
			triedParsing = true;
		}
	}


	class ServerConfig {
		private int waitingTimeInLobby;
		private int turnTimeLimit;
		private String host;
		private int rmiPort;
		private int socketPort;

		long getWaitingTimeInLobbyMs() {
			return waitingTimeInLobby * 1000L; // Convert seconds to milliseconds.
		}

		long getTurnTimeLimitMs() {
			return turnTimeLimit * 1000L; // Convert seconds to milliseconds.
		}

		String getHost() {
			return host;
		}

		int getRmiPort() {
			return rmiPort;
		}

		int getSocketPort() {
			return socketPort;
		}
	}
}
