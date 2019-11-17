package com.mc6007.t2.data.allegro.graph.model;

public class AllegroGraphDatabaseConfiguration {

    private final String url;

    public AllegroGraphDatabaseConfiguration(String url) {
        this.url = url;
    }

    public String getDatabaseUrl(){
        return url;
    }

}
