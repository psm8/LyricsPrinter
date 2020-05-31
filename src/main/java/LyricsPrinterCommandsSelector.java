import org.jline.builtins.Commands;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.terminal.Terminal;

import java.io.PrintStream;

public class LyricsPrinterCommandsSelector implements CommandsSelector<Object> {

    @Override
    public void choose(CommandLineInterface cli, ParsedLine pl, String[] argv, LineReader reader, Terminal terminal, Object dummyObject) {
        if ("song".equals(pl.word())) {
            try {
                LyricsPrinterCommands.song(cli, System.out, System.err, argv);
            }catch (Exception e){
                System.out.println("Exception: " + e);
            }

        } else if ("history".equals(pl.word())) {
            try {
                Commands.history(reader, System.out, System.err, argv);
            }catch (Exception e){
                System.out.println("Exception: " + e);
            }

        } else {
            usage(System.out);

        }
    }

    private static void usage(PrintStream out) {
        out.println("Usage: [song/help/history/exit/quit]");
    }
}
