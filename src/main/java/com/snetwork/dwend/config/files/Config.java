package com.snetwork.dwend.config.files;

import com.snetwork.dwend.config.model.ConfigSound;
import com.snetwork.dwend.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class Config extends AbstractConfiguration {

    private String hologramId;
    private String openDay;
    private int openHour;
    private int openMinute;
    private String closeDay;
    private int closeHour;
    private int closeMinute;
    private int knockback;
    private Location hologramLocation;

    private List<String> closingEndMessage;
    private List<String> openingEndMessage;
    private List<String> openedEndMessage;
    private List<String> blockedCommands;
    private ConfigSound playerInClosedEndSound;
    private String blockedCommandMessage;

    public Config(Plugin plugin) {
        super(plugin, "/", "config.yml", true);
        reload();
    }

    public boolean isOpen() {
        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        ZonedDateTime now = ZonedDateTime.now(moscowZone);

        DayOfWeek openDayOfWeek = DayOfWeek.valueOf(openDay.toUpperCase());
        ZonedDateTime openTime = now.with(openDayOfWeek)
                .withHour(openHour)
                .withMinute(openMinute)
                .withSecond(0)
                .withNano(0);

        DayOfWeek closeDayOfWeek = DayOfWeek.valueOf(closeDay.toUpperCase());
        ZonedDateTime closeTime = now.with(closeDayOfWeek)
                .withHour(closeHour)
                .withMinute(closeMinute)
                .withSecond(0)
                .withNano(0);

        if (closeTime.isBefore(openTime)) {
            closeTime = closeTime.plusWeeks(1);
        }

        return now.isAfter(openTime) && now.isBefore(closeTime);
    }

    @Override
    public void reload() {
        super.reload();
        YamlConfiguration cfg = getYamlConfiguration();

        this.hologramId = cfg.getString("hologram");
        this.openDay = cfg.getString("schedule.open.day");
        this.closeDay = cfg.getString("schedule.close.day");
        this.knockback = cfg.getInt("knockback");
        this.blockedCommands = cfg.getStringList("blocklist-commands");

        String openTime = cfg.getString("schedule.open.time");
        String closeTime = cfg.getString("schedule.close.time");

        if (openTime != null) parseOpenTime(openTime);
        if (closeTime != null) parseCloseTime(closeTime);

        ConfigurationSection soundSection = cfg.getConfigurationSection("player-in-closed-end-sound");
        if (soundSection != null) {
            this.playerInClosedEndSound = new ConfigSound(
                    soundSection.getString("name"),
                    (float) soundSection.getDouble("volume"),
                    (float) soundSection.getDouble("pitch")
            );
        }

        this.blockedCommandMessage = cfg.getString("blocklist-commands-message");

        if (!cfg.getString("hologram-location", "").isEmpty()) {
            this.hologramLocation = LocationUtils.stringToLocation(cfg.getString("hologram-location"));
        }
    }

    private void parseOpenTime(String timeStr) {
        String[] parts = timeStr.split(" ");
        String[] timeParts = parts[0].split(":");
        String period = parts[1];

        int hour = Integer.parseInt(timeParts[0]);
        this.openMinute = Integer.parseInt(timeParts[1]);

        if (period.equalsIgnoreCase("PM") && hour != 12) {
            hour += 12;
        } else if (period.equalsIgnoreCase("AM") && hour == 12) {
            hour = 0;
        }

        this.openHour = hour;
    }

    private void parseCloseTime(String timeStr) {
        String[] parts = timeStr.split(" ");
        String[] timeParts = parts[0].split(":");
        String period = parts[1];

        int hour = Integer.parseInt(timeParts[0]);
        this.closeMinute = Integer.parseInt(timeParts[1]);

        if (period.equalsIgnoreCase("PM") && hour != 12) {
            hour += 12;
        } else if (period.equalsIgnoreCase("AM") && hour == 12) {
            hour = 0;
        }

        this.closeHour = hour;
    }

    public int getKnockback() {
        return knockback;
    }

    public Location getHologramLocation() {
        return hologramLocation;
    }

    public void deleteHologram() {
        this.hologramId = "";
        this.hologramLocation = null;
        YamlConfiguration cfg = getYamlConfiguration();
        cfg.set("hologram", "");
        cfg.set("hologram-location", "");
        save();
    }

    public void saveHologramId(String hologramId, Location hologramLocation) {
        this.hologramId = hologramId;
        this.hologramLocation = hologramLocation;
        YamlConfiguration cfg = getYamlConfiguration();
        cfg.set("hologram", hologramId);
        cfg.set("hologram-location", LocationUtils.locationToString(hologramLocation));
        save();
    }

    public String getHologramId() {
        return hologramId;
    }


    public String getOpenDay() {
        return openDay;
    }

    public int getOpenHour() {
        return openHour;
    }

    public int getOpenMinute() {
        return openMinute;
    }

    public String getCloseDay() {
        return closeDay;
    }

    public int getCloseHour() {
        return closeHour;
    }

    public int getCloseMinute() {
        return closeMinute;
    }

    public List<String> getClosingEndMessage() {
        return closingEndMessage;
    }

    public List<String> getOpeningEndMessage() {
        return openingEndMessage;
    }

    public List<String> getOpenedEndMessage() {
        return openedEndMessage;
    }

    public List<String> getBlockedCommands() {
        return blockedCommands;
    }

    public ConfigSound getPlayerInClosedEndSound() {
        return playerInClosedEndSound;
    }

    public String getBlockedCommandMessage() {
        return blockedCommandMessage;
    }
}