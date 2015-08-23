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
import java.util.UUID;

import static io.github.wheleph.grava.web.GameController.GAME_MODEL_VIEW_BEAN;
import static io.github.wheleph.grava.web.GameController.VIEW_GAME;
import static io.github.wheleph.grava.web.GameController.VIEW_INIT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

    private MockHttpSession session1;
    private MockHttpSession session2;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        session1 = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
        session2 = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_INIT));
    }

    @Test
    public void testStartGame() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/start_game"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_GAME))
                .andReturn();

        GameState gameState = getGameState(mvcResult);

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
    public void testMultipleSessions() throws Exception {
        // Initialize one session
        mockMvc.perform(post("/start_game").session(session1))
                .andExpect(status().isOk());

        // Initialize another session
        mockMvc.perform(post("/start_game").session(session2))
                .andExpect(status().isOk());
        // End game in that session
        mockMvc.perform(post("/end_game").session(session2))
                .andExpect(status().isOk());

        // Make move in the first session. Should be valid
        MvcResult mvcResult = mockMvc.perform(post("/move").param("player", "PLAYER_1").param("pitIndex", "2").session(session1))
                .andExpect(status().isOk())
                .andReturn();

        GameState gameState = getGameState(mvcResult);

        assertEquals(GamePhase.IN_PROGRESS, gameState.getGamePhase());
        assertEquals(Player.PLAYER_2, gameState.getCurrentPlayer());
    }

    @Test
    public void testMove() throws Exception {
        mockMvc.perform(post("/start_game").session(session1))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(post("/move").param("player", "PLAYER_1").param("pitIndex", "2").session(session1))
                .andExpect(status().isOk())
                .andReturn();

        GameState gameState = getGameState(mvcResult);

        assertEquals(GamePhase.IN_PROGRESS, gameState.getGamePhase());
        assertEquals(Player.PLAYER_2, gameState.getCurrentPlayer());

        Board board = gameState.getBoard();
        assertEquals(7, board.getPitStoneCount(Player.PLAYER_1, 1));
        assertEquals(0, board.getPitStoneCount(Player.PLAYER_1, 2));
        assertEquals(7, board.getPitStoneCount(Player.PLAYER_1, 3));
        assertEquals(7, board.getPitStoneCount(Player.PLAYER_1, 4));
        assertEquals(7, board.getPitStoneCount(Player.PLAYER_1, 5));
        assertEquals(7, board.getPitStoneCount(Player.PLAYER_1, 6));
        assertEquals(1, board.getGravaHalStoneCount(Player.PLAYER_1));
    }

    @Test
    public void testWrongMove() throws Exception {
        mockMvc.perform(post("/start_game").session(session1))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(post("/move").param("player", "PLAYER_2").param("pitIndex", "2").session(session1))
                .andExpect(status().isOk())
                .andReturn();

        GameModelViewBean gameModelViewBean = getGameModelViewBean(mvcResult);
        assertTrue(gameModelViewBean.getMessage().startsWith("Error"));
    }

    @Test
    public void testEndGame() throws Exception {
        mockMvc.perform(post("/end_game"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_INIT));
    }

    private GameState getGameState(MvcResult mvcResult) {
        GameModelViewBean gameModelViewBean = getGameModelViewBean(mvcResult);
        GameState gameState = gameModelViewBean.getGameState();
        assertNotNull(gameState);
        return gameState;
    }

    private GameModelViewBean getGameModelViewBean(MvcResult mvcResult) {
        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        GameModelViewBean gameModelViewBean = (GameModelViewBean) model.get(GAME_MODEL_VIEW_BEAN);
        assertNotNull(gameModelViewBean);
        return gameModelViewBean;
    }
}