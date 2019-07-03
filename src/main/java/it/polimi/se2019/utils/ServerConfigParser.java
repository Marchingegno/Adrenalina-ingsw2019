package it.polimi.se2019.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Parses the server configuration file and generates a ServerConfig class.
 *
 * @author MarcerAndrea
 */
class ServerConfigParser {

    private static final String FILE = "server-config.json";

    ServerConfig parseConfig() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + FILE)));
        try {
            Gson gson = new com.google.gson.GsonBuilder().create();
            return gson.fromJson(reader, ServerConfig.class);
        } catch (com.google.gson.JsonParseException e) {
            Utils.logError("Cannot parse server-config.json.", e);
        }
        return null;
    }
}
