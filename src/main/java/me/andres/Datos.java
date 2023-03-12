package me.andres;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Datos implements Serializable {


    private static final long serialVersionUID = -6899040859891724497L;
    private static Map<String, List<Location>> playerChests;


    public Datos() {
        playerChests = new HashMap<>();
    }



}
