package me.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.discordbot.DiscordBot;
import java.awt.*;
import java.util.Locale;

public final class SpigotListener implements Listener {
    private DiscordBot bot;
    private static final char playerCommandChar = '!';
    private boolean done = false;
    public SpigotListener(DiscordBot b) {
        bot = b;
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {

        // Comando ingame
       if(event.getMessage().charAt(0) == playerCommandChar) {
            event.setCancelled(true);
            String msg_com_s = "DiscordBot: Comando desconocido \"" + event.getMessage() + "\"";
            String msg_com_d = msg_com_s;
            Color color_com = Color.MAGENTA;

            // Comando REC
            if(event.getMessage().substring(1).equalsIgnoreCase("rec")) {
                msg_com_s = String.format (Locale.ENGLISH, ChatColor.GOLD + "Coordenadas de " + event.getPlayer().getDisplayName() + ": "
                                + ChatColor.RESET
                                +"[%.0f, %.0f, %.0f]",
                        event.getPlayer().getLocation().getX(),
                        event.getPlayer().getLocation().getY(),
                        event.getPlayer().getLocation().getZ());
                msg_com_d = String.format (Locale.ENGLISH,"Coordenadas de " + event.getPlayer().getDisplayName() + ": "
                                +"[%.0f, %.0f, %.0f]",
                        event.getPlayer().getLocation().getX(),
                        event.getPlayer().getLocation().getY(),
                        event.getPlayer().getLocation().getZ());
            }
            // Emisión del comando
            Bukkit.broadcastMessage(msg_com_s);
            bot.sendDiscMessage(event.getPlayer(), msg_com_d, false, color_com);
        }
        // Chat normal
        else {
            String msg_chat = event.getMessage().toLowerCase();
            msg_chat = msg_chat.replace("maricon", "macarron");
           msg_chat = msg_chat.replace("nigger", "driller");
           msg_chat = msg_chat.replace("zorra", "gorras");
           msg_chat = msg_chat.replace("puto andres", "gora eta");
           msg_chat = msg_chat.replace("puto andre", "gora eta");
            event.setMessage(msg_chat);
            bot.sendDiscMessage(event.getPlayer(), msg_chat, false, Color.GRAY);
        }
    }
    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        bot.sendDiscMessage(event.getPlayer(), event.getPlayer().getDisplayName() + " se ha unido al server.", true, Color.YELLOW);
    }
    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        bot.sendDiscMessage(event.getPlayer(), event.getPlayer().getDisplayName() + " se ha salido del server.", true, Color.YELLOW);
    }
    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        bot.sendDiscMessage(event.getEntity(), event.getEntity().getDisplayName() + " se ha matao (" + event.getDeathMessage() + ").", true, Color.RED);
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        bot.sendDiscMessage(
                event.getPlayer(),
                event.getPlayer().getDisplayName() + " ha lanzado el comando: " + event.getMessage(),
                false, Color.MAGENTA);
    }


}
