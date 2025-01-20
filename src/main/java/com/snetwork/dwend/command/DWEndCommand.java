package com.snetwork.dwend.command;


import com.snetwork.dwend.config.ConfigManager;
import com.snetwork.dwend.config.files.Config;
import com.snetwork.dwend.config.files.MessagesConfig;
import com.snetwork.dwend.util.HologramUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DWEndCommand extends AbstractCommand {
    private MessagesConfig messagesConfig;
    private Config config;

    public DWEndCommand() {
        super("dwend");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        ConfigManager configManager = ConfigManager.getInstance();

        messagesConfig = configManager.getMessagesConfig();
        config = configManager.getConfig();

        if (!sender.hasPermission("dwend.admin")) {
            sender.sendMessage(messagesConfig.getColorizedMessage(MessagesConfig.Message.NO_PERM));
            return;
        }
        if (args.length == 0) {
            return;
        }
        String subcommand = args[0];
        switch (subcommand) {
            case "place" -> place(sender);
            case "reload" -> reload(sender);
            case "delete" -> delete(sender);
        }
    }

    private void delete(CommandSender sender) {

        HologramUtils.deleteHologram(config.getHologramId());
        config.deleteHologram();
        sender.sendMessage("Hologram deleted.");


    }

    private void place(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }
        Location location = player.getLocation();
        HologramUtils.createHologram("dwend_hologram", location);
        player.sendMessage(messagesConfig.replaceColorizedPlaceholder(
                MessagesConfig.Message.PLACED, Map.of("%id%", "dwend_hologram")
        ));


    }

    private void reload(CommandSender sender) {
        ConfigManager configManager = ConfigManager.getInstance();
        MessagesConfig messagesConfig = configManager.getMessagesConfig();
        configManager.reloadAll();
        sender.sendMessage(messagesConfig.getColorizedMessage(MessagesConfig.Message.RELOADED));
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        ConfigManager configManager = ConfigManager.getInstance();

        messagesConfig = configManager.getMessagesConfig();
        config = configManager.getConfig();
        List<String> list = new ArrayList<>();

        if (!sender.hasPermission("dwend.admin")) return list;

        if (args.length == 1) {
            list.add("place");
            list.add("reload");
            list.add("delete");
        }

        return list;
    }

}
