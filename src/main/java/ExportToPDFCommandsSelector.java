import org.jline.builtins.Commands;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.terminal.Terminal;

import java.io.PrintStream;

public class ExportToPDFCommandsSelector implements CommandsSelector<String>{

    @Override
    public void choose(CommandLineInterface cli, ParsedLine pl, String[] argv, LineReader reader, Terminal terminal, String text){
        if ("export".equals(pl.word())) {
            try{
                ExportToPDFCommands.export(System.out, System.err, argv, text);
            }catch (Exception e){
                System.out.println("Exception: " + e);
            }

        } else if ("exportdefaults".equals(pl.word())) {
            try{
                ExportToPDFCommands.exportDefaults(System.out, System.err, argv);
            }catch (Exception e){
                System.out.println("Exception: " + e);
            }

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
        out.println("Usage: [export/help/history/exit/quit]");
    }
}
