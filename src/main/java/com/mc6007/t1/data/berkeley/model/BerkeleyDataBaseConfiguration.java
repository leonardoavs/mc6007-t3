package com.mc6007.t1.data.berkeley.model;

import java.io.File;

public class BerkeleyDataBaseConfiguration {

    private final int databasePort;
    private final int databaseHelperPort;

    public BerkeleyDataBaseConfiguration(int databasePort, Integer dataBaseHelperPort) {
        this.databasePort = databasePort;
        this.databaseHelperPort = dataBaseHelperPort == null ? databasePort : dataBaseHelperPort;
    }

    public String getDataBaseDirectory(){
        String dataBaseDirectory = "/Berkeley/databases/database" + databasePort;
        File dataBaseFile = new java.io.File(dataBaseDirectory);
        dataBaseFile.mkdirs();
        return dataBaseDirectory;
    }

    public String getNodeName(){
        return "node" + databasePort;
    }

    public String getDataBaseHostName(){
        return "localhost:" + databasePort;
    }

    public String getDataBaseHostHelperName(){
        return "localhost:" + databaseHelperPort;
    }

    public String getReplicationGroupName(){
        return "Mc6007T1Group";
    }
}
