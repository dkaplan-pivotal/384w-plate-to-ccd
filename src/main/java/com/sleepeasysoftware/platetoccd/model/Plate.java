package com.sleepeasysoftware.platetoccd.model;

import com.google.common.collect.Table;

import java.util.Optional;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class Plate {

    private final String name;
    private final Table<String, String, Optional<String>> data;


    public Plate(String name, Table<String, String, Optional<String>> data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Table<String, String, Optional<String>> getData() {
        return data;
    }
}
