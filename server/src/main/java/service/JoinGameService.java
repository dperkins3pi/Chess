package service;

import chess.ChessGame;
import dataAccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import model.GameData;

public class JoinGameService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public void joinGame(String authToken, String playerColor, Integer gameID) throws UnauthorizedException, DataAccessException, BadRequestException, AlreadyTakenException {
        if(authDAO.getAuth(authToken) == null){  // If the given authtoken is not in the list
            throw new UnauthorizedException("Error: unauthorized");
        }
        if(gameID == null){  // If invalid input is given
            throw new BadRequestException("Error: bad request1");
        }

        // Access the data
        GameData game = gameDAO.getGame(gameID);
        String username = authDAO.getUsername(authToken);
        if(game == null){  //The game does not exist
            throw new BadRequestException("Error: bad request2");
        }
        String gameName = game.gameName();
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        ChessGame chessGame = game.game();

        if(playerColor != null)
            if(playerColor.equals("WHITE")){
                if(whiteUsername != null){  // White user already there
                    throw new AlreadyTakenException("Error: already taken");
                }
                game = new GameData(gameID, username, blackUsername, gameName, chessGame);
                gameDAO.updateGame(game);  // Update the game
            } else if (playerColor.equals("BLACK")) {
                if(blackUsername != null){  // Black user already there
                    throw new AlreadyTakenException("Error: already taken");
                }
                game = new GameData(gameID, whiteUsername, username, gameName, chessGame);
                gameDAO.updateGame(game);  // Update the game
            }
            else{  // Invalid team color selected
                throw new BadRequestException("Error: bad request3");
            }
    }
}
