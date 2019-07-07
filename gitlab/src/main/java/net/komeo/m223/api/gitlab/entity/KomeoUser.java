package net.komeo.m223.api.gitlab.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import lombok.Data;

@Data
@Entity
@NamedQueries({
    @NamedQuery(name = "allUser", query = "SELECT k FROM KomeoUser k"),
    @NamedQuery(name = "countUser", query = "SELECT COUNT(k) FROM KomeoUser k")})
public class KomeoUser implements Serializable {

    @Id
    int id;

    String name;
    String userName;
    String webUrl;
    String avatarUrl;
}
