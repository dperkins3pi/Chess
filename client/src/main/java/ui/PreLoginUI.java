package ui;
import java.util.Scanner;

public class PreLoginUI {
    private final PreLoginClient client;
    private final String serverUrl;
    private String authToken = null;

    public PreLoginUI(String serverUrl) {
        this.serverUrl = serverUrl;
        client = new PreLoginClient(serverUrl);
    }

    public void run() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_BLUE +
                " Welcome to 240 chess. Type help to get started." +
                EscapeSequences.SET_TEXT_COLOR_YELLOW + EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_WHITE);
        Scanner scanner = new Scanner(System.in);
        String line = "";

        while (!line.equals("quit")) {  // Read input until the use quits
            System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA
                    + "[LOGGED_OUT] >>> " + EscapeSequences.SET_TEXT_COLOR_WHITE);
            line = scanner.nextLine();
            try {
                authToken = client.eval(line);
                if(authToken != null) break;// Break if successfully logged in
            } catch (Throwable e) {
                var msg = e.getMessage();
                System.out.println(msg);
            }
        }

        if (!line.equals("quit")){  // If the user didn't quit, move to PostLoginUI
            new PostLoginUI(this.serverUrl, authToken).run();
        }
    }
}
