package io.github.wheleph.grava.web;

import io.github.wheleph.grava.model.Board;
import io.github.wheleph.grava.model.GamePhase;
import io.github.wheleph.grava.model.GameState;
import io.github.wheleph.grava.model.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static io.github.wheleph.grava.web.GameController.BEAN_GAME_STATE;
import static io.github.wheleph.grava.web.GameController.VIEW_GAME;
import static io.github.wheleph.grava.web.GameController.VIEW_INIT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:grava-core-config.xml", "classpath:grava-mvc-config.xml"})
public class GameControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    MockHttpSession session;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_INIT));
    }

    // TODO test multiple sessions
    @Test
    public void testStartGame() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/start_game"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_GAME))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        GameState gameState = (GameState) model.get(BEAN_GAME_STATE);
        assertNotNull(gameState);

        // Verify game state
        assertEquals(GamePhase.IN_PROGRESS, gameState.getGamePhase());
        assertEquals(Player.PLAYER_1, gameState.getCurrentPlayer());

        // Verify board
        Board board = gameState.getBoard();
        assertEquals(0, board.getGravaHalStoneCount(Player.PLAYER_1));
        assertEquals(0, board.getGravaHalStoneCount(Player.PLAYER_2));
        assertEquals(6, board.getPitStoneCount(Player.PLAYER_1, 1));
        assertEquals(6, board.getPitStoneCount(Player.PLAYER_2, 2));
    }

    @Test
    public void testEndGame() throws Exception {
        // TODO think how to test end game better
        mockMvc.perform(post("/end_game"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_INIT));
    }
}