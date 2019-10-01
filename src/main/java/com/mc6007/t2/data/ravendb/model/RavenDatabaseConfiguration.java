package com.mc6007.t2.data.ravendb.model;

public class RavenDatabaseConfiguration {

    private final String url;

    public RavenDatabaseConfiguration(String url) {
        this.url = url;
    }

    public String getDatabaseUrl(){
        return url;
    }

}
