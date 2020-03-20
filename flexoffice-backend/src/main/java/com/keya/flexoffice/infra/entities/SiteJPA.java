package com.keya.flexoffice.infra.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.geolatte.geom.Geometry;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity(name = "site")
@Getter
@Setter
@ToString
public class SiteJPA implements Serializable {
    @Id
    @Column(name = "site_id")
    private String id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", referencedColumnName = "site_id")
    private Set<FloorJPA> floors;
    @Embedded
    private AddressJPA address;
    @Embedded
    private GeoPointJPA geoPoint;
    private Geometry geocode;


    public SiteJPA() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FloorJPA> getFloors() {
        return floors;
    }

    public void setFloors(Set<FloorJPA> floors) {
        this.floors = floors;
    }
}
