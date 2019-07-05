package it.polimi.se2019.utils;

import com.google.gson.Gson;

import java.io.*;
import java.net.URISyntaxException;

/**
 * Parses the server configuration file and generates a ServerConfig class.
 *
 * @author MarcerAndrea
 */
class ServerConfigParser {

	private static final String FILE = "/server-config.json";

	ServerConfig parseConfig() {
		File file;
		try {
			file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			Utils.logError("Cannot load server-config.json", e);
			file = new File("");
		}

		BufferedReader reader;
		String path = file.getParentFile().getPath() + FILE;

		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			Utils.logError("Failed to load server-config.json from absolute path", e);
			Utils.logInfo("Loading server-config.json file from within the jar file");
			reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(FILE)));
		}

		try {
			Gson gson = new com.google.gson.GsonBuilder().create();
			return gson.fromJson(reader, ServerConfig.class);
		} catch (com.google.gson.JsonParseException e) {
			Utils.logError("Cannot parse server-config.json.", e);
		}
		return null;
	}
}
