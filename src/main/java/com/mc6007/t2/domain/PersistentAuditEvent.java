package com.mc6007.t2.domain;

import com.franz.agraph.jena.AGModel;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Persist AuditEvent managed by the Spring Boot actuator.
 *
 * @see org.springframework.boot.actuate.audit.AuditEvent
 */
public class PersistentAuditEvent implements Serializable, Identifiable, DatabaseResource {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    private String principal;

    private Date auditEventDate;

    private String auditEventType;

    private Map<String, String> data = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Date getAuditEventDate() {
        return auditEventDate;
    }

    public void setAuditEventDate(Date auditEventDate) {
        this.auditEventDate = auditEventDate;
    }

    public String getAuditEventType() {
        return auditEventType;
    }

    public void setAuditEventType(String auditEventType) {
        this.auditEventType = auditEventType;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersistentAuditEvent)) {
            return false;
        }
        return id != null && id.equals(((PersistentAuditEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PersistentAuditEvent{" +
            "principal='" + principal + '\'' +
            ", auditEventDate=" + auditEventDate +
            ", auditEventType='" + auditEventType + '\'' +
            '}';
    }

    public void createResource(AGModel model, Resource resource, String baseUrl) {
        Resource base = model.getResource(baseUrl + getClass().getName());
        base = base == null ? model.createResource(baseUrl + getClass().getName()) : base;

        createProperty(model, baseUrl, resource, "id", String.valueOf(id));
        createProperty(model, baseUrl, resource, "principal", String.valueOf(principal));
        createProperty(model, baseUrl, resource, "auditEventDate", String.valueOf(auditEventDate));
        createProperty(model, baseUrl, resource, "auditEventType", auditEventType);

        int i = 0;
        for(Map.Entry<String, String> entry : data.entrySet()) {
            createProperty(model, baseUrl, resource, "dataKey" + i, entry.getKey());
            createProperty(model, baseUrl, resource, "dataValue" + i, entry.getValue());
            i++;
        }
        if(!model.contains(resource, RDF.type, base)) {
            model.add(resource, RDF.type, base);
        }
    }

    @Override
    public PersistentAuditEvent loadEntity(AGModel model, String baseUrl, Statement statement) {
        Resource resource = statement.getSubject();
        this.id = getStringValue(model, baseUrl, resource, "id");
        this.principal = getStringValue(model, baseUrl, resource, "principal");
        this.auditEventDate =  null;//getStringValue(model, baseUrl, resource, "id")).getString();
        this.auditEventType = getStringValue(model, baseUrl, resource, "auditEventType");
        int i = 0;
        while(true) {
            String key = getStringValue(model, baseUrl, resource, "dataKey" + i);
            String data = getStringValue(model, baseUrl, resource, "dataValue" + i);
            if(
                (key == null || key.trim().length() == 0)
                && (data == null || data.trim().length() == 0)
            ) {
                break;
            }
            this.data.put(key, data);
            i++;
        }
        return this;
    }
}
