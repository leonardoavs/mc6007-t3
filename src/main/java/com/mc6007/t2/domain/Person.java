package com.mc6007.t2.domain;

import com.franz.agraph.jena.AGModel;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A Person.
 */
public class Person implements Serializable, Identifiable, DatabaseResource {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 30)
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Person name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }

    public void createResource(AGModel model, Resource resource, String baseUrl) {
        Resource base = model.getResource(baseUrl + getClass().getName());
        base = base == null ? model.createResource(baseUrl + getClass().getName()) : base;

        createProperty(model, baseUrl, resource, "id", id);
        createProperty(model, baseUrl, resource, "name", name);

        if(!model.contains(resource, RDF.type, base)) {
            model.add(resource, RDF.type, base);
        }
    }

    @Override
    public Person loadEntity(AGModel model, String baseUrl, Statement statement) {
        Resource resource = statement.getSubject();
        this.id = getStringValue(model, baseUrl, resource, "id");
        this.name = getStringValue(model, baseUrl, resource, "name");
        return this;
    }

}
