package br.pry.watchdog;

import br.pry.watchdog.commands.WatchdogCommand;
import br.pry.watchdog.listener.WatchdogListener;
import br.pry.watchdog.utils.DiscordWebhook;
import br.pry.watchdog.utils.PryConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public final class WatchdogMain extends JavaPlugin {
    private PryConfig config;
    private DiscordWebhook dw;

    @Override
    public void onEnable() {
        this.config = new PryConfig(this, "config.yml");
        config.saveDefaultConfig();
        this.dw = new DiscordWebhook(config.getString("discord.webhook"));
        new WatchdogListener(this);
        new WatchdogCommand(this);
        getLogger().log(Level.INFO, "ServerWatchdog - Iniciado com sucesso");
        getLogger().log(Level.INFO, "Suprimindo os comandos: pl, plugins, about, ? ...");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
}
