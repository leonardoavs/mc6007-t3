package com.mc6007.t2.domain;

import com.franz.agraph.jena.AGModel;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * An authority (a security role) used by Spring Security.
 */
//@Document(collection = "jhi_authority")
public class Authority implements Serializable, Identifiable, DatabaseResource {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    private String id;

    public String getName() {
        return id;
    }

    public void setName(String name) {
        this.id = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authority)) {
            return false;
        }
        return Objects.equals(id, ((Authority) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Authority{" +
            "name or id='" + id + '\'' +
            "}";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void createResource(AGModel model, Resource resource, String baseUrl) {
        Resource base = model.getResource(baseUrl + getClass().getName());
        base = base == null ? model.createResource(baseUrl + getClass().getName()) : base;

        createProperty(model, baseUrl, resource, "id", id);

        if(!model.contains(resource, RDF.type, base)) {
            model.add(resource, RDF.type, base);
        }
    }

    @Override
    public Authority loadEntity(AGModel model, String baseUrl, Statement statement) {
        Resource resource = statement.getSubject();
        this.id = getStringValue(model, baseUrl, resource, "id");
        return this;
    }


}
