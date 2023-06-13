package de.whs.ni37900.ina.p1;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.Objects;

@Entity
public class RSSFeed {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Bitte gib einen Namen ein.")
    @Size(min = 3, max = 20, message = "Name muss zwischen 3 und 20 Zeichen lang sein.")
    private String name;

    @NotNull(message = "Feed muss eine URL haben.")
    @URL(message = "Muss eine URL sein!")
    private String uri;

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
