package br.pry.watchdog.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PryConfig {
    public PryConfig(JavaPlugin plugin, String nome) {
        this.plugin = plugin;
        setName(nome);
        reloadConfig();
    }
   
    private JavaPlugin plugin;
    private String name;
    private File file;
    public JavaPlugin getPlugin() {
        return plugin;
    }
    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public File getFile() {
        return file;
    }
    public  YamlConfiguration getConfig() {
        return config;
    }
    private YamlConfiguration config;
    public void saveConfig() {
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveDefault() {
        getConfig().options().copyDefaults(true);
    }
    public void saveDefaultConfig() {
    	if(existeConfig()) {
    	saveConfig();	
    	} else {
        getPlugin().saveResource(getName(), false);
    	}
    }
    public Set<String> getSection(String path){
        ConfigurationSection section = getConfig().getConfigurationSection(path);
        if (section != null) return section.getKeys(false);
        return new HashSet<>();
    }
    public ConfigurationSection getConfigurationSection(String path){
        return getConfig().getConfigurationSection(path);
    }

    public ConfigurationSection createSection(String path){
        return getConfig().createSection(path);
    }
    public void reloadConfig() {
        file = new File(getPlugin().getDataFolder(),getName());
        config = YamlConfiguration.loadConfiguration(getFile());
       
    }
    public void deleteConfig() {
        getFile().delete();
    }
    public boolean existeConfig() {
        return getFile().exists();
    }
   
    public String getString(String path) {
        return getConfig().getString(path);
    }
   
    public int getInt(String path) {
        return getConfig().getInt(path);
    }
    public long getLong(String path){
        return getConfig().getLong(path);
    }
    public boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }
   
    public double getDouble(String path) {
        return getConfig().getDouble(path);
    }
   
    public List<?> getList(String path){
        return getConfig().getList(path);
    }
    public boolean contains(String path) {
        return getConfig().contains(path);
    }
	public void setLocation(String path, Location location) {
        getConfig().set(path + ".world", location.getWorld().getName());
        getConfig().set(path + ".x", location.getX());
        getConfig().set(path + ".y", location.getY());
        getConfig().set(path + ".z", location.getZ());
        saveConfig();
    }

    public Location getLocation(String path) {
        World world = Bukkit.getWorld(getConfig().getString(path + ".world"));
        double x = getConfig().getDouble(path + ".x");
        double y = getConfig().getDouble(path + ".y");
        double z = getConfig().getDouble(path + ".z");
        return new Location(world, x, y, z);
    }
   
    public void set(String path, Object value) {
        getConfig().set(path, value);
    }


}