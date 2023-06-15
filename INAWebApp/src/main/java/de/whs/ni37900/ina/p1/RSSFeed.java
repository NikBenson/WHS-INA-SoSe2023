package de.whs.ni37900.ina.p1;

import de.whs.ni37900.ina.praktikum.inawebapp.models.user.UserBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.Objects;

@Entity
@Table(name = "feeds")
public class RSSFeed {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @Column(name = "name")
    private String name;

    @NotNull(message = "Feed muss eine URL haben.")
    @URL(message = "Muss eine URL sein!")
    @Column(name = "link")
    private String uri;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private UserBean owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RSSFeed) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.uri, that.uri);
    }

    public UserBean getOwner() {
        return owner;
    }

    public void setOwner(UserBean owner) {
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uri);
    }

    @Override
    public String toString() {
        return "RSSFeed[" +
                "name=" + name + ", " +
                "uri=" + uri + ']';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
