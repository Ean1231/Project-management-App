package com.example.pimoscanner.basemaps;

import java.util.ArrayList;
import java.util.HashMap;

public class PersistBasemaps {
    public static PersistBasemaps getInstance(){
        return ourInstance;
    }

    public final HashMap<String, ArrayList<BasemapItem>> storage = new HashMap<>();

    private PersistBasemaps(){}

    private static final PersistBasemaps ourInstance = new PersistBasemaps();

}
