package me.andres;

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
        }
        if(block.getType() == Material.LEVER ) {
            Bukkit.getLogger().info("\n\n" + player.getDisplayName() + " HA COLOCADO UNA PALANCA EN EN ["
                    + block.getLocation().getX() + ", " + block.getLocation().getY() + ", " + block.getLocation().getZ() + "]\n\n");
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        ItemStack libro = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bm = (BookMeta) libro.getItemMeta();
        bm.setDisplayName("Muerte de "
                + event.getEntity().getDisplayName()
                + " número "
                + event.getEntity().getStatistic(Statistic.DEATHS));
        bm.setTitle("Te has muerto");
        bm.setAuthor("Trufa");
        bm.addPage(String.format(Locale.ENGLISH,
                "En concreto, fuiste un bobo  aquí: "
                        + event.getEntity().getLocation().getWorld().getName()
                        + "[%.0f, %.0f, %.0f].\n"
                        + "Te mató "
                        + (event.getEntity().getKiller()!=null ?
                        (event.getEntity().getKiller().getDisplayName()
                                + (event.getEntity().getKiller().getInventory().getItemInMainHand()!=null ?
                                (" con " + event.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName())
                                : " por tonto."))
                        : "tu idiocia (" + event.getDeathMessage() + ")."),
                event.getEntity().getLocation().getX(),
                event.getEntity().getLocation().getY(),
                event.getEntity().getLocation().getZ()));
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
                msg = "w " + player.getDisplayName() + " \"Mates a un perro una vez y te llamen mataperros...\"";
                break;
            case 2:
                msg = "w " + player.getDisplayName() + " \"Nota: fomentar la natalidad ESPAÑOLA.\"";
                break;
            case 3:
                msg = "w " + player.getDisplayName() + " \"Sorry but... GOD....is a MAN\"";
                break;
            case 4:
                msg = "w " + player.getDisplayName() + "se nos acaba lo bueno se nos acaba \"el chollo\" wasat va a desaparecer y " +
                        "nos van a ofrecer un nuevo Aplication : wassap 2 costara 8,30$ te cobraran 0,80 dollar por cada mensaje que Tú envias." +
                        " No es una broma reenvíalo para que puedas seguir utilizando wasa";
                break;
            case 5:
                msg = "w " + player.getDisplayName() + " \"\"";
                break;
        }
        if(msg==null) return;
        String finalMsg = msg;
        Bukkit.getScheduler().callSyncMethod(mainPlugin, () -> mainPlugin.getServer().dispatchCommand( mainPlugin.getServer().getConsoleSender(), finalMsg) );
    }

}
