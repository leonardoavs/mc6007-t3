package com.mc6007.t2.data.allegro.graph;

import com.franz.agraph.jena.AGGraphMaker;
import com.franz.agraph.jena.AGModel;
import com.franz.agraph.repository.AGCatalog;
import com.franz.agraph.repository.AGRepository;
import com.franz.agraph.repository.AGRepositoryConnection;
import com.franz.agraph.repository.AGServer;
import com.mc6007.t2.data.allegro.graph.model.AllegroGraphDatabaseConfiguration;
import com.mc6007.t2.domain.DatabaseResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
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
import java.util.UUID;
import java.util.stream.Collectors;

public class AllegroGraphDatabaseValueAdapter extends AbstractKeyValueAdapter {
    private static final Logger log = LoggerFactory.getLogger(AllegroGraphDatabaseValueAdapter.class);

    private String uri = "http://localhost:8080/";
    private final AGGraphMaker maker;
    private final AGModel model;


    /**
     * Creates a new {@link AllegroGraphDatabaseValueAdapter} using the given {@link Map} as backing store.
     *
     */
    @SuppressWarnings("rawtypes")
    public AllegroGraphDatabaseValueAdapter(@Value("${database.url}")String url) {

        AllegroGraphDatabaseConfiguration berkeleyDataBaseConfiguration = new AllegroGraphDatabaseConfiguration(url);

        AGServer server = new AGServer("http://localhost:10035", "test", "xyzzy");

        AGCatalog catalog = server.getCatalog();

        catalog.deleteRepository("mc6003t3repo");
        AGRepository repository = catalog.createRepository("mc6003t3repo");
        repository.initialize();
        AGRepositoryConnection connection = repository.getConnection();

        this.maker = new AGGraphMaker(connection);

        this.model = new AGModel(maker.getGraph());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#put(java.lang.Object, java.lang.Object, java.lang.String)
     */
    @Override
    public Object put(Object id, Object item, String clazzName) {

        if(item instanceof DatabaseResource) {
            DatabaseResource databaseResource = (DatabaseResource) item;
            Resource resource = null;
            if(databaseResource.getId() == null) {
                databaseResource.setId(UUID.randomUUID().toString());
                resource = model.createResource(uri + clazzName + "/" + databaseResource.getId());
            } else {
                resource = model.getResource(uri + clazzName + "/" + databaseResource.getId());
            }
            databaseResource.createResource(model, resource, uri);
        }


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
        long count = model.listStatements(null, RDF.type, createBaseResource(clazzName)).toList().size();
        return count;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#get(java.lang.Object, java.lang.String)
     */
    @Override
    public Object get(Object id, String clazzName) {
        List<Statement> list = model.listStatements(null, createProperty("id"), String.valueOf(id)).toList();

        return list.stream()
            .peek( x -> System.out.println(x))
            .map(statement -> createResource(clazzName).loadEntity(model, uri, statement))
            .findFirst()
            .orElse(null);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#delete(java.lang.Object, java.lang.String)
     */
    @Override
    public Object delete(Object id, String clazzName) {
        //List<Statement> list = model.listStatements(getEntityResourceWithId(clazzName, String.valueOf(id)), createProperty("id"), String.valueOf(id)).toList();
        List<Statement> list = model.listStatements(getEntityResourceWithId(clazzName, String.valueOf(id)), null, (RDFNode) null).toList();
        model.remove(list);
        //maker.removeGraph(uri + clazzName + "/" + String.valueOf(id));
        //model.removeAll(getEntityResourceWithId(clazzName, String.valueOf(id)), createProperty("id"), model.createLiteral(String.valueOf(id)));
        return null;
    }

    private Resource getEntityResourceWithId(String clazzName, String id){
        return model.createResource(uri + clazzName + "/" + id);
    }

    private Resource getEntityResource(String clazzName){
        return model.createResource(uri + clazzName);
    }

    private Property createProperty(String property){
        return model.createProperty(uri + property);
    }

    private Resource createBaseResource(String clazzName){
        return model.createResource(uri + clazzName);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#getAllOf(java.lang.String)
     */
    @Override
    public Collection<?> getAllOf(String clazzName) {
        List<Statement> list = model.listStatements(null, RDF.type, createBaseResource(clazzName)).toList();

        return list.stream()
            .map(statement -> createResource(clazzName).loadEntity(model, uri, statement))
            .collect(Collectors.toList());
    }

    private DatabaseResource createResource(String clazzName) {
        try {
            return (DatabaseResource) Class.forName(clazzName).newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#entries(java.lang.String)
     */
    @Override
    public CloseableIterator<Entry<Object, Object>> entries(String clazzName) {
        Map<Object, Object> objects = new HashMap<>();

        List<Statement> list = model.listStatements(getEntityResource(clazzName), null, (RDFNode)null).toList();

        list.stream().map(statement -> createResource(clazzName).loadEntity(model, uri, statement)).forEach(x -> objects.put(x, x));

        return new ForwardingCloseableIterator<>(objects.entrySet().iterator());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#deleteAllOf(java.lang.String)
     */
    @Override
    public void deleteAllOf(String clazzName) {
        maker.removeGraph(uri + clazzName);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#clear()
     */
    @Override
    public void clear() {
        model.close();
        maker.close();
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
