package io.github.wheleph.grava.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.github.wheleph.grava.web.GameController.GAME_VIEW;
import static io.github.wheleph.grava.web.GameController.INIT_VIEW;
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
                .andExpect(view().name(INIT_VIEW));
    }

    @Test
    public void testStartGame() throws Exception {
        mockMvc.perform(post("/start_game"))
                .andExpect(status().isOk())
                .andExpect(view().name(GAME_VIEW));
    }

    @Test
    public void testEndGame() throws Exception {
        mockMvc.perform(post("/end_game"))
                .andExpect(status().isOk())
                .andExpect(view().name(INIT_VIEW));
    }
}