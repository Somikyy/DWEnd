package com.snetwork.dwend.config.files;


import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessagesConfig extends AbstractConfiguration {

    public MessagesConfig() {
        super("/", "messages.yml", true);
    }

    public String getColorizedMessageString(String message) {
        return MessagesUtils.colorize(message);
    }

    public List<String> getColorizedMessagesListString(List<String> messages) {
        return MessagesUtils.colorizeList(messages);
    }

    private String replacePlaceholderString(String message, Map<String, Object> placeholderReplacement) {
        String result = message;
        for (Map.Entry<String, Object> entry : placeholderReplacement.entrySet()) {
            String replace = entry.getKey();
            Object replacement = entry.getValue();
            result = result.replaceAll(replace, String.valueOf(replacement));
        }
        return result;
    }

    public String replaceColorizedPlaceholderString(String message, Map<String, Object> placeholderReplacement) {
        return MessagesUtils.colorize(replacePlaceholderString(message, placeholderReplacement));
    }

    public List<String> replaceColorizedPlaceholderListString(List<String> messages, Map<String, Object>
            placeholderReplacement) {
        List<String> result = new ArrayList<>();
        for (String msg : messages) {
            result.add(MessagesUtils.colorize(replacePlaceholderString(msg, placeholderReplacement)));
        }
        return result;
    }


    public List<String> replacePlaceholderListString(List<String> messages, Map<String, Object> placeholderReplacement) {
        List<String> result = new ArrayList<>();
        for (String msg : messages) {
            result.add(replacePlaceholderString(msg, placeholderReplacement));
        }
        return result;
    }

    public String getMessage(Message message) {
        return getYamlConfiguration().getString(message.getPath());
    }

    public String getColorizedMessage(Message message) {
        return MessagesUtils.colorize(getMessage(message));
    }

    public List<String> getMessagesList(Message message) {
        return message.isList() ? getYamlConfiguration().getStringList(message.getPath()) :
                new ArrayList<>(List.of(getMessage(message)));
    }


    @Override
    public void reload() {
        super.reload();
    }

    @Override
    public void save() {
        super.save();
    }

    public List<String> getMessagesColorizedList(Message message) {
        return MessagesUtils.colorizeList(getMessagesList(message));
    }

    private String replacePlaceholder(String message, Map<String, Object> placeholderReplacement) {
        String result = message;
        for (Map.Entry<String, Object> entry : placeholderReplacement.entrySet()) {
            String replace = entry.getKey();
            Object replacement = entry.getValue();
            result = result.replaceAll(replace, String.valueOf(replacement));
        }
        return result;
    }

    public String replaceColorizedPlaceholder(Message message, Map<String, Object> placeholderReplacement) {
        return MessagesUtils.colorize(replacePlaceholder(message, placeholderReplacement));
    }

    public List<String> replaceListColorizedPlaceholder(Message message, Map<String, Object> placeholderReplacement) {
        return MessagesUtils.colorizeList(replaceListPlaceholder(message, placeholderReplacement));
    }

    public String replacePlaceholder(Message message, Map<String, Object> placeholderReplacement) {
        return replacePlaceholder(getMessage(message), placeholderReplacement);
    }

    public List<String> replaceListPlaceholder(Message message, Map<String, Object> placeholderReplacement) {
        List<String> result = new ArrayList<>();
        for (String msg : getMessagesList(message)) {

            result.add(replacePlaceholder(msg, placeholderReplacement));
        }
        return result;
    }

    public void setMessage(Message message, String newValue) {
        getYamlConfiguration().set(message.getPath(), newValue);
    }


    public enum Message {
        NO_PERM("no-perm"),
        PLACED("placed"),
        RELOADED("reloaded"),
        HOLOGRAM_CLOSED("hologram-closed", true),
        HOLOGRAM_OPENED("hologram-opened", true),
        BLOCKED_COMMAND("blocklist-commands-message"),
        IN_CLOSED_END("player-in-closed-end"),
        OPENING_END("opening-end-message", true),
        CLOSING_END("closing-end-message", true),
        OPENED_END("opened-end-message", true);

        private final String path;
        private final boolean list;

        Message(String path, boolean list) {
            this.path = path;
            this.list = list;
        }

        Message(String path) {
            this.path = path;
            this.list = false;
        }

        public boolean isList() {
            return list;
        }

        public String getPath() {
            return path;
        }
    }

    public static class MessagesUtils {
        /**
         * @param input
         * @return Return colorized string
         */
        public static String colorize(String input) {
            if (input != null)
                return ChatColor.translateAlternateColorCodes('&', input);
            else
                return input;
        }

        /**
         * @param lore
         * @return Return colorized string list(i use this for lore)
         */
        public static List<String> colorizeList(List<String> lore) {
            return lore.stream().map(MessagesUtils::colorize).collect(Collectors.toList());
        }
    }

}
