package br.pry.watchdog.commands;

import br.pry.watchdog.WatchdogMain;
import br.pry.watchdog.utils.Color;
import br.pry.watchdog.utils.DiscordWebhook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class WatchdogCommand implements CommandExecutor {
    private final WatchdogMain instance;

    public WatchdogCommand(WatchdogMain instance) {
        this.instance = instance;
        Objects.requireNonNull(instance.getCommand("swatchdog")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command c, @NotNull String s, @NotNull String[] args) {
        if (!sender.hasPermission("watchdog.group.developer")) {
            sender.sendMessage(Color.translate(instance.getConfigManager().getString("messages.no-permission")));
            return true;
        }
        @Nullable DiscordWebhook dw = instance.getDiscordWebhook();
        if (args.length != 1) {
            sendCommandUsage(sender);
            return true;
        }
        if (args[0].equalsIgnoreCase("debug")) {
            if (!instance.isDiscordEnabled()) {
                sender.sendMessage(Color.translate("<red>A coleira do cão de guarda não está conectada ao Discord, verifique a configuração."));
                return true;
            }
            dw.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Serviço veterinário")
                    .setDescription(sender.getName() + " usou o comando de debug do Watchdog.")
                    .setColor(java.awt.Color.GREEN)
            );
            try {
                dw.execute();
                sender.sendMessage(Color.translate("<green>Debugado com sucesso, verifique o Discord."));
            } catch (IOException e) {
                sender.sendMessage(Color.translate("<red>Ocorreu um erro ao utilizar o debug. (IoException)"));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            instance.reload();
            sender.sendMessage("<aqua>Todas as configurações foram recarregadas.");
            return true;
        }
        sendCommandUsage(sender);
        return true;
    }

    private void sendCommandUsage(CommandSender s) {
        s.sendMessage(" ");
        s.sendMessage(Color.translate("<gray>/swatchdog debug<white> - <green>Testa a conexão com o Webhook."));
        s.sendMessage(Color.translate("<gray>/swatchdog reload<white> - <green>Recarrega as configurações."));
        s.sendMessage(" ");
    }
}
