package br.pry.watchdog.commands;

import br.pry.watchdog.WatchdogMain;
import br.pry.watchdog.utils.Color;
import br.pry.watchdog.utils.DiscordWebhook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class WatchdogCommand implements CommandExecutor {
    private WatchdogMain instance;

    public WatchdogCommand(WatchdogMain instance) {
        this.instance = instance;
        instance.getCommand("swatchdog").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command c, @NotNull String s, @NotNull String[] args) {
        if (!sender.hasPermission("watchdog.group.developer")) {
            sender.sendMessage(Color.translate(instance.getConfigManager().getString("messages.no-permission")));
            return true;
        }
        if (args.length != 1) {
            sendCommandUsage(sender);
            return true;
        }
        DiscordWebhook dw = instance.getDiscordWebhook();
        if (args[0].equalsIgnoreCase("debug")) {
            dw.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Serviço veterinário")
                    .setDescription(sender.getName() + " usou o comando de debug do Watchdog.")
                    .setColor(java.awt.Color.GREEN)
            );
            try {
                dw.execute();
                sender.sendMessage("Debugado com sucesso, verifique o Discord.");
            } catch (IOException e) {
                sender.sendMessage(Color.translate("&cOcorreu um erro ao utilizar o debug. (IoException)"));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            instance.reloadConfig();
            instance.setDiscordWebhook(new DiscordWebhook(instance.getConfigManager().getString("discord.webhook")));
            return true;
        }
        sendCommandUsage(sender);
        return true;
    }

    private void sendCommandUsage(CommandSender s) {
        s.sendMessage(" ");
        s.sendMessage("/swatchdog debug - Testa a conexão com o Webhook.");
        s.sendMessage("/swatchdog reload - Recarrega as configurações.");
    }
}
