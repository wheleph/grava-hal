<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Game of Grava Hal</title>
    </head>
    <body>
        <table border="1" cellpadding="0" cellspacing="0">
            <tr>
                <td></td>
                <td>Grava Hal</td>
                <td>Pit</td>
                <td>Pit</td>
                <td>Pit</td>
                <td>Pit</td>
                <td>Pit</td>
                <td>Pit</td>
                <td>Grava Hal</td>
            </tr>
            <tr>
                <td>Player 1</td>
                <td>&nbsp;</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_1', 1)}</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_1', 2)}</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_1', 3)}</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_1', 4)}</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_1', 5)}</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_1', 6)}</td>
                <td>${gameState.board.getGravaHalStoneCount("PLAYER_1")}</td>
            </tr>
            <tr>
                <td>Player 2</td>
                <td>${gameState.board.getGravaHalStoneCount("PLAYER_2")}</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_2', 6)}</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_2', 5)}</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_2', 4)}</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_2', 3)}</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_2', 2)}</td>
                <td>${gameState.board.getPitStoneCount('PLAYER_2', 1)}</td>
                <td>&nbsp;</td>
            </tr>
        </table>
        <form action="end_game" method="POST">
            <button type="submit">End game</button>
        </form>
    </body>
</html>