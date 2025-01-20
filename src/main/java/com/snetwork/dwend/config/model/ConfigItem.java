package com.snetwork.dwend.config.model;

import org.bukkit.Material;

import java.util.List;

public class ConfigItem {
    private String displayName;
    private List<String> lore;
    private String command;
    private Material material;
    private List<Integer> slot;

    public ConfigItem(String displayName, List<String> lore, String command, Material material, List<Integer> slot) {
        this.displayName = displayName;
        this.lore = lore;
        this.command = command;
        this.material = material;
        this.slot = slot;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public List<Integer> getSlot() {
        return slot;
    }

    public void setSlot(List<Integer> slot) {
        this.slot = slot;
    }
}