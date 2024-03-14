package ui;

public class PreLoginClient {
    private final ServerFacade server;
    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void eval(String input) {
        var tokens = input.toLowerCase().split(" ");
//        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        if(input.equals("help")) help();
    }

    public void help() {
        // Create the string to help the user
        String helpString = EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "register <USERNAME> <PASSWORD> <EMAIL>" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - to create an account\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "login <USERNAME> <PASSWORD>" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - to play chess\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "quit" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - playing chess\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "help" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - with possible commands";

        System.out.println(helpString);
    }
}
