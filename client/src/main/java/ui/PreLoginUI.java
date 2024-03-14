package ui;
import java.util.Scanner;

public class PreLoginUI {
    private final PreLoginClient client;
    public PreLoginUI(String serverUrl) {
        client = new PreLoginClient(serverUrl);
    }

    public void run() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_BLUE +
                " Welcome to 240 chess. Type Help to get started." +
                EscapeSequences.SET_TEXT_COLOR_YELLOW + EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_WHITE);
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (!line.equals("quit")) {  // Read input until the use quits
            line = scanner.nextLine();
            try {
                client.eval(line);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }
}
