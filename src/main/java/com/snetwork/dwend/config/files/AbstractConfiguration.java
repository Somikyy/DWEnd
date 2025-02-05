package com.snetwork.dwend.config.files;


import com.snetwork.dwend.DWEnd;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Objects;

public abstract class AbstractConfiguration {
    private final String path;
    private final String pathToFolder;
    private File file;
    private YamlConfiguration configuration;
    protected final  Plugin plugin;

    public AbstractConfiguration(Plugin plugin, final String path, final String filename, boolean copy) {
        this.plugin = plugin;
        this.pathToFolder = Objects.requireNonNull(plugin).getDataFolder() + path;
        this.path = pathToFolder + filename;
        setFile();
        if (!file.exists()) {
            initializeFile();
            if (copy)
                copyFromResource(Objects.requireNonNull(plugin.getResource(filename)), file);
        }
        setYamlConfiguration();
    }

    public String getPathToFolder() {
        return pathToFolder;
    }


    public FileConfiguration loadFromInternal(String resourceName) {

        File tempFile = null;
        try (InputStream input = getClass().getResourceAsStream("/" + resourceName)) {
            tempFile = File.createTempFile(resourceName.replace(".yml", ""), ".yml");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {

                assert input != null;

                byte[] buffer = new byte[8192];
                int n;
                while (-1 != (n = input.read(buffer))) {
                    out.write(buffer, 0, n);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        File messageYML = tempFile;
        return YamlConfiguration.loadConfiguration(messageYML);
    }

    /**
     * Create external file
     */
    private void initializeFile() {

        if (!file.exists())
            file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param resource
     * @param target   copy chars(bytes) from resource(InputStream) to target(File).
     *                 <p>
     *                 i use it for copy internal jar files and past it to a certain
     *                 directory
     */
    private void copyFromResource(InputStream resource, File target) {
        byte[] buffer;
        try {
            buffer = new byte[resource.available()];
            resource.read(buffer);
            OutputStream outStream = new FileOutputStream(target);
            outStream.write(buffer);
            outStream.flush();
            outStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path initialize file by path
     */
    public void initializeFile(String path) {
        File file = new File(path);
        if (!file.exists())
            file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            getYamlConfiguration().save(getFile());
            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setFile() {
        this.file = new File(path);
    }

    public File getFile() {
        return file;
    }

    private void setYamlConfiguration() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getYamlConfiguration() {
        return configuration;
    }


    public void reload() {
        this.setYamlConfiguration();
    }
}