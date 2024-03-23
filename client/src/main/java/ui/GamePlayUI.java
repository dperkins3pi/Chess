package ui;

import handler.GameHandler;

import java.util.Scanner;

public class GamePlayUI implements GameHandler {
    private final GamePlayClient client;
    private final String serverUrl;
    private final String authToken;
    public GamePlayUI(String serverUrl, String authToken) {
        this.serverUrl = serverUrl;
        this.authToken = authToken;
        client = new GamePlayClient(serverUrl, authToken);
    }

    public void run() {
        String state = "gamePlay";
        System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_BLUE +
                " Type help if you need it. " +
                EscapeSequences.SET_TEXT_COLOR_YELLOW + EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_WHITE);
        client.display();
        Scanner scanner = new Scanner(System.in);
        String line = "";

        while (!line.equals("quit")) {  // Read input until the use quits
            System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA
                    + "[GAME_PLAY] >>> " + EscapeSequences.SET_TEXT_COLOR_WHITE);
            line = scanner.nextLine();
            try {
                state = client.eval(line);
                if (state.equals("loggedOut")) break;
                if (state.equals("loggedIn")) break;
            } catch (Throwable e) {
                var msg = e.getMessage();
                System.out.print(msg + "\n");
            }
        }
        if (!line.equals("quit") && state.equals("loggedOut")){  // If the user didn't quit, move to PreLoginUI
            new PreLoginUI(this.serverUrl).run();
        } else if (!line.equals("quit")){  // If the user didn't quit, move to PreLoginUI
            new PostLoginUI(this.serverUrl, authToken).run();
        }

    }
}
