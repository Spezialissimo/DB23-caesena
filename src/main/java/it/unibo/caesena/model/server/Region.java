package it.unibo.caesena.model.server;

import java.util.List;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name = "Regions")
@Table(name = "Regions")
@Access(AccessType.FIELD)
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int regionID;

    @ManyToOne
    private CardinalPoint cardinalPoint;

    @ManyToOne
    private Continent continent;

    @OneToMany(mappedBy = "region")
    private List<Server> Servers;

    public Region(int regionID, Continent continent) {
        this.regionID = regionID;
        this.continent = continent;
    }

    public int getRegionID() {
        return regionID;
    }

    public Continent getContinent() {
        return continent;
    }

    public List<Server> getServers() {
        return Servers;
    }
}