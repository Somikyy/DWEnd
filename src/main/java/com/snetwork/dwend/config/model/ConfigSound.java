package com.snetwork.dwend.config.model;

public class ConfigSound {
    private String name;
    private float volume;
    private float pitch;

    public ConfigSound(String name, float volume, float pitch) {
        this.name = name;
        this.volume = volume;
        this.pitch = pitch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}