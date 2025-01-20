package com.snetwork.dwend;

import com.snetwork.dwend.command.DWEndCommand;
import com.snetwork.dwend.config.ConfigManager;
import com.snetwork.dwend.config.files.Config;
import com.snetwork.dwend.listener.EndListener;
import com.snetwork.dwend.util.HologramUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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

        if (!config.getHologramId().isEmpty())
            HologramUtils.createHologram(config.getHologramId(), config.getHologramLocation());

        Bukkit.getScheduler().runTaskTimer(this,
                () -> {
                    String hologramId = config.getHologramId();
                    if (!hologramId.isEmpty()) {
                        HologramUtils.updateHologram(hologramId, config.isOpen());
                    }
                }, 20, 20);
        getServer().getPluginManager().registerEvents(new EndListener(), this);
        getLogger().info("DWEnd has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }
}
