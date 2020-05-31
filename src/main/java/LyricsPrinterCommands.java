import java.io.PrintStream;

class LyricsPrinterCommands {

    static void song(CommandLineInterface cli, PrintStream out, PrintStream err, String[] argv) throws Exception {

        if (argv.length == 2 && ("-t".equals(argv[0]) || "--title".equals(argv[0]))) {
            Scraper scraper = new Scraper(argv[1]);
            scraper.getSongsList();

            String[] cliCommands = {"help", "history", "lyrics", "translation", "show", "exit", "quit"};
            CommandsSelector<Scraper> commandsSelector = new ChooseTextCommandsSelector();
            cli.run(commandsSelector, cliCommands, "lyrics/translation [song number] > ", scraper);

        } /*else if (argv.length == 2 && ("-a".equals(argv[0]) || "--author".equals(argv[0]))) {
            Scraper scraper = new Scraper("", argv[1]);
            scraper.getSongsList();

            String[] cliCommands = {"help", "history", "lyrics", "translation", "show", "exit", "quit"};
            CommandsSelector<Scraper> commandsSelector = new ChooseTextCommandsSelector();
            cli.run(commandsSelector, cliCommands, "lyrics/translation [song number] > ", scraper);

        }*/else if (argv.length == 4 && ("-t".equals(argv[0]) || "--title".equals(argv[0]))
                && ("-a".equals(argv[2]) || "--author".equals(argv[2]))) {
            Scraper scraper = new Scraper(argv[1],argv[3]);
            scraper.getSongsList();

            String[] cliCommands = {"help", "history", "lyrics", "translation", "show", "exit", "quit"};
            CommandsSelector<Scraper> commandsSelector = new ChooseTextCommandsSelector();
            cli.run(commandsSelector, cliCommands, "lyrics/translation [song number] > ", scraper);

        } else{
            songOptions(out);
        }
    }

    private static void songOptions(PrintStream out) {
        out.println("Usage: song -t --title title+separated+with+plus+signs");
        out.println("Usage: song -t --title title+separated+with+plus+signs -a --author author+separated+with+plus+signs");
    }
}
