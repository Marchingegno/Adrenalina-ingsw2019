package it.polimi.se2019.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ServerConfigParser {

	private static final String path = System.getProperty("user.dir") + "/src/resources/server-config.json";
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
			Utils.logInfo("Returned the default value for waiting time in lobby.");
			return 5000;
		}
	}

	public static long getMoveTimeLimitMs() {
		// Parse the file if not already parsed.
		if(!triedParsing)
			parseConfig();

		// Return the config or a default value.
		if(serverConfig != null) {
			return serverConfig.getMoveTimeLimitMs();
		} else {
			Utils.logInfo("Returned the default value for move time limit.");
			return 20000;
		}
	}

	private static void parseConfig() {
		try(Reader reader = new FileReader(path)) {
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
		private int moveTimeLimit;

		long getWaitingTimeInLobbyMs() {
			return waitingTimeInLobby * 1000L; // Convert seconds to milliseconds.
		}

		long getMoveTimeLimitMs() {
			return moveTimeLimit * 1000L; // Convert seconds to milliseconds.
		}
	}
}
