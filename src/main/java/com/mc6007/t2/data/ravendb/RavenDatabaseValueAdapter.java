package com.mc6007.t2.data.ravendb;

import com.mc6007.t2.data.ravendb.model.RavenDatabaseConfiguration;
import com.mc6007.t2.domain.Identifiable;
import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.session.IDocumentSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.keyvalue.core.AbstractKeyValueAdapter;
import org.springframework.data.keyvalue.core.ForwardingCloseableIterator;
import org.springframework.data.util.CloseableIterator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.UUID;

public class RavenDatabaseValueAdapter extends AbstractKeyValueAdapter {
    private static final Logger log = LoggerFactory.getLogger(RavenDatabaseValueAdapter.class);

    private final IDocumentStore store;

    private Timer timer = new Timer();

    /**
     * Creates a new {@link RavenDatabaseValueAdapter} using the given {@link Map} as backing store.
     *
     */
    @SuppressWarnings("rawtypes")
    public RavenDatabaseValueAdapter(@Value("${database.port}")int databasePort) {

        RavenDatabaseConfiguration berkeleyDataBaseConfiguration = new RavenDatabaseConfiguration(databasePort);

        store = new DocumentStore(new String[]{berkeleyDataBaseConfiguration.getDataBaseHostName()}, "mc6007db");

        DocumentConventions conventions = store.getConventions();

        store.initialize();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#put(java.lang.Object, java.lang.Object, java.lang.String)
     */
    @Override
    public Object put(Object id, Object item, String clazzName) {

        IDocumentSession session = store.openSession();

        if(item instanceof Identifiable) {
            Identifiable identifiable = (Identifiable) item;
            if(identifiable.getId() == null) {
                identifiable.setId(UUID.randomUUID().toString());
            }
        }

        session.store(item);

        session.saveChanges();
        return item;
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
        IDocumentSession session = store.openSession();

        return session.query(getClazz(clazzName)).count();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#get(java.lang.Object, java.lang.String)
     */
    @Override
    public Object get(Object id, String clazzName) {
        IDocumentSession session = store.openSession();

        return session.load(getClazz(clazzName), id.toString());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#delete(java.lang.Object, java.lang.String)
     */
    @Override
    public Object delete(Object id, String clazzName) {
        IDocumentSession session = store.openSession();

        session.delete(id.toString());

        session.saveChanges();

        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#getAllOf(java.lang.String)
     */
    @Override
    public Collection<?> getAllOf(String clazzName) {
        IDocumentSession session = store.openSession();

        List<?> list = session.query(getClazz(clazzName)).toList();

        return list;
    }

    private Class<?> getClazz(String clazzName) {
        try {
            return Class.forName(clazzName);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#entries(java.lang.String)
     */
    @Override
    public CloseableIterator<Entry<Object, Object>> entries(String clazzName) {
        Map<Object, Object> objects = new HashMap<>();

        IDocumentSession session = store.openSession();

        List<?> list = session.query(getClazz(clazzName)).toList();

        list.stream().forEach(x -> objects.put(x, x));

        return new ForwardingCloseableIterator<>(objects.entrySet().iterator());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#deleteAllOf(java.lang.String)
     */
    @Override
    public void deleteAllOf(String clazzName) {
        IDocumentSession session = store.openSession();

        List<?> list = session.query(getClazz(clazzName)).toList();

        list.stream().forEach(x -> session.delete(x));

        session.saveChanges();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#clear()
     */
    @Override
    public void clear() {
        store.close();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        clear();
    }
}
