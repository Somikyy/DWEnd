package com.snetwork.dwend.config;


import com.snetwork.dwend.DWEnd;
import com.snetwork.dwend.config.files.Config;
import com.snetwork.dwend.config.files.MessagesConfig;

public class ConfigManager {
    private static ConfigManager instance;
    private Config config;
    private MessagesConfig messagesConfig;
    private final DWEnd plugin;

    public ConfigManager(DWEnd plugin) {
        this.plugin = plugin;
        load();
    }

    public static ConfigManager init(DWEnd plugin) {
        if (instance == null) {
            instance = new ConfigManager(plugin);
        }
        return instance;
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public Config getConfig() {
        return config;
    }

    private void load() {
        config = new Config(plugin);
        config.reload();

        messagesConfig = new MessagesConfig(plugin);
        messagesConfig.reload();
    }

    public void reloadAll() {
        reloadConfig();
        reloadMessagesConfig();
    }

    public void saveAll() {
        saveConfig();
        saveMessagesConfig();
    }

    public void reloadMessagesConfig() {
        messagesConfig.reload();
    }

    public void saveMessagesConfig() {
        messagesConfig.save();
    }

    public void saveConfig() {
        config.save();
    }

    public void reloadConfig() {
        config.reload();
    }


}