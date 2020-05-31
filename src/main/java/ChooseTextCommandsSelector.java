import org.jline.builtins.Commands;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.terminal.Terminal;

import java.io.PrintStream;

public class ChooseTextCommandsSelector implements CommandsSelector<Scraper>{

    @Override
    public void choose(CommandLineInterface cli, ParsedLine pl, String[] argv, LineReader reader, Terminal terminal, Scraper scraper) {
        if ("lyrics".equals(pl.word()) && pl.words().size() == 2 && Integer.parseInt(pl.words().get(1)) < 30) {
            try{
                String lyrics = scraper.getLyrics(Integer.parseInt(pl.words().get(1)) - 1);

                String[] cliCommands = {"help", "history", "export", "exportdefaults", "exit", "quit"};
                CommandsSelector<String> commandsSelector = new ExportToPDFCommandsSelector();
                cli.run(commandsSelector, cliCommands, "song@lyrics > ", lyrics);

            }catch (Exception e){
                System.out.println("Exception: " + e);
            }

        } else if ("translation".equals(pl.word()) && pl.words().size() == 2 && Integer.parseInt(pl.words().get(1)) < 30) {
            try{
                String translation = scraper.getTranslation(Integer.parseInt(pl.words().get(1)) - 1);

                String[] cliCommands = {"help", "history", "export", "exportdefaults", "exit", "quit"};
                CommandsSelector<String> commandsSelector = new ExportToPDFCommandsSelector();
                cli.run(commandsSelector, cliCommands, "song@translation > ", translation);

            }catch (Exception e){
                System.out.println("Exception: " + e);
            }

        } else if ("show".equals(pl.word())) {
            scraper.listSongs();

        } else if ("history".equals(pl.word())) {
            try{
                Commands.history(reader, System.out, System.err, argv);

            }catch (Exception e){
                System.out.println("Exception: " + e);
            }

        } else{
            usage(System.out);
        }
    }

    private static void usage(PrintStream out) {
        out.println("Usage: [lyrics/translation/show/help/history/exit/quit]");
    }
}
