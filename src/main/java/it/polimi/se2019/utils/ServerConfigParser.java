package it.polimi.se2019.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerConfigParser {

	private static final String FILE = "server-config.json";
	private static boolean triedParsing = false;
	private static ServerConfig serverConfig;

	public long getWaitingTimeInLobbyMs() {
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

	public long getTurnTimeLimitMs() {
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

	public String getHost() {
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

	public int getRmiPort() {
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

	public int getSocketPort() {
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

	private void parseConfig() {
		InputStream in = getClass().getResourceAsStream("/" + FILE);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			Gson gson = new com.google.gson.GsonBuilder().create();
			serverConfig = gson.fromJson(reader, ServerConfig.class);
		} catch (com.google.gson.JsonParseException e) {
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
