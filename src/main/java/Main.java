public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println();
        /*better to set it in bat file*/
        System.setProperty("file.encoding", "UTF-8");

        String[] cliArgs = {"simple"};
        String[] cliCommands = {"help", "history", "song", "exit", "quit"};
        CommandLineInterface cli = new CommandLineInterface("prompt > ", cliArgs, cliCommands);
        CommandsSelector<Object> commandsSelector = new LyricsPrinterCommandsSelector();
        cli.run(commandsSelector, cliCommands, "prompt > ", null);
    }
}