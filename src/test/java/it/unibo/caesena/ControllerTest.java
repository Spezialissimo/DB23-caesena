package it.unibo.caesena;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.unibo.caesena.controller.Controller;
import it.unibo.caesena.controller.ControllerImpl;
import it.unibo.caesena.model.Color;
import it.unibo.caesena.model.tile.Tile;
import it.unibo.caesena.model.tile.TileFactoryWithBuilder;
import it.unibo.caesena.utils.Pair;

final class ControllerTest {
    private final Controller controller;
    private final String firstPlayerName;
    private final Color firstPlayerColor;
    private final String secondPlayerName;
    private final Color secondPlayerColor;

    ControllerTest() {
        this.controller = new ControllerImpl();
        this.firstPlayerName = "Giocatore1";
        this.secondPlayerName = "Giocatore2";
        this.firstPlayerColor = new Color(0, 0, 0);
        this.secondPlayerColor = new Color(50, 50, 50);
    }

    @Test
    void testStartGameAndAddPlayer() {
        this.controller.resetGame();
        assertThrows(IllegalStateException.class, () -> this.controller.startGame());
        this.controller.addPlayer(this.firstPlayerName, this.firstPlayerColor);
        assertDoesNotThrow(() -> this.controller.startGame());
    }

    @Test
    void testTilesBuild() {
        this.controller.resetGame();
        this.controller.addPlayer(this.firstPlayerName, this.firstPlayerColor);
        this.controller.startGame();
        assertFalse(this.controller.getNotPlacedTiles().isEmpty());
    }

    @Test
    void testAddPlayer() {
        this.controller.resetGame();
        this.controller.addPlayer(this.firstPlayerName, this.firstPlayerColor);
        this.controller.startGame();
        assertEquals(this.controller.getPlayers().get(0), this.controller.getCurrentPlayer().get());
        this.controller.addPlayer(this.secondPlayerName, this.secondPlayerColor);
        assertEquals(2, this.controller.getPlayers().size());
    }

    @Test
    void testGetMeeples() {
        this.controller.resetGame();
        this.controller.addPlayer(this.firstPlayerName, this.firstPlayerColor);
        this.controller.startGame();
        assertEquals(this.controller.getPlayerMeeples(this.controller.getCurrentPlayer().get()).size(), 8);
    }

    @Test
    void testIsCurrentTilePleceable() {
        final Tile citytile = new TileFactoryWithBuilder().createCity().getX();
        final Tile tubetile = new TileFactoryWithBuilder().createCityTube().getX();

        this.controller.resetGame();
        this.controller.addPlayer(this.firstPlayerName, this.firstPlayerColor);
        this.controller.startGame();

        while (!this.controller.getCurrentTile().equals(citytile) &&
            !this.controller.getCurrentTile().equals(tubetile)) {
            this.controller.endTurn();
        }
        if (this.controller.getCurrentTile().equals(tubetile)) {
            this.controller.rotateCurrentTile();
        }
        assertTrue(this.controller.isPositionValidForCurrentTile(new Pair<>(0, -1)));
    }
}
