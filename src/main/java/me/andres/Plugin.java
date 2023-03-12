package me.andres;


import me.discordbot.DiscordBot;
import me.listeners.SpigotListener;
import me.util.Database;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.*;

public final class Plugin extends JavaPlugin {
    // CAMPOS
    private static Database db;
    private static DiscordBot MCBort;

   // EN APERTURA Y CIERRE
    @Override
    public void onEnable() {
        MCBort = new DiscordBot(this);
        db = new Database();
        db.connect_to_db("spigot20230310","posgres","admin");
        Bukkit.getPluginManager().registerEvents(new Eventos(this), this);
        // Handler para el initEmbed
        /*Handler h = new Handler() {
            @Override
            public void publish(LogRecord record) {
                /*if(record.getMessage().contains("Record: ")
                        || record.getMessage().contains("joined")
                        || record.getMessage().contains("command")
                        || record.getMessage().contains("command")
                )
                try {
                    if(Files.exists(Paths.get("./logs/registro.txt"), LinkOption.NOFOLLOW_LINKS)) {
                        Files.write(Paths.get("./logs/registro.txt"),
                                (Date.from(Instant.now()) + record.getMessage()).getBytes(StandardCharsets.UTF_8),
                                StandardOpenOption.APPEND);
                        Files.write(Paths.get("./logs/registro.txt"),
                                "\n".getBytes(StandardCharsets.UTF_8),
                                StandardOpenOption.APPEND);
                    }
                    else
                        Files.write(Paths.get("./logs/registro.txt"),record.getMessage().getBytes(StandardCharsets.UTF_8));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void flush() {
            }
            @Override
            public void close() throws SecurityException {
            }
        };
        Bukkit.getLogger().addHandler(h);
        Bukkit.getServer().getLogger().addHandler(h);
        DATA = (Datos) deserializado("./plugins/elPlugin_data","data.andres");
        */
    }
    @Override
    public void onDisable() {
        MCBort.disableBot();
       // serializado("./plugins/elPlugin_data","data.andres", DATA);
    }

    public static Database getDB() {
        return db;
    }
    public static void serializado(String dir, String filename, Object ser_target) {
        ObjectOutputStream data_out;
        try {
            System.out.println("\nLos path");
            Path p_dir = Paths.get(dir);
            filename = dir + "/" + filename;
            Path p_file = Paths.get(filename);
            System.out.println("\nLos path HECHOS");

            System.out.println("\nEl if con el isDirectory {");
            // Si no existe el directorio lo crea
            if(!Files.isDirectory(p_dir, LinkOption.NOFOLLOW_LINKS)) {
                System.out.println("\n\tEl createDirectory");
                Files.createDirectory(p_dir);
                System.out.println("\n\tEl createDirectory HECHO");
            }
            System.out.println("\n} HECHO");

            System.out.println("\nSerializado");
            Files.deleteIfExists(p_file);
            data_out = new ObjectOutputStream(new FileOutputStream(filename));
            data_out.writeObject(ser_target);
            System.out.println("Serializado HECHO");
        }
        catch (IOException e) {
            System.err.println("Error en deserializacion: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static Object deserializado(String dir, String filename) {
        Object deser_target = null;
        ObjectInputStream data_in;
        try {
            System.out.println("\nLos path");
            Path p_dir = Paths.get(dir);
            filename = dir + "/" + filename;
            Path p_file = Paths.get(filename);
            System.out.println("Los path HECHOS");


            System.out.println("\nEl if con el isDirectory {");
            // Si no existe el directorio lo crea
            if(!Files.isDirectory(p_dir, LinkOption.NOFOLLOW_LINKS)) {
                System.out.println("\n\tEl createDirectory");
                Files.createDirectory(p_dir);
                System.out.println("\tEl createDirectory HECHO");
            }
            System.out.println("\n} El if con el isDirectory HECHO");


            // si existe el fichero data.andres lo carga
            System.out.println("\nEl Files.exists {");
            if(Files.exists(p_file)) {
                System.out.println("\n\tDeserializado");
                data_in = new ObjectInputStream(new FileInputStream(filename));
                deser_target = data_in.readObject();
                System.out.println("\tDeserializado HECHO");
            }
            System.out.println("\n} El Files.exists HECHO");
        }
        catch (ClassNotFoundException e) {
            System.err.println("\nClassNotFound: No se encontró la clase serializada");
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();

        }
        return deser_target;
    }


    // STATICS
    public static boolean isPlayerLookingAtHitBox(Player p1, Location loc, int width, int height)
    {
        boolean isLooking = false;
        Location loc1 = lookAt(p1.getLocation(), loc);
        Location loc2 = p1.getLocation();
        int yaw1 = (int)Math.abs(loc1.getYaw());
        int yaw2 = (int)Math.abs(loc2.getYaw());
        int pitch1 = (int)Math.abs(loc1.getPitch());
        int pitch2 = (int)Math.abs(loc2.getPitch());
        if(yaw1 + width > yaw2 || yaw1 - width < yaw2 && pitch1 + height > pitch2 || pitch1 - height < pitch2)
        {
            isLooking = true;
        }
        return isLooking;
    }
    public static Location lookAt(Location loc, Location lookat)
    {

        loc = loc.clone();

        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();

        if(dx != 0)
        {
            if(dx < 0)
            {
                loc.setYaw((float)(1.5 * Math.PI));
            }
            else
            {
                loc.setYaw((float)(0.5 * Math.PI));
            }
            loc.setYaw((float)loc.getYaw() - (float)Math.atan(dz / dx));
        }
        else if(dz < 0)
        {
            loc.setYaw((float)Math.PI);
        }

        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        loc.setPitch((float)-Math.atan(dy / dxz));

        loc.setYaw(-loc.getYaw() * 180f / (float)Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float)Math.PI);

        return loc;
    }

}
