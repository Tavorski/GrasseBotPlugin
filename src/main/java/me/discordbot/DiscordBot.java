package me.discordbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import me.listeners.DiscordListener;
import me.listeners.SpigotListener;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class DiscordBot {
    private  JavaPlugin mainPlugin;
    private JDA jda;
    private TextChannel discChatServerMC;
    private String initEmbedId = null;
    private Map<String,String> nicknameMap = new HashMap<>();

    public DiscordBot(JavaPlugin plugin) {
        mainPlugin = plugin;
        String botToken = mainPlugin.getConfig().getString("bot-token");

        // Inicio del bot
        System.out.println("\nInicio del bot");
        try {
            JDABuilder jdaBuilder = JDABuilder.createDefault(botToken);
            jdaBuilder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
            jda = jdaBuilder.build();
            jda.awaitReady();


        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Inicio del bot HECHO");

        // Si es nulo (no se ha iniciado como debería), se desactiva el plugin
        System.out.println("\nBot CHECK NULL");
        if (jda == null) {
            System.err.println("Fallo al iniciar MCBort");
            return;
        }
        System.out.println("Bot CHECK NULL HECHO");

        // Config.yml
        // Tomamos una referencia al canal con el chat de Discord
        System.out.println("\n} config.yml {");
        System.out.println("\n\tReferencia al chat de Discord");
        String chatChannelId = mainPlugin.getConfig().getString("chat-channel-id");
        if(chatChannelId != null) {
            discChatServerMC = jda.getTextChannelById(chatChannelId);
        }
        System.out.println("\tReferencia al chat de Discord HECHO");

        System.out.println("} config.yml");


        // Registramos manejadores de eventos
        System.out.println("\nRegistro de manejadores");
        jda.addEventListener(new DiscordListener(this));
        Bukkit.getPluginManager().registerEvents(new SpigotListener(this), mainPlugin);
        System.out.println("Registro de manejadores HECHO");


        // Mensaje de apertura
        String msg = "El servidor está ABIERTO. \u2705\u2705\u2705 ";
        //msg = msg.replace("E:","" );
        //msg = msg.replace("(0)","" );
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(
                        msg,
                        null,
                        null

                );
        builder.setColor(Color.GREEN);
        discChatServerMC.sendMessageEmbeds(builder.build()).queue();
    }
    /*public void editInitEmbed(String msg) {
        System.out.println(msg);
        if(chatServerMC==null) return;
        if(initEmbedId==null) {
            EmbedBuilder builder = new EmbedBuilder().setAuthor(msg);
            builder.setColor(Color.GREEN);
            chatServerMC.sendMessageEmbeds(builder.build()).queue(
                    (sent) -> initEmbedId = sent.getId()
            );
        }
        else {
            chatServerMC.editMessageById(initEmbedId,
                    new EmbedBuilder().setAuthor(msg).setColor(Color.GREEN).build());
        }
    }*/

    public void disableBot() {
        // Mensaje de cierre
        String msg = "El servidor se ha CERRADO. \uD83D\uDEAB\uD83D\uDEAB\uD83D\uDEAB";
        //msg = msg.replace("E:","" );
        //msg = msg.replace("(0)","" );
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(
                        msg,
                        null,
                        null
                );
        builder.setColor(Color.RED);
        discChatServerMC.sendMessageEmbeds(builder.build()).queue();
        // Orden de apagado del bot
        if(jda !=null) jda.shutdown();
    }

    public TextChannel getDiscChatServerMC(){
        return discChatServerMC;
    }
    public JavaPlugin getMainPlugin() {
        return mainPlugin;
    }
    public void sendCommand(String command) {
        Bukkit.getScheduler().callSyncMethod(mainPlugin, () -> mainPlugin.getServer().dispatchCommand( mainPlugin.getServer().getConsoleSender(), command ) );
    }
    public void sendDiscMessage(Player player, String content, boolean contentInAuthorLine, Color color) {
        if(discChatServerMC ==null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(
                        contentInAuthorLine ? content : player.getDisplayName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1"

                );

        if(!contentInAuthorLine) {
            builder.setDescription(content);
        }
        builder.setColor(color);
        discChatServerMC.sendMessageEmbeds(builder.build()).queue();
    }

}
