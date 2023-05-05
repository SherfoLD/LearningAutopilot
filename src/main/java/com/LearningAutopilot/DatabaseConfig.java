package com.LearningAutopilot;

import com.LearningAutopilot.Exceptions.InvalidConfigException;
import com.LearningAutopilot.UI.UiConsts;
import lombok.Getter;

import java.util.prefs.Preferences;

public class DatabaseConfig {
    private static DatabaseConfig databaseConfig;
    private final Preferences DatabaseConfigOnDisk = Preferences.userRoot().node(UiConsts.APP_NAME);
    @Getter
    private String databaseHostName;
    @Getter
    private int databasePort;
    @Getter
    private String databaseName;

    public static DatabaseConfig getInstance() {
        if (databaseConfig == null) {
            databaseConfig = new DatabaseConfig();
        }
        return databaseConfig;
    }

    private DatabaseConfig() {

    }

    public void setConfig(String databaseHostName, int databasePort, String databaseName) {
        this.databaseHostName = databaseHostName;
        this.databasePort = databasePort;
        this.databaseName = databaseName;

        saveConfig();
    }

    public void restoreConfig() throws InvalidConfigException {
        databaseHostName = DatabaseConfigOnDisk.get("databaseHostName", "");
        databaseName = DatabaseConfigOnDisk.get("databaseName", "");
        try {
            databasePort = DatabaseConfigOnDisk.getInt("databasePort", 0);
        } catch (IllegalArgumentException e) {
            throw new InvalidConfigException("Saved database port was in incorrect format", e);
        }

        if (databaseHostName.equals("") || databaseName.equals(""))
            throw new InvalidConfigException("Saved database info was empty");
    }

    private void saveConfig() {
        DatabaseConfigOnDisk.put("databaseHostName", databaseHostName);
        DatabaseConfigOnDisk.putInt("databasePort", databasePort);
        DatabaseConfigOnDisk.put("databaseName", databaseName);
    }
}
