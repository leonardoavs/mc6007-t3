package com.mc6007.t2.domain;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import com.franz.agraph.jena.AGModel;
import com.mc6007.t2.config.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * A user.
 */
public class User implements Serializable, Identifiable, DatabaseResource {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    //@JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 10)
    private String langKey;

    @Size(max = 256)
    private String imageUrl;

    @Size(max = 20)
    //@JsonIgnore
    private String activationKey;

    @Size(max = 20)
    //@JsonIgnore
    private String resetKey;

    private Date resetDate = null;

    //@JsonIgnore
    private Set<String> authorities = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    // Lowercase the login before saving it in database
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Date getResetDate() {
        return resetDate;
    }

    public void setResetDate(Date resetDate) {
        this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated='" + activated + '\'' +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }


    @CreatedBy
    //@JsonIgnore
    private String createdBy;

    @CreatedDate
    //@JsonIgnore
    private Date createdDate = new Date();

    @LastModifiedBy
    //@JsonIgnore
    private String lastModifiedBy;

    @LastModifiedDate
    //@JsonIgnore
    private Date lastModifiedDate = new Date();

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void createResource(AGModel model, Resource resource, String baseUrl) {
        Resource base = model.getResource(baseUrl + getClass().getName());
        base = base == null ? model.createResource(baseUrl + getClass().getName()) : base;

        createProperty(model, baseUrl, resource, "id", id);
        createProperty(model, baseUrl, resource, "password", password);
        createProperty(model, baseUrl, resource, "login", login);
        createProperty(model, baseUrl, resource, "firstName", firstName);
        createProperty(model, baseUrl, resource, "lastName", lastName);
        createProperty(model, baseUrl, resource, "email", email);
        createProperty(model, baseUrl, resource, "imageUrl", imageUrl);
        createProperty(model, baseUrl, resource, "activated", String.valueOf(activated));
        createProperty(model, baseUrl, resource, "langKey", langKey);
        createProperty(model, baseUrl, resource, "activationKey", activationKey);
        createProperty(model, baseUrl, resource, "resetKey", resetKey);
        createProperty(model, baseUrl, resource, "resetDate", String.valueOf(resetDate));
        createProperty(model, baseUrl, resource, "createdBy", String.valueOf(createdBy));
        createProperty(model, baseUrl, resource, "createdDate", String.valueOf(createdDate));
        createProperty(model, baseUrl, resource, "lastModifiedBy", String.valueOf(lastModifiedBy));
        createProperty(model, baseUrl, resource, "lastModifiedDate", String.valueOf(lastModifiedDate));
        int i = 0;
        for(String entry : authorities) {
            createProperty(model, baseUrl, resource, "authority" + i, entry);
            i++;
        }

        if(!model.contains(resource, RDF.type, base)) {
            model.add(resource, RDF.type, base);
        }
    }

    @Override
    public User loadEntity(AGModel model, String baseUrl, Statement statement) {
        Resource resource = statement.getSubject();
        this.id = getStringValue(model, baseUrl, resource, "id");
        this.password = getStringValue(model, baseUrl, resource, "password");
        this.login = getStringValue(model, baseUrl, resource, "login");
        this.firstName = getStringValue(model, baseUrl, resource, "firstName");
        this.lastName = getStringValue(model, baseUrl, resource, "lastName");
        this.email = getStringValue(model, baseUrl, resource, "email");
        this.imageUrl = getStringValue(model, baseUrl, resource, "imageUrl");
        //this.activated = resource.getProperty(createProperty(model, baseUrl, "activated")).getBoolean();
        this.activated = Boolean.valueOf(getStringValue(model, baseUrl, resource, "activated"));

        //this.activated = active != null ? active.equalsIgnoreCase("true") : false;

        this.langKey = getStringValue(model, baseUrl, resource, "langKey");
        this.activationKey = getStringValue(model, baseUrl, resource, "activationKey");
        this.resetKey = getStringValue(model, baseUrl, resource, "resetKey");
        this.resetDate = null;//resource.getProperty(createProperty(model, baseUrl, "resetDate")).getString();
        this.createdBy = getStringValue(model, baseUrl, resource, "createdBy");
        this.createdDate = null;//resource.getProperty(createProperty(model, baseUrl, "createdDate")).getString();
        this.lastModifiedBy = getStringValue(model, baseUrl, resource, "lastModifiedBy");
        this.lastModifiedDate = null;//resource.getProperty(createProperty(model, baseUrl, "lastModifiedDate")).getString();

        int i = 0;
        while(true) {
            String key = getStringValue(model, baseUrl, resource,"authority" + i);
            if(key == null || key.trim().length() == 0) {
                break;
            }
            this.authorities.add(key);
            i++;
        }

        return this;
    }


}
