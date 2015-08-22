package io.github.wheleph.grava.model;

import org.junit.Test;

import static io.github.wheleph.grava.model.Player.PLAYER_1;
import static io.github.wheleph.grava.model.Player.PLAYER_2;
import static io.github.wheleph.grava.model.Player.fromIndex;
import static io.github.wheleph.grava.model.Player.nextPlayer;
import static io.github.wheleph.grava.model.Player.values;
import static org.junit.Assert.assertEquals;

public class PlayerTest {
    @Test
    public void testFromIndex() {
        assertEquals(PLAYER_1, fromIndex(1));
        assertEquals(PLAYER_2, fromIndex(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromIndexZero() {
        fromIndex(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromIndexLarge() {
        int numberOfPlayers = values().length;
        fromIndex(numberOfPlayers + 1);
    }

    @Test
    public void testNextPlayer() {
        assertEquals(PLAYER_2, nextPlayer(PLAYER_1));
        assertEquals(PLAYER_1, nextPlayer(PLAYER_2));
    }

}