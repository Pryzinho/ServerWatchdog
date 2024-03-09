package br.pry.watchdog.listener;

import br.pry.watchdog.WatchdogMain;
import br.pry.watchdog.utils.Color;
import br.pry.watchdog.utils.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

    public WatchdogListener(WatchdogMain instance) {
        this.instance = instance;
        this.dw = instance.getDiscordWebhook();
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    private final List<String> commands = Arrays.asList("?", "pl", "about", "version", "ver", "plugins", "bukkit:?", "bukkit:pl", "bukkit:about", "bukkit:version", "bukkit:ver", "bukkit:plugins", "minecraft:pl", "minecraft:plugins", "minecraft:about", "minecraft:version", "minecraft:ver");

    @EventHandler(priority = EventPriority.HIGH)
    public void onCommandUse(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        commands.forEach(all -> {
            String[] arrCommand = event.getMessage().toLowerCase().split(" ", 2);
            if (arrCommand[0].equalsIgnoreCase("/" + all.toLowerCase())) {
                if (!p.hasPermission("watchdog.group.developer")) {
                    event.setCancelled(true);
                    p.sendMessage(Color.translate(instance.getConfigManager().getString("messages.no-permission")));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playsoundByConfig("messages.no-permission-sound", p));
                    Bukkit.getOnlinePlayers().stream().filter(t -> t.hasPermission("watchdog.group.equipe")).forEach(t -> {
                        t.sendMessage(Color.translate("<green>O jogador <yellow>" + p.getName() + " <green>tentou usar o comando<white>:<gray> " + arrCommand[0]));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playsoundByConfig("messages.bark-sound", t));
                    });
                    if (instance.isDiscordEnabled()) {
                        dw.addEmbed(new DiscordWebhook.EmbedObject()
                                .setTitle("Novo suspeito em potencial!")
                                .setDescription("O jogador " + p.getName() + "(" + p.getAddress().getHostString() + ") tentou usar o comando: " + arrCommand[0])
                                .setColor(java.awt.Color.RED)
                        );
                        try {
                            dw.execute();
                        } catch (IOException e) {
                            instance.getLogger().log(Level.SEVERE, "Falha ao enviar um suspeito para o discord. Mandando no console e adiando envio...");
                            instance.getServer().getConsoleSender().sendMessage("O jogador " + p.getName() + " tentou usar o comando: " + arrCommand[0]);
                        }
                    }
                }
            }
        });
    }

    private String playsoundByConfig(String path, Player target) {
        String soundId = instance.getConfigManager().getString(path + ".id");
        String volume = instance.getConfigManager().getString(path + ".volume");
        String pitch = instance.getConfigManager().getString(path + ".pitch");

        int x = target.getLocation().getBlockX();
        int y = target.getLocation().getBlockY();
        int z = target.getLocation().getBlockZ();

        return String.format("playsound %s master %s %d %d %d %s %s", soundId, target.getName(), x, y, z, volume, pitch);
    }

}
