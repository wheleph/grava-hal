<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title><spring:message code="title" /></title>
    </head>
    <body>
        <table border="1" cellspacing="0">
            <tr>
                <td></td>
                <td><spring:message code="board.grava.hal"/></td>
                <td><spring:message code="board.pit"/></td>
                <td><spring:message code="board.pit"/></td>
                <td><spring:message code="board.pit"/></td>
                <td><spring:message code="board.pit"/></td>
                <td><spring:message code="board.pit"/></td>
                <td><spring:message code="board.pit"/></td>
                <td><spring:message code="board.grava.hal"/></td>
            </tr>
            <tr>
                <td><spring:message code="name.player1"/></td>
                <td bgcolor="black">&nbsp;</td>
                <td>
                    <%--TODO think how to extract this common part into some fragment--%>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_1" />
                        <input type="hidden" name="pitIndex" value="1" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_1', 1)}</button>
                    </form>
                </td>
                <td>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_1" />
                        <input type="hidden" name="pitIndex" value="2" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_1', 2)}</button>
                    </form>
                </td>
                <td>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_1" />
                        <input type="hidden" name="pitIndex" value="3" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_1', 3)}</button>
                    </form>
                </td>
                <td>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_1" />
                        <input type="hidden" name="pitIndex" value="4" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_1', 4)}</button>
                    </form>
                </td>
                <td>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_1" />
                        <input type="hidden" name="pitIndex" value="5" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_1', 5)}</button>
                    </form>
                </td>
                <td>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_1" />
                        <input type="hidden" name="pitIndex" value="6" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_1', 6)}</button>
                    </form>
                </td>
                <td>${gameModelViewBean.gameState.board.getGravaHalStoneCount('PLAYER_1')}</td>
            </tr>
            <tr>
                <td><spring:message code="name.player2"/></td>
                <td>${gameModelViewBean.gameState.board.getGravaHalStoneCount('PLAYER_2')}</td>
                <td>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_2" />
                        <input type="hidden" name="pitIndex" value="6" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_2', 6)}</button>
                    </form>
                </td>
                <td>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_2" />
                        <input type="hidden" name="pitIndex" value="5" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_2', 5)}</button>
                    </form>
                </td>
                <td>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_2" />
                        <input type="hidden" name="pitIndex" value="4" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_2', 4)}</button>
                    </form>
                </td>
                <td>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_2" />
                        <input type="hidden" name="pitIndex" value="3" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_2', 3)}</button>
                    </form>
                </td>
                <td>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_2" />
                        <input type="hidden" name="pitIndex" value="2" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_2', 2)}</button>
                    </form>
                </td>
                <td>
                    <form action="move" method="POST">
                        <input type="hidden" name="player" value="PLAYER_2" />
                        <input type="hidden" name="pitIndex" value="1" />
                        <button type="submit">${gameModelViewBean.gameState.board.getPitStoneCount('PLAYER_2', 1)}</button>
                    </form>
                </td>
                <td bgcolor="black">&nbsp;</td>
            </tr>
        </table>
        <p>${gameModelViewBean.message}</p>
        <form action="end_game" method="POST">
            <button type="submit"><spring:message code="button.end.game"/></button>
        </form>
    </body>
</html>