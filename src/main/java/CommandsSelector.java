import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.terminal.Terminal;

interface CommandsSelector<T> {

    void choose(CommandLineInterface cli, ParsedLine pl, String[] argv, LineReader reader, Terminal terminal, T object) throws Exception;
}
