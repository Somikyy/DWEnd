package com.snetwork.dwend.config.model;

import java.util.List;
import java.util.Map;

public class ConfigFurnaceType {
    private String typeName;
    private int customModelData;
    private String name;
    private List<String> lore;
    private int maxLevel;
    private int defaultLevel;
    private Map<Integer, FurnaceLevel> levels;

    public ConfigFurnaceType(int customModelData, int defaultLevel, Map<Integer, FurnaceLevel> levels,
                             List<String> lore, int maxLevel, String name, String typeName) {
        this.customModelData = customModelData;
        this.defaultLevel = defaultLevel;
        this.levels = levels;
        this.lore = lore;
        this.maxLevel = maxLevel;
        this.name = name;
        this.typeName = typeName;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public int getDefaultLevel() {
        return defaultLevel;
    }

    public void setDefaultLevel(int defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    public Map<Integer, FurnaceLevel> getLevels() {
        return levels;
    }

    public void setLevels(Map<Integer, FurnaceLevel> levels) {
        this.levels = levels;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
