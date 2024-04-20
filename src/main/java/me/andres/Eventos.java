package me.andres;

import me.util.Database;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Eventos implements Listener {

    private static Map<String,Boolean> advertidos;
    private static Map<String, ItemStack> deathNotes;
    private Plugin mainPlugin;

    // CONSTRUCTOR
    public Eventos(Plugin p) {
        mainPlugin = p;
        advertidos = new HashMap<>();
        deathNotes = new HashMap<>();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if(block.getType() == Material.TNT || block.getType() == Material.TNT_MINECART ) {
            Bukkit.getLogger().info("\n\n" + player.getDisplayName() + " HA COLOCADO DINAMITA EN ["
                    + block.getLocation().getX() + ", " + block.getLocation().getY() + ", " + block.getLocation().getZ() + "]\n\n");
            Database.insertTNT(mainPlugin.getDB(),player.getDisplayName(), Date.valueOf(LocalDate.now()));
        }
        if(block.getType() == Material.LEVER ) {
            Bukkit.getLogger().info("\n\n" + player.getDisplayName() + " HA COLOCADO UNA PALANCA EN ["
                    + block.getLocation().getX() + ", " + block.getLocation().getY() + ", " + block.getLocation().getZ() + "]\n\n");
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        String coordenadas, mundo;
        ItemStack libro;
        BookMeta bm;

        libro = new ItemStack(Material.WRITTEN_BOOK, 1);
        bm = (BookMeta) libro.getItemMeta();
        bm.setDisplayName("Muerte de "
                + event.getEntity().getDisplayName()
                + " número "
                + event.getEntity().getStatistic(Statistic.DEATHS));
        bm.setTitle("Te has muerto");
        bm.setAuthor("Trufa");
        coordenadas = String.format("[%.0f, %.0f, %.0f].\n",
                event.getEntity().getLocation().getX(),
                event.getEntity().getLocation().getY(),
                event.getEntity().getLocation().getZ());
        mundo = event.getEntity().getLocation().getWorld().getName();
        switch (mundo){
            case "world":
                mundo = "Superficie";
                break;
            case "nether":
                mundo = "Nether";
                break;
            case "end":
                mundo = "End";
                break;
            default:
                mundo = "Backrooms?";
        }
        if(event.getEntity().getKiller()!=null){    // Hubo un asesino
            String asesino = event.getEntity().getKiller().getDisplayName();
            if(event.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta()!=null) {  // El asesino tenía un arma
                String arma = event.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                bm.addPage("En concreto, fuiste un bobo  aquí:\n" + mundo + ": " + coordenadas + "\n" +
                        "Te mató " + asesino + " con " + arma + " a las " + LocalTime.now() + " del " + LocalDate.now());
            }
            else    // Asesino a puños
                bm.addPage("En concreto, fuiste un bobo  aquí:\n"+mundo+": "+coordenadas+"\n" +
                        "Te mató "+asesino+" a hostias a las "+ LocalTime.now()+" del "+LocalDate.now());
        }
        else    // No hubo asesino, muerte por idiocia
            bm.addPage("En concreto, fuiste un bobo  aquí:\n"+mundo+": "+coordenadas+"\n" +
                    "Te mataste a las "+ LocalTime.now()+" del "+LocalDate.now());
        /*
        bm.addPage(String.format(Locale.ENGLISH,
                "En concreto, fuiste un bobo  aquí: "
                        + event.getEntity().getLocation().getWorld().getName()
                        + "[%.0f, %.0f, %.0f].\n"
                        + "Te mató "
                        + (event.getEntity().getKiller()!=null ?
                        (event.getEntity().getKiller().getDisplayName()
                                + (event.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta()!=null ?
                                (" con " + event.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName())
                                : " por tonto."))
                        : "tu idiocia (" + event.getDeathMessage() + ")."),
                event.getEntity().getLocation().getX(),
                event.getEntity().getLocation().getY(),
                event.getEntity().getLocation().getZ()));
                */


        libro.setItemMeta(bm);
        deathNotes.put(event.getEntity().getPlayer().getDisplayName(),libro);
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        event.getPlayer().getInventory().addItem(
                deathNotes.get(
                        event.getPlayer().getDisplayName()
                )
        );
    }
    @EventHandler
    public void onXP(PlayerExpChangeEvent event) {
        if(event.getPlayer().getDisplayName().equals("Tavorski")) {
            event.setAmount(event.getAmount()*2);
        }
    }
    @EventHandler
    public void onGoingBed(PlayerBedEnterEvent event) {
        if(event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) return;

        Player player = event.getPlayer();
        Random gen = new Random();
        String msg = null;
        switch(gen.nextInt(3000)) {

            case 1:
                msg = "w " + player.getDisplayName() + "se nos acaba lo bueno se nos " +
                        "acaba \"el chollo\" wasat va a desaparecer y " +
                        "nos van a ofrecer un nuevo Aplication : wassap 2 " +
                        "costara 8,30$ te cobraran 0,80 dollar por cada mensaje que Tú envias." +
                        " No es una broma reenvíalo para que puedas seguir utilizando wasa";
                break;

        }
        if(msg==null) return;
        String finalMsg = msg;
        Bukkit.getScheduler().callSyncMethod(mainPlugin, () -> mainPlugin.getServer().dispatchCommand( mainPlugin.getServer().getConsoleSender(), finalMsg) );
    }

}
