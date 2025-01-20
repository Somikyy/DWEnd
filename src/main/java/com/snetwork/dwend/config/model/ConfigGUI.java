package com.snetwork.dwend.config.model;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigGUI {
    private String title;
    private int size;
    private Map<String, ConfigItem> configItems;

    public ConfigGUI(String title, int size) {
        this.title = title;
        this.size = size;
        this.configItems = new HashMap<>();
    }


    public void loadFromConfig(YamlConfiguration config, String section) {
        ConfigurationSection slotsSection = config.getConfigurationSection(section);
        if (slotsSection != null) {
            for (String slotName : slotsSection.getKeys(false)) {
                ConfigurationSection slotSection = slotsSection.getConfigurationSection(slotName);
                if (slotSection != null) {
                    List<Integer> slot = slotSection.getIntegerList("slot");
                    String displayName = slotSection.getString("name");
                    List<String> lore = slotSection.getStringList("lore");
                    String command = slotSection.getString("command");
                    String materialString = slotSection.getString("material");
                    Material material = ((materialString == null || materialString.isEmpty()) ? null : Material.valueOf(materialString.toUpperCase()));
                    configItems.put(slotName, new ConfigItem(displayName, lore, command, material, slot));
                }
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Map<String, ConfigItem> getConfigItems() {
        return configItems;
    }

    public void setConfigItems(Map<String, ConfigItem> configItems) {
        this.configItems = configItems;
    }
}