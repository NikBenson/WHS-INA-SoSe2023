package de.whs.ni37900.ina.praktikum.inawebapp.models.user;

import de.whs.ni37900.ina.p1.RSSFeed;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "users")
public class UserBean {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Nutzername ist ein notwendiges feld.")
    @Size(min = 3, max = 20, message = "Benutzername muss zwischen 3 und 20 Zeichen haben.")
    @Column(name = "name", unique = true)
    private String name;

    @NotNull(message = "Passwort ist ein notwendiges feld.")
    @Size(min = 3, max = 20, message = "Password muss zwischen 3 und 20 Zeichen haben.")
    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private List<RSSFeed> feeds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<RSSFeed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<RSSFeed> feeds) {
        this.feeds = feeds;
    }
}
