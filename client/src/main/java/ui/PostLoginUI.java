package ui;

import java.util.Scanner;

public class PostLoginUI {
    private final PostLoginClient client;
    private final String serverUrl;
    public PostLoginUI(String serverUrl, String authToken) {
        this.serverUrl = serverUrl;
        client = new PostLoginClient(serverUrl, authToken);
    }

    public void run() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_BLUE +
                " Type help to get started. " +
                EscapeSequences.SET_TEXT_COLOR_YELLOW + EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_WHITE);
        Scanner scanner = new Scanner(System.in);
        String line = "";

        while (!line.equals("quit")) {  // Read input until the use quits
            System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA
                    + "[LOGGED_IN] >>> " + EscapeSequences.SET_TEXT_COLOR_WHITE);
            line = scanner.nextLine();
            try {
                client.eval(line);
            } catch (Throwable e) {
                var msg = e.getMessage();
                System.out.print(msg + "\n");
            }
        }
//        if (!line.equals("quit")){  // If the user didn't quit, move to PostLoginUI
//            new PostLoginUI(this.serverUrl).run();
//        }
    }
}
