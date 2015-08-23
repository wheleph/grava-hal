package io.github.wheleph.grava.web;

import io.github.wheleph.grava.model.GamePhase;
import io.github.wheleph.grava.model.GameState;
import io.github.wheleph.grava.model.Player;
import io.github.wheleph.grava.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
public class GameController {

    static final String VIEW_INIT = "init";
    static final String VIEW_GAME = "game";
    static final String GAME_MODEL_VIEW_BEAN = "gameModelViewBean";
    static final String MSG_ERROR = "msg.error";
    static final String MSG_NAME_PLAYER1 = "name.player1";
    static final String MSG_NAME_PLAYER2 = "name.player2";
    static final String MSG_MOVE_PROMPT = "msg.move.prompt";
    static final String MSG_DRAW_GAME = "msg.draw.game";
    static final String MSG_VICTORY = "msg.victory";

    @Autowired
    private GameService gameService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return VIEW_INIT;
    }

    @RequestMapping(value = "/start_game", method = RequestMethod.POST)
    public ModelAndView startGame() {
        GameState gameState = gameService.startGame();
        return new ModelAndView(VIEW_GAME, GAME_MODEL_VIEW_BEAN, createViewBean(gameState));
    }

    /**
     * @param pitIndex 1-based pit index
     */
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public ModelAndView move(Player player, int pitIndex) {
        GameState oldGameState = gameService.getGameState();
        try {
            GameState gameState = gameService.move(player, pitIndex);
            return new ModelAndView(VIEW_GAME, GAME_MODEL_VIEW_BEAN, createViewBean(gameState));
        } catch (Exception e) {
            String errorMessage = getSimpleMessage(MSG_ERROR, new Object[] {e.getMessage()});
            return new ModelAndView(VIEW_GAME, GAME_MODEL_VIEW_BEAN, new GameModelViewBean(oldGameState, errorMessage));
        }
    }

    @RequestMapping(value = "/end_game", method = RequestMethod.POST)
    public String endGame() {
        gameService.endGame();
        return VIEW_INIT;
    }

    private GameModelViewBean createViewBean(GameState gameState) {
        String message;
        GamePhase gamePhase = gameState.getGamePhase();
        Player currentPlayer = gameState.getCurrentPlayer();
        String player1Name = getSimpleMessage(MSG_NAME_PLAYER1);
        String player2Name = getSimpleMessage(MSG_NAME_PLAYER2);
        if (gamePhase == GamePhase.IN_PROGRESS) {
            if (currentPlayer == Player.PLAYER_1) {
                message = getSimpleMessage(MSG_MOVE_PROMPT, new Object[] { player1Name });
            } else if (currentPlayer == Player.PLAYER_2){
                message = getSimpleMessage(MSG_MOVE_PROMPT, new Object[] { player2Name });
            } else {
                throw new IllegalArgumentException("Unknown player");
            }
        } else if (gamePhase == GamePhase.DRAW) {
            message = getSimpleMessage(MSG_DRAW_GAME);
        } else if (gamePhase == GamePhase.VICTORY) {
            if (currentPlayer == Player.PLAYER_1) {
                message = getSimpleMessage(MSG_VICTORY, new Object[] { player1Name });
            } else if (currentPlayer == Player.PLAYER_2){
                message = getSimpleMessage(MSG_VICTORY, new Object[] { player2Name });
            } else {
                throw new IllegalArgumentException("Unknown player");
            }
        } else {
            throw new IllegalArgumentException("Unknown game phase");
        }

        return new GameModelViewBean(gameState, message);
    }

    private String getSimpleMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, Locale.getDefault());
    }

    private String getSimpleMessage(String code) {
        return getSimpleMessage(code, new Object[0]);
    }
}
