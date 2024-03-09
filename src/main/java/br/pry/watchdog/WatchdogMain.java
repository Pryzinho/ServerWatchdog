package br.pry.watchdog;

import br.pry.watchdog.commands.WatchdogCommand;
import br.pry.watchdog.listener.WatchdogListener;
import br.pry.watchdog.utils.DiscordWebhook;
import br.pry.watchdog.utils.PryConfig;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class WatchdogMain extends JavaPlugin {
    private PryConfig config;
    private DiscordWebhook dw;
    private boolean discordEnabled;

    /* To-do: ajeitar a maneira de tocar o som, pq atualemnte eu uso comando e isso aparece feiao no console*/
    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Acordando o cão de guarda");
        this.config = new PryConfig(this, "config.yml");
        config.saveDefaultConfig();
        this.discordEnabled = config.getBoolean("discord.enabled");
        if (this.discordEnabled) {
            this.dw = new DiscordWebhook(config.getString("discord.webhook"));
            getLogger().log(Level.INFO, "Conectando coleira ao Discord...");
        }
        new WatchdogListener(this);
        new WatchdogCommand(this);
        getLogger().log(Level.INFO, "Suprimindo os comandos: pl, plugins, about, ?, ...");
        getLogger().info("O cão de guarda está farejando...");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        getLogger().info("Cão de guarda foi colocado para dormir.");
    }

    public PryConfig getConfigManager() {
        return config;
    }

    public DiscordWebhook getDiscordWebhook() {
        return dw;
    }


    public void setDiscordWebhook(DiscordWebhook discordWebhook) {
        this.dw = discordWebhook;
    }

    public boolean isDiscordEnabled() {
        return discordEnabled;
    }

    public void reload() {
        config.reloadConfig();
        this.discordEnabled = config.getBoolean("discord.enabled");
        if (discordEnabled) {
            this.dw = new DiscordWebhook(config.getString("discord.webhook"));
        }
    }
}
