package com.snetwork.dwend.config.model;

public class FurnaceLevel {
    private int level;
    private int smeltBoost;
    private int fuelBoost;
    private int expBoost;
    private int itemBoost;

    public FurnaceLevel(int level, int itemBoost, int fuelBoost, int expBoost, int smeltBoost) {
        this.level = level;
        this.itemBoost = itemBoost;
        this.fuelBoost = fuelBoost;
        this.expBoost = expBoost;
        this.smeltBoost = smeltBoost;
    }

    public int getExpBoost() {
        return expBoost;
    }

    public void setExpBoost(int expBoost) {
        this.expBoost = expBoost;
    }

    public int getFuelBoost() {
        return fuelBoost;
    }

    public void setFuelBoost(int fuelBoost) {
        this.fuelBoost = fuelBoost;
    }

    public int getItemBoost() {
        return itemBoost;
    }

    public void setItemBoost(int itemBoost) {
        this.itemBoost = itemBoost;
    }

    public int getSmeltBoost() {
        return smeltBoost;
    }

    public void setSmeltBoost(int smeltBoost) {
        this.smeltBoost = smeltBoost;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
