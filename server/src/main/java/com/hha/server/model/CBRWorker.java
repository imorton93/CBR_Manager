package com.hha.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@Table(name = "WORKER_DATA")
public class CBRWorker {
    @Column
    @JsonProperty("FIRST_NAME")
    private String firstName;

    @Column
    @JsonProperty("LAST_NAME")
    private String lastName;

    @Column
    @JsonProperty("USERNAME")
    private String username;

    @Column
    @JsonProperty("ZONE")
    private String zone;

    @Column(length = 10485760)
    @JsonProperty("PHOTO")
    private String photo;

    @Column
    @JsonProperty("PASSWORD")
    private String password;

    @Id
    @Column
    @JsonProperty("ID")
    private String id;

    @Column
    @JsonProperty("IS_ADMIN")
    private String isAdmin;

    public CBRWorker() {
    }

    public CBRWorker(String firstName, String lastName, String username, String zone, String photo, String password, String id, String isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.zone = zone;
        this.photo = photo;
        this.password = password;
        this.id = id;
        this.isAdmin = isAdmin;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
