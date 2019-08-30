package com.mc6007.t1.data.berkeley;

import com.mc6007.t1.data.berkeley.model.BerkeleyDataBaseConfiguration;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Durability;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.Transaction;
import com.sleepycat.je.rep.ReplicatedEnvironment;
import com.sleepycat.je.rep.ReplicationConfig;
import com.sleepycat.je.rep.TimeConsistencyPolicy;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.keyvalue.core.AbstractKeyValueAdapter;
import org.springframework.data.keyvalue.core.ForwardingCloseableIterator;
import org.springframework.data.util.CloseableIterator;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class BerkeleyKeyValueAdapter extends AbstractKeyValueAdapter {
    private static final Logger log = LoggerFactory.getLogger(BerkeleyKeyValueAdapter.class);

    private EntityStore entityStoreVar;
    private BerkeleyDataBaseConfiguration berkeleyDataBaseConfiguration;

    @Value("${database.port}")
    private int databasePort;

    @Value("${database.helper.port}")
    private Integer databaseHelperPort;

    /**
     * Creates a new {@link BerkeleyKeyValueAdapter} using the given {@link Map} as backing store.
     *
     */
    @SuppressWarnings("rawtypes")
    public BerkeleyKeyValueAdapter() {

    }

    public synchronized EntityStore getEntityStore() {
        if(entityStoreVar == null) {
            berkeleyDataBaseConfiguration = new BerkeleyDataBaseConfiguration(databasePort, databaseHelperPort);
            entityStoreVar = getEntityStoreInitialConfiguration(berkeleyDataBaseConfiguration);
        }
        return entityStoreVar;
    }

    private EntityStore getEntityStoreInitialConfiguration(BerkeleyDataBaseConfiguration berkeleyDataBaseConfiguration) {
        ReplicationConfig replicationConfig = new ReplicationConfig();
        TimeConsistencyPolicy consistencyPolicy = new TimeConsistencyPolicy(
            1, TimeUnit.SECONDS, /* 1 sec of lag */
                 3, TimeUnit.SECONDS  /* Wait up to 3 sec */);
        replicationConfig.setConsistencyPolicy(consistencyPolicy);
        /* Wait up to two seconds for commit acknowledgments. */
        replicationConfig.setReplicaAckTimeout(2, TimeUnit.SECONDS);
        replicationConfig.setNodeName(berkeleyDataBaseConfiguration.getNodeName());
        replicationConfig.setNodeHostPort(berkeleyDataBaseConfiguration.getDataBaseHostName());
        replicationConfig.setHelperHosts(berkeleyDataBaseConfiguration.getDataBaseHostHelperName());
        replicationConfig.setGroupName(berkeleyDataBaseConfiguration.getReplicationGroupName());


        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        envConfig.setTransactional(true);
        envConfig.setLockTimeout(10, TimeUnit.SECONDS);

        Durability durability = new Durability(
                Durability.SyncPolicy.WRITE_NO_SYNC,
                Durability.SyncPolicy.WRITE_NO_SYNC,
                Durability.ReplicaAckPolicy.SIMPLE_MAJORITY);
        envConfig.setDurability(durability);

        ReplicatedEnvironment env = new ReplicatedEnvironment(new File(berkeleyDataBaseConfiguration.getDataBaseDirectory()), replicationConfig, envConfig);

        StoreConfig storeConfig = new StoreConfig();
        storeConfig.setTransactional(true);
        envConfig.setReadOnly(false);
        storeConfig.setAllowCreate(true);
        EntityStore entityStore = new EntityStore(env, "Mc6007T1Store", storeConfig);

        logInfo(berkeleyDataBaseConfiguration, env);

        return entityStore;
    }

    private void logInfo(BerkeleyDataBaseConfiguration berkeleyDataBaseConfiguration, ReplicatedEnvironment env) {
        log.info("\n----------------------------------------------------------\n\t" +
                "Database Directory: \t{}\n\t" +
                "Is Master : \t{}\n\t" +
                "Is Replica: \t{}\n\t" +
                "Is Active : \t{}\n\t" +
                "Node Name : \t{}\n\t" +
                "Database Host Name : \t{}\n\t" +
                "Database Host Helper Name : \t{}\n\t" +
                "Replication Group Name: \t{}\n----------------------------------------------------------",
            berkeleyDataBaseConfiguration.getDataBaseDirectory(),
            env.getState().isMaster(),
            env.getState().isReplica(),
            env.getState().isActive(),
            berkeleyDataBaseConfiguration.getNodeName(),
            berkeleyDataBaseConfiguration.getDataBaseHostName(),
            berkeleyDataBaseConfiguration.getDataBaseHostHelperName(),
            berkeleyDataBaseConfiguration.getReplicationGroupName()
        );
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#put(java.lang.Object, java.lang.Object, java.lang.String)
     */
    @Override
    public Object put(Object id, Object item, String clazzName) {

        if(isMaster()) {
            Assert.notNull(id, "Cannot add item with null id.");
            Assert.notNull(clazzName, "Cannot add item for null collection.");

            return getEntityPrimaryIndex(clazzName).put(item);
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#contains(java.lang.Object, java.lang.String)
     */
    @Override
    public boolean contains(Object id, String keyspace) {
        return get(id, keyspace) != null;
    }

    /* (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#count(java.lang.String)
     */
    @Override
    public long count(String clazzName) {
        return getEntityPrimaryIndex(clazzName).count();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#get(java.lang.Object, java.lang.String)
     */
    @Override
    public Object get(Object id, String clazzName) {
        Assert.notNull(id, "Cannot get item with null id.");
        return getEntityPrimaryIndex(clazzName).get((String)id);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#delete(java.lang.Object, java.lang.String)
     */
    @Override
    public Object delete(Object id, String clazzName) {
        if(isMaster()) {
            Transaction txn = getEntityStore().getEnvironment().beginTransaction(null, null);

            try {
                Assert.notNull(id, "Cannot delete item with null id.");
                PrimaryIndex<String, Object> entityPrimaryIndex = getEntityPrimaryIndex(clazzName);
                Object returnObject = entityPrimaryIndex.get(txn, (String)id, LockMode.READ_UNCOMMITTED_ALL);
                Object value = null;
                if (entityPrimaryIndex.delete(txn, (String)id)) {
                    value = returnObject;
                }
                txn.commit();
                return value;
            } catch (DatabaseException e) {
                txn.abort();
                throw e;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#getAllOf(java.lang.String)
     */
    @Override
    public Collection<?> getAllOf(String clazzName) {
        try(EntityCursor<Object> cursor = getEntityPrimaryIndex(clazzName).entities()){
            return getListFromIterator(cursor.iterator());
        }
    }

    private static <T> List<T> getListFromIterator(Iterator<T> iterator)
    {

        // Create an empty list
        List<T> list = new ArrayList<>();

        // Add each element of iterator to the List
        iterator.forEachRemaining(list::add);

        // Return the List
        return list;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#entries(java.lang.String)
     */
    @Override
    public CloseableIterator<Entry<Object, Object>> entries(String clazzName) {
        Map<Object, Object> objects = new HashMap<>();
        PrimaryIndex<String,Object> index = getEntityPrimaryIndex(clazzName);
        index.keys().iterator().forEachRemaining( x -> objects.put(x, index.get(x)));
        return new ForwardingCloseableIterator<>(objects.entrySet().iterator());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#deleteAllOf(java.lang.String)
     */
    @Override
    public void deleteAllOf(String clazzName) {
        if(isMaster()) {
            getEntityPrimaryIndex(clazzName).keys().delete();
        }
    }

    private boolean isMaster() {
        ReplicatedEnvironment replicatedEnvironment = (ReplicatedEnvironment) getEntityStore().getEnvironment();

        return replicatedEnvironment.getState().isMaster();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#clear()
     */
    @Override
    public void clear() {
        EntityStore entityStore = getEntityStore();
        entityStore.close();
        entityStore.getEnvironment().close();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        clear();
    }

    /**
     * Get map associated with given key space.
     *
     * @param clazzName must not be {@literal null}.
     * @return
     */
    protected PrimaryIndex<String,Object> getEntityPrimaryIndex(String clazzName) {
        try {
            System.out.println("ClazzName: " + clazzName);
            Assert.notNull(clazzName, "Collection must not be null for lookup.");

            Class<Object> clazz = (Class<Object>) ClassUtils.forName(clazzName, Thread.currentThread().getContextClassLoader());

            PrimaryIndex<String, Object> primaryIndex = this.getEntityStore().getPrimaryIndex(
                String.class, clazz);

            logInfo(berkeleyDataBaseConfiguration, (ReplicatedEnvironment) this.getEntityStore().getEnvironment());

            return primaryIndex;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
