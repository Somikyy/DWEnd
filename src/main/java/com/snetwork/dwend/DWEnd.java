package com.snetwork.dwend;

import com.snetwork.dwend.command.DWEndCommand;
import com.snetwork.dwend.config.ConfigManager;
import com.snetwork.dwend.config.files.Config;
import com.snetwork.dwend.config.files.MessagesConfig;
import com.snetwork.dwend.config.model.ConfigSound;
import com.snetwork.dwend.listener.EndListener;
import com.snetwork.dwend.util.HologramUtils;
import org.bukkit.Bukkit;
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

    public static DWEnd getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.getInstance();

        new DWEndCommand();
        Config config = ConfigManager.getInstance().getConfig();
        MessagesConfig messages = ConfigManager.getInstance().getMessagesConfig();

        if (!config.getHologramId().isEmpty())
            HologramUtils.createHologram(config.getHologramId(), config.getHologramLocation());

        Bukkit.getScheduler().runTaskTimer(this,
                () -> {
                    String hologramId = config.getHologramId();
                    if (!hologramId.isEmpty()) {
                        HologramUtils.updateHologram(hologramId, config.isOpen());
                    }
                }, 20L, 20L);

        Bukkit.getScheduler().runTaskTimer(this,
                this::checkPlayersInEnd, 20L, 20L);

        getServer().getPluginManager().registerEvents(new EndListener(), this);
        getLogger().info("DWEnd has been enabled!");
    }

    private void checkPlayersInEnd() {
        Config config = ConfigManager.getInstance().getConfig();
        MessagesConfig messages = ConfigManager.getInstance().getMessagesConfig();

        if (!config.isOpen()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());

                    for (String line : messages.getMessagesColorizedList(MessagesConfig.Message.IN_CLOSED_END)) {
                        player.sendMessage(line);
                    }

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
        Config config = ConfigManager.getInstance().getConfig();
        MessagesConfig messages = ConfigManager.getInstance().getMessagesConfig();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));

        if (isOpen) {
            LocalDateTime closeTime = getNextCloseTime();
            long minutesUntilClose = ChronoUnit.MINUTES.between(now, closeTime);

            if (minutesUntilClose == 30) {
                broadcastMessage(config.getClosingEndMessage());
            }
        } else {
            LocalDateTime openTime = getNextOpenTime();
            long minutesUntilOpen = ChronoUnit.MINUTES.between(now, openTime);

            if (minutesUntilOpen == 15) {
                broadcastMessage(config.getOpeningEndMessage());
            } else if (minutesUntilOpen == 0) {
                broadcastMessage(config.getOpenedEndMessage());
            }
        }
    }

    private void broadcastMessage(List<String> messages) {
        if (messages == null || messages.isEmpty()) return;

        for (String message : messages) {
            Bukkit.broadcastMessage(MessagesConfig.MessagesUtils.colorize(message));
        }
    }

    private LocalDateTime getNextOpenTime() {
        Config config = ConfigManager.getInstance().getConfig();
        return LocalDateTime.now(ZoneId.of("Europe/Moscow"))
                .with(DayOfWeek.valueOf(config.getOpenDay()))
                .withHour(config.getOpenHour())
                .withMinute(config.getOpenMinute());
    }

    private LocalDateTime getNextCloseTime() {
        Config config = ConfigManager.getInstance().getConfig();
        return LocalDateTime.now(ZoneId.of("Europe/Moscow"))
                .with(DayOfWeek.valueOf(config.getCloseDay()))
                .withHour(config.getCloseHour())
                .withMinute(config.getCloseMinute());
    }
}