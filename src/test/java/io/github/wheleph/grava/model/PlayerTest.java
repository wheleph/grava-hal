package io.github.wheleph.grava.model;

import org.junit.Test;

import static io.github.wheleph.grava.model.Player.PLAYER_1;
import static io.github.wheleph.grava.model.Player.PLAYER_2;
import static io.github.wheleph.grava.model.Player.nextPlayer;
import static org.junit.Assert.assertEquals;

public class PlayerTest {
    @Test
    public void testNextPlayer() {
        assertEquals(PLAYER_2, nextPlayer(PLAYER_1));
        assertEquals(PLAYER_1, nextPlayer(PLAYER_2));
    }

}