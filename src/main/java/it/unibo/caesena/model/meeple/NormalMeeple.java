package it.unibo.caesena.model.meeple;

import it.unibo.caesena.model.Player;
import it.unibo.caesena.utils.StringUtil;

public class NormalMeeple implements Meeple {

    private static final int STRENGTH = 1;
    private final Player owner;

    private boolean placed;

    public NormalMeeple(final Player owner) {
        this.owner = owner;

        this.placed = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStrength() {
        return NormalMeeple.STRENGTH;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getOwner() {
        return this.owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlaced(final boolean placed) {
        this.placed = placed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlaced() {
        return this.placed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + (placed ? 1 : 0);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final NormalMeeple other = (NormalMeeple) obj;
        return owner.equals(other.getOwner()) 
            && placed == other.isPlaced();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringUtil.ToStringBuilder().addFromObjectGetters(this).build();
    }

}
