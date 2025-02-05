package com.snetwork.dwend;

import com.snetwork.dwend.command.DWEndCommand;
import com.snetwork.dwend.config.ConfigManager;
import com.snetwork.dwend.config.files.Config;
import com.snetwork.dwend.config.files.MessagesConfig;
import com.snetwork.dwend.config.model.ConfigSound;
import com.snetwork.dwend.listener.EndListener;
import com.snetwork.dwend.util.HologramUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DWEnd extends JavaPlugin {
    private static DWEnd instance;
    private MessagesConfig messages;
    private Config config;

    public static DWEnd getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.init(this);

        messages = ConfigManager.getInstance().getMessagesConfig();
        config = ConfigManager.getInstance().getConfig();

        new DWEndCommand();
        if (!config.getHologramId().isEmpty()) {
            HologramUtils.createHologram(config.getHologramId(), config.getHologramLocation());
        }

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            String hologramId = config.getHologramId();
            if (!hologramId.isEmpty()) {
                boolean isOpen = config.isOpen();
                HologramUtils.updateHologram(hologramId, isOpen);
                checkAndSendNotifications(isOpen);
            }
        }, 20L, 20L);

        Bukkit.getScheduler().runTaskTimer(this, this::checkPlayersInEnd, 20L, 20L);

        getServer().getPluginManager().registerEvents(new EndListener(), this);
        getLogger().info("DWEnd has been enabled!");
    }

    private void checkPlayersInEnd() {
        if (!config.isOpen()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
                    World spawnWorld = Bukkit.getWorlds().get(0);
                    Location spawnLoc = spawnWorld.getSpawnLocation();
                    player.teleport(spawnLoc);

                    messages.getMessagesColorizedList(MessagesConfig.Message.IN_CLOSED_END)
                            .forEach(player::sendMessage);

                    ConfigSound sound = config.getPlayerInClosedEndSound();
                    if (sound != null) {
                        player.playSound(player.getLocation(),
                                Sound.valueOf(sound.getName()),
                                sound.getVolume(),
                                sound.getPitch());
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }

    private void checkAndSendNotifications(boolean isOpen) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));

        if (!isOpen) {
            LocalDateTime openTime = getNextOpenTime();
            long minutesUntilOpen = ChronoUnit.MINUTES.between(now, openTime);

            if (minutesUntilOpen <= 15 && minutesUntilOpen >= 14) {
                messages.getMessagesColorizedList(MessagesConfig.Message.OPENING_END)
                        .forEach(Bukkit::broadcastMessage);
            }
        }
    }

    private void broadcastMessages(List<String> messages) {
        if (messages == null || messages.isEmpty()) return;
        messages.forEach(Bukkit::broadcastMessage);
    }

    private LocalDateTime getNextOpenTime() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        DayOfWeek currentDay = now.getDayOfWeek();
        DayOfWeek targetDay = DayOfWeek.valueOf(config.getOpenDay());

        LocalDateTime targetTime = now
                .withHour(config.getOpenHour())
                .withMinute(config.getOpenMinute())
                .withSecond(0)
                .withNano(0);

        // If target day is today but time has passed, go to next week
        if (currentDay == targetDay && now.isAfter(targetTime)) {
            targetTime = targetTime.plusWeeks(1);
        }

        // Move to target day
        while (targetTime.getDayOfWeek() != targetDay) {
            targetTime = targetTime.plusDays(1);
        }

        return targetTime;
    }

    private LocalDateTime getNextCloseTime() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        DayOfWeek targetDay = DayOfWeek.valueOf(config.getCloseDay());

        LocalDateTime targetTime = now
                .withHour(config.getCloseHour())
                .withMinute(config.getCloseMinute())
                .withSecond(0)
                .withNano(0);

        if (now.getDayOfWeek() == targetDay && now.isAfter(targetTime)) {
            targetTime = targetTime.plusWeeks(1);
        }

        while (targetTime.getDayOfWeek() != targetDay) {
            targetTime = targetTime.plusDays(1);
        }

        return targetTime;
    }
}