package com.snetwork.dwend.config.files;

import com.snetwork.dwend.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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

    public Config() {
        super("/", "config.yml", true);
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
        this.openHour = cfg.getInt("schedule.open.hour");
        this.openMinute = cfg.getInt("schedule.open.minute");
        this.closeDay = cfg.getString("schedule.close.day");
        this.closeHour = cfg.getInt("schedule.close.hour");
        this.closeMinute = cfg.getInt("schedule.close.minute");
        this.knockback = cfg.getInt("knockback");
        if (!cfg.getString("hologram-location").isEmpty())
            this.hologramLocation = LocationUtils.stringToLocation(cfg.getString("hologram-location"));
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
}