package org.example.httpserver.config;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.httpserver.util.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigManager {

    private static ConfigManager myConfigManager;
    private static Config myCurrentConfig;

    public ConfigManager() {
    }

    public static ConfigManager getInstance() {
        if(myConfigManager == null) {
            myConfigManager = new ConfigManager();
        }
        return myConfigManager;
    }

    //used to load the configuration
    public void loadConfigFile(String filePath) throws IOException {
        FileReader fR = null;

        try {
            fR = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigException(e);
        }

        StringBuilder sb = new StringBuilder();
        int i;

        try {
            while((i = fR.read()) != -1) {
                sb.append((char)i);
            }
        } catch(IOException e) {
            throw new HttpConfigException(e);
        }

        JsonNode conf = null;

        try {
            conf = Json.parse(sb.toString());
        } catch (IOException e) {
            throw new HttpConfigException("Error parsing failed", e);
        }

        try {
            myCurrentConfig = Json.fromJson(conf, Config.class);
        } catch (IOException e) {
            throw new HttpConfigException("Error parsing failed", e);
        }
    }

    //returns the current loaded configuration
    public Config getCurrentConfig() {
        if(myCurrentConfig == null) {
            throw new HttpConfigException("No current config set.");
        }

        return myCurrentConfig;
    }
}
