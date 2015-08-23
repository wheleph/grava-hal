package io.github.wheleph.grava.web;

import io.github.wheleph.grava.model.GameState;
import io.github.wheleph.grava.model.Player;
import io.github.wheleph.grava.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameController {

    static final String VIEW_INIT = "init";
    static final String VIEW_GAME = "game";
    static final String BEAN_GAME_STATE = "gameState";

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return VIEW_INIT;
    }

    // TODO make url not updated in browser url box
    @RequestMapping(value = "/start_game", method = RequestMethod.POST)
    public ModelAndView startGame() {
        GameState gameState = gameService.startGame();
        return new ModelAndView(VIEW_GAME, BEAN_GAME_STATE, gameState);
    }

    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public ModelAndView move(Player player, int pitIndex) {
        GameState gameState = gameService.move(player, pitIndex);
        return new ModelAndView(VIEW_GAME, BEAN_GAME_STATE, gameState);
    }

    @RequestMapping(value = "/end_game", method = RequestMethod.POST)
    public String endGame() {
        gameService.endGame();
        return VIEW_INIT;
    }
}
