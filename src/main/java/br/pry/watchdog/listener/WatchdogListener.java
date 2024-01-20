package br.pry.watchdog.listener;

import br.pry.watchdog.WatchdogMain;
import br.pry.watchdog.utils.Color;
import br.pry.watchdog.utils.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class WatchdogListener implements Listener {
    private final WatchdogMain instance;
    private final DiscordWebhook dw;

    public WatchdogListener(WatchdogMain instance){
        this.instance = instance;
        this.dw = instance.getDiscordWebhook();
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onCommandUse(PlayerCommandPreprocessEvent event) {
        List<String> commands = Arrays.asList("?", "pl", "about", "version", "ver", "plugins", "bukkit:?", "bukkit:pl", "bukkit:about", "bukkit:version", "bukkit:ver", "bukkit:plugins", "minecraft:pl", "minecraft:plugins", "minecraft:about", "minecraft:version", "minecraft:ver");
        commands.forEach(all -> {
            String[] arrCommand = event.getMessage().toLowerCase().split(" ", 2);
            if (arrCommand[0].equalsIgnoreCase("/" + all.toLowerCase())) {
                if (!event.getPlayer().hasPermission("watchdog.group.developer")) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Color.translate(instance.getConfigManager().getString("messages.no-permission")));
                    event.getPlayer().playSound(event.getPlayer(), Sound.valueOf(instance.getConfigManager().getString("messages.no-permission-sound")), 1f, 1f);
                    Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("watchdog.group.equipe")).forEach(t -> {
                        t.sendMessage(Color.translate("<green>O jogador <yellow>" + event.getPlayer().getName() + " <green>tentou usar o comando<white>:<gray> " + arrCommand[0]));
                        t.playSound(t, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    });
                    dw.addEmbed(new DiscordWebhook.EmbedObject()
                            .setTitle("Novo suspeito em potencial!")
                            .setDescription("O jogador " + event.getPlayer().getName() + "(" + event.getPlayer().getAddress() + ") tentou usar o comando: " + arrCommand[0])
                            .setColor(java.awt.Color.RED)
                    );
                    try {
                        dw.execute();
                    } catch (IOException e) {
                        instance.getLogger().log(Level.SEVERE, "Falha ao enviar um suspeito para o discord. Registrando em log.");
                        instance.getServer().getConsoleSender().sendMessage("O jogador " + event.getPlayer().getName() + " tentou usar o comando: " + arrCommand[0]);
                    }
                }
            }
        });
    }
}
