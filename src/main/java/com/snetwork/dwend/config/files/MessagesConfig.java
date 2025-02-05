package com.snetwork.dwend.config.files;

import com.snetwork.dwend.util.ColorUtils;
import org.bukkit.plugin.Plugin;
import java.util.*;
import java.util.stream.Collectors;

public class MessagesConfig extends AbstractConfiguration {

    public MessagesConfig(Plugin plugin) {
        super(plugin, "/", "messages.yml", true);
    }

    public List<String> getMessagesColorizedList(Message message) {
        return getMessagesList(message).stream()
                .map(ColorUtils::colorize)
                .collect(Collectors.toList());
    }

    public String getMessage(Message message) {
        return getYamlConfiguration().getString(message.getPath(), "");
    }

    public String getColorizedMessage(Message message) {
        return ColorUtils.colorize(getMessage(message));
    }

    public List<String> getMessagesList(Message message) {
        if (!message.isList()) {
            return Collections.singletonList(getMessage(message));
        }
        return getYamlConfiguration().getStringList(message.getPath());
    }

    public String replaceColorizedPlaceholder(Message message, Map<String, Object> placeholderReplacement) {
        return ColorUtils.colorize(replacePlaceholder(message, placeholderReplacement));
    }

    public String replacePlaceholder(Message message, Map<String, Object> placeholderReplacement) {
        return replacePlaceholder(getMessage(message), placeholderReplacement);
    }

    private String replacePlaceholder(String message, Map<String, Object> placeholderReplacement) {
        String result = message;
        for (Map.Entry<String, Object> entry : placeholderReplacement.entrySet()) {
            result = result.replace(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return result;
    }

    public List<String> replaceColorizedPlaceholderListString(List<String> messages, Map<String, Object> placeholderReplacement) {
        List<String> result = new ArrayList<>();
        for (String msg : messages) {
            result.add(ColorUtils.colorize(replacePlaceholder(msg, placeholderReplacement)));
        }
        return result;
    }

    public enum Message {
        NO_PERM("no-perm"),
        PLACED("placed"),
        RELOADED("reloaded"),
        HOLOGRAM_CLOSED("hologram-closed", true),
        HOLOGRAM_OPENED("hologram-opened", true),
        BLOCKED_COMMAND("blocklist-commands-message", true),
        IN_CLOSED_END("player-in-closed-end", true),
        OPENING_END("opening-end-message", true),
        CLOSING_END("closing-end-message", true),
        OPENED_END("opened-end-message", true);

        private final String path;
        private final boolean list;

        Message(String path) {
            this.path = path;
            this.list = false;
        }

        Message(String path, boolean list) {
            this.path = path;
            this.list = list;
        }

        public String getPath() {
            return path;
        }

        public boolean isList() {
            return list;
        }
    }

    @Override
    public void reload() {
        super.reload();
    }

    @Override
    public void save() {
        super.save();
    }
}