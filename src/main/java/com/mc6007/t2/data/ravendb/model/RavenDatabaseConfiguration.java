package com.mc6007.t2.data.ravendb.model;

public class RavenDatabaseConfiguration {

    private final int databasePort;

    public RavenDatabaseConfiguration(int databasePort) {
        this.databasePort = databasePort;
    }

    public String getDataBaseHostName(){
        return "http://localhost:" + databasePort;
    }

}
