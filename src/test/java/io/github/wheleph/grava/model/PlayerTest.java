package io.github.wheleph.grava.model;

import org.junit.Test;

import static io.github.wheleph.grava.model.Player.*;
import static org.junit.Assert.*;

public class PlayerTest {
    @Test
    public void testFromIndex() {
        assertSame(PLAYER_1, fromIndex(1));
        assertSame(PLAYER_2, fromIndex(2));
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
        assertSame(PLAYER_2, nextPlayer(PLAYER_1));
        assertSame(PLAYER_1, nextPlayer(PLAYER_2));
    }

}