package com.mc6007.t2.domain;

import com.franz.agraph.jena.AGModel;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

public interface DatabaseResource extends Identifiable {
    void createResource(AGModel model, Resource resource, String baseUrl);

    <T> T loadEntity(AGModel model, String baseUrl, Statement statement);

    default void createProperty(AGModel model, String baseUrl,  Resource resource, String property, String value) {
        if(value != null) {
            Property accountName = createProperty(model, baseUrl, property);
            resource.addProperty(accountName, value);
        }
    }

    default Property createProperty(AGModel model, String baseUrl, String property) {
        Property prop = model.getProperty(baseUrl + property);
        return prop == null ? model.createProperty(baseUrl+ property) : prop;
    }

    default String getStringValue(AGModel model, String baseUrl, Resource resource, String activationKey) {
        Statement statement = resource.getProperty(createProperty(model, baseUrl, activationKey));
        return statement == null ? null : statement.getString();
    }
}
