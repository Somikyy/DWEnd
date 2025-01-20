package com.snetwork.dwend.command;

import com.snetwork.dwend.DWEnd;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    public AbstractCommand(String command) {
        PluginCommand pluginCommand = DWEnd.getInstance().getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    List<String> completeList(String start, List<String> list) {
        return (list.stream().filter(p -> p.startsWith(start)).
                toList());

    }

    public abstract void execute(CommandSender sender, String label, String[] args);

    public abstract List<String> complete(CommandSender sender, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        execute(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return filter(complete(sender, args), args);
    }

    List<String> completePlayers(String start) {
        return (Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).filter(p -> p.startsWith(start)).
                toList());

    }

    private List<String> filter(List<String> list, String[] args) {
        if (list == null) return null;
        String last = args[args.length - 1];
        List<String> result = new ArrayList<>();
        for (String arg : list) {
            if (arg.toLowerCase().startsWith(last.toLowerCase())) result.add(arg);
        }
        return result;
    }
}
