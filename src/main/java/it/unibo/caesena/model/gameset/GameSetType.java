package it.unibo.caesena.model.gameset;

import java.util.List;

import it.unibo.caesena.model.Expansion;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * This is an enum class that allows us to identify the various
 * types of gamesets and its relative initial and final scores.
 */
@Entity(name = "GameSetTypes")
@Table(name = "GameSetTypes")
@Access(AccessType.FIELD)
public class GameSetType {

    @Id
    private String name;

    @OneToMany(mappedBy = "type")
    private List<GameSetImpl> gameSets;

    @ManyToOne
    private Expansion expansion;

    private int startingPoints;
    private int endGameRatio;

    /**
     * Class constructor.
     *
     * @param startingPoints points related to GameSet initialization.
     * @param endGameRatio ratio of related points at the end of the game
     */
    public GameSetType() {}

    /**
     *
     * @return name of a specific GameSet.
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return starting points of a specific GameSet.
     */
    public int getStartingPoints() {
        return this.startingPoints;
    }

    /**
     *
     * @return division of points at the end of the game.
     */
    public int getEndGameRatio() {
        return this.endGameRatio;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameSetType other = (GameSetType) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.toUpperCase().equals(other.name.toUpperCase()))
            return false;
        return true;
    }

}
