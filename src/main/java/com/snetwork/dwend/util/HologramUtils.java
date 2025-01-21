package com.snetwork.dwend.util;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import com.snetwork.dwend.config.ConfigManager;
import com.snetwork.dwend.config.files.Config;
import com.snetwork.dwend.config.files.MessagesConfig;
import org.bukkit.Location;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramUtils {
    private static final Map<String, Hologram> holograms = new HashMap<>();

    public static void createHologram(String hologramId, Location location) {
        Hologram existingHologram = DHAPI.getHologram(hologramId);
        if (existingHologram != null) {
            existingHologram.delete();
        }

        if (holograms.containsKey(hologramId)) {
            holograms.remove(hologramId);
        }

        Hologram hologram = DHAPI.createHologram(hologramId, location);
        hologram.setDisplayRange(64);
        holograms.put(hologramId, hologram);

        ConfigManager.getInstance().getConfig().saveHologramId(hologramId, location);
    }

    public static void deleteHologram(String hologramId) {
        Hologram hologram = holograms.get(hologramId);
        if (hologram != null) {
            hologram.delete();
            holograms.remove(hologramId);
            return;
        }

        hologram = DHAPI.getHologram(hologramId);
        if (hologram != null) {
            hologram.delete();
        }
    }

    public static void updateHologram(String hologramId, boolean isOpen) {
        Hologram hologram = holograms.get(hologramId);
        if (hologram == null) {
            return;
        }

        List<String> lines;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        LocalDateTime targetTime = getNextTargetTime(isOpen);

        long secondsUntilTarget = ChronoUnit.SECONDS.between(now, targetTime);
        long days = secondsUntilTarget / (24 * 3600);
        secondsUntilTarget %= 24 * 3600;
        long hours = secondsUntilTarget / 3600;
        secondsUntilTarget %= 3600;
        long minutes = secondsUntilTarget / 60;
        if (secondsUntilTarget != 0) {
            secondsUntilTarget %= 60;
        }
        MessagesConfig messagesConfig = ConfigManager.getInstance().getMessagesConfig();
        if (isOpen) {
            lines = messagesConfig.replaceColorizedPlaceholderListString(messagesConfig.getMessagesColorizedList(
                    MessagesConfig.Message.HOLOGRAM_OPENED), Map.of("%days%", days,
                    "%hours%", hours, "%minutes%", minutes, "%seconds%", secondsUntilTarget)
            );
        } else {
            lines = messagesConfig.replaceColorizedPlaceholderListString(messagesConfig.getMessagesColorizedList(
                    MessagesConfig.Message.HOLOGRAM_CLOSED), Map.of("%days%", days,
                    "%hours%", hours, "%minutes%", minutes, "%seconds%", secondsUntilTarget)
            );
        }

        DHAPI.setHologramLines(hologram, lines);
    }


    private static LocalDateTime getNextTargetTime(boolean isOpen) {
        Config config = ConfigManager.getInstance().getConfig();
        DayOfWeek targetDay = isOpen ? DayOfWeek.valueOf(config.getCloseDay()) : DayOfWeek.valueOf(config.getOpenDay());
        int targetHour = isOpen ? config.getCloseHour() : config.getOpenHour();
        int targetMinute = isOpen ? config.getCloseMinute() : config.getOpenMinute();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        LocalDateTime targetDateTime = now.with(DayOfWeek.from(targetDay))
                .with(LocalTime.of(targetHour, targetMinute));

        if (targetDateTime.isBefore(now)) {
            targetDateTime = targetDateTime.plusWeeks(1);
        }
        return targetDateTime;
    }


}
