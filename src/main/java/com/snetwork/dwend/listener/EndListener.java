package com.snetwork.dwend.listener;

import com.snetwork.dwend.config.ConfigManager;
import com.snetwork.dwend.config.files.Config;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class EndListener implements Listener {


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
