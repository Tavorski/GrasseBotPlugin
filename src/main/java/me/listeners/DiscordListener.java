package me.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import me.discordbot.DiscordBot;

import java.awt.*;

public final class DiscordListener extends ListenerAdapter {

    private static DiscordBot bot;
    private static final char discordCommandChar = '!';
    public DiscordListener(DiscordBot b) {
        Message.suppressContentIntentWarning();
        bot = b;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.getChannel().equals(bot.getDiscChatServerMC())) return;
        Member member = event.getMember();
        if (member == null || member.getUser().isBot()) return;
        String in_msg = event.getMessage().getContentDisplay();
        System.out.println(event.getMessage().getContentRaw());
        System.out.println(event.getMessage().getContentStripped());
        System.out.println(event.getMessage().getContentDisplay());
        System.out.println(event.getMessage());
        String out_msg = "";
        Color color = Color.MAGENTA;

        // Comando para envíar al server?
        if(in_msg.length()>0 && in_msg.charAt(0) == discordCommandChar){
            boolean enviar = false;
            if(in_msg.charAt(0)!=discordCommandChar) return;
            in_msg = in_msg.substring(1);
            System.out.println("\n Recibido comando");
            // COMANDO: LIST
            if(in_msg.equals("list")) {
                System.out.println("\n Recibido list");
                out_msg = "Jugadores conectados: ";
                if(bot.getMainPlugin().getServer().getOnlinePlayers().isEmpty()) {
                    out_msg += "0";
                }
                else {
                    out_msg += bot.getMainPlugin().getServer().getOnlinePlayers().size() + " (";
                    for (Player p : bot.getMainPlugin().getServer().getOnlinePlayers()) {
                        out_msg += p.getDisplayName() + ", ";
                    }
                    out_msg = out_msg.substring(0, out_msg.length()-2);
                    out_msg += ")";
                }
                enviar = true;
            }

            // COMANDO: STOP
            if(in_msg.equals("stop")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
            }
            if(enviar) {
                EmbedBuilder builder = new EmbedBuilder().setAuthor(out_msg, null, null);
                builder.setColor(color);
                bot.getDiscChatServerMC().sendMessageEmbeds(builder.build()).queue();
            }
            return;
        }

        // Chat normal
        else {
            Bukkit.broadcastMessage((ChatColor.LIGHT_PURPLE + "<" + member.getEffectiveName() + ">"
                    + ChatColor.RESET + " " + in_msg));
        }
    }

}
