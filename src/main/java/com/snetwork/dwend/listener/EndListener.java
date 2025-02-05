package com.snetwork.dwend.listener;

import com.snetwork.dwend.config.ConfigManager;
import com.snetwork.dwend.config.files.Config;
import com.snetwork.dwend.config.files.MessagesConfig;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class EndListener implements Listener {
    private final ConfigManager configManager = ConfigManager.getInstance();
    private final Config config = configManager.getConfig();
    private final MessagesConfig messages = configManager.getMessagesConfig();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getEnvironment() != World.Environment.THE_END) {
            return;
        }

        String command = event.getMessage().split(" ")[0].toLowerCase();
        if (config.getBlockedCommands().contains(command)) {
            event.setCancelled(true);
            messages.getMessagesColorizedList(MessagesConfig.Message.BLOCKED_COMMAND)
                    .forEach(player::sendMessage);
        }
    }

    @EventHandler
    private void onEnd(PlayerTeleportEvent event) {
        if (event.getTo().getWorld().getEnvironment() != World.Environment.THE_END) {
            return;
        }
        Player player = event.getPlayer();
        Config config = ConfigManager.getInstance().getConfig();
        if (config.isOpen()) {
            return;
        }
        event.setCancelled(true);

        Vector direction = player.getLocation().getDirection().normalize();
        Vector knockback = direction.multiply(-1).multiply(config.getKnockback());
        knockback.setY(config.getKnockback());
        player.setVelocity(knockback);
    }
}
