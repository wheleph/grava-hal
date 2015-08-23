package io.github.wheleph.grava.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GameController {

    public static final String INIT_VIEW = "init";
    public static final String GAME_VIEW = "game";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return INIT_VIEW;
    }

    // TODO make url not updated in browser url box
    @RequestMapping(value = "/start_game", method = RequestMethod.POST)
    public String startGame() {
        return GAME_VIEW;
    }

    @RequestMapping(value = "/end_game", method = RequestMethod.POST)
    public String endGame() {
        return INIT_VIEW;
    }
}
