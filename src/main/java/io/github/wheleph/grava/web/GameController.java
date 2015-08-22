package io.github.wheleph.grava.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GameController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "game";
    }
}
