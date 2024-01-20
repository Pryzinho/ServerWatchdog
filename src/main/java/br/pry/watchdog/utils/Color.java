package br.pry.watchdog.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class Color {

    public static Component translate(String source){
        return MiniMessage.miniMessage().deserialize(source);
    }

    public static List<Component> translate(List<String> source) {
        return source.stream().map(Color::translate).collect(Collectors.toList());
    }
}
