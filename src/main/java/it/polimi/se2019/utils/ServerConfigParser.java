package it.polimi.se2019.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Parses the server configuration file and generates a ServerConfig class.
 *
 * @author MarcerAndrea
 */
class ServerConfigParser {

    private static final String FILE = "server-config.json";

    ServerConfig parseConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath() + "/server-config.json"))) {
            Gson gson = new com.google.gson.GsonBuilder().create();
            return gson.fromJson(reader, ServerConfig.class);
        } catch (com.google.gson.JsonParseException | URISyntaxException | IOException e) {
            Utils.logError("Cannot parse server-config.json.", e);
        }
        return null;
    }
}
