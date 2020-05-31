import org.jline.builtins.Completers;
import org.jline.builtins.Options;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Cursor;
import org.jline.terminal.MouseEvent;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static org.jline.builtins.Completers.TreeCompleter.node;

class CommandLineInterface<T> {
    String prompt;
    String rightPrompt = null;
    Character mask = null;
    String trigger = null;
    boolean color = false;
    boolean timer = false;
    TerminalBuilder builder = TerminalBuilder.builder();
    int mouse = 0;
    Completer completer = null;
    Parser parser = null;
    List<Consumer<LineReader>> callbacks = new ArrayList<>();
    Terminal terminal;
    LineReader reader;

    CommandLineInterface(String promptText, String[] args, String[] commands){

        if ((args == null) || (args.length == 0)) {
            usage();

            return;
        }

        prompt = promptText;

        int index = 0;
        label:
        while (args.length > index) {
            switch (args[index]) {
                    /* SANDBOX JANSI
                    case "-posix":
                        builder.posix(false);
                        index++;
                        break;
                    case "+posix":
                        builder.posix(true);
                        index++;
                        break;
                    case "-native-pty":
                        builder.nativePty(false);
                        index++;
                        break;
                    case "+native-pty":
                        builder.nativePty(true);
                        index++;
                        break;
                    */
                case "timer":
                    timer = true;
                    index++;
                    break;
                case "-system":
                    builder.system(false);
                    index++;
                    break;
                case "+system":
                    builder.system(true);
                    index++;
                    break;
                case "none":
                    break label;
                case "files":
                    completer = new Completers.FileNameCompleter();
                    break label;
                case "simple":
                    completer = new StringsCompleter(commands);
                    break label;
                case "quotes":
                    DefaultParser p = new DefaultParser();
                    p.setEofOnUnclosedQuote(true);
                    parser = p;
                    break label;
                case "brackets":
                    prompt = "long-prompt> ";
                    DefaultParser p2 = new DefaultParser();
                    p2.setEofOnUnclosedBracket(DefaultParser.Bracket.CURLY, DefaultParser.Bracket.ROUND, DefaultParser.Bracket.SQUARE);
                    parser = p2;
                    break label;
                case "status":
                    completer = new StringsCompleter(commands);
                    callbacks.add(reader -> {
                        new Thread(() -> {
                            int counter = 0;
                            while (true) {
                                try {
                                    Thread.sleep(1000);
                                    Status status = Status.getStatus(reader.getTerminal());
                                    counter++;
                                    status.update(Arrays.asList(new AttributedStringBuilder().append("counter: " + counter).toAttributedString()));
                                    ((LineReaderImpl) reader).redisplay();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    });
                    break label;
                case "foo":
                    completer = new ArgumentCompleter(
                            new StringsCompleter("foo11", "foo12", "foo13"),
                            new StringsCompleter("foo21", "foo22", "foo23"),
                            new Completer() {
                                @Override
                                public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
                                    candidates.add(new Candidate("", "", null, "frequency in MHz", null, null, false));
                                }
                            });
                    break label;
                case "param":
                    completer = (reader, line, candidates) -> {
                        if (line.wordIndex() == 0) {
                            candidates.add(new Candidate("Command1"));
                        } else if (line.words().get(0).equals("Command1")) {
                            if (line.words().get(line.wordIndex() - 1).equals("Option1")) {
                                candidates.add(new Candidate("Param1"));
                                candidates.add(new Candidate("Param2"));
                            } else {
                                if (line.wordIndex() == 1) {
                                    candidates.add(new Candidate("Option1"));
                                }
                                if (!line.words().contains("Option2")) {
                                    candidates.add(new Candidate("Option2"));
                                }
                                if (!line.words().contains("Option3")) {
                                    candidates.add(new Candidate("Option3"));
                                }
                            }
                        }
                    };
                    break label;
                case "tree":
                    completer = new Completers.TreeCompleter(
                            node("Command1",
                                    node("Option1",
                                            node("Param1", "Param2")),
                                    node("Option2"),
                                    node("Option3")));
                    break label;
                case "regexp":
                    Map<String, Completer> comp = new HashMap<>();
                    comp.put("C1", new StringsCompleter("cmd1"));
                    comp.put("C11", new StringsCompleter("--opt11", "--opt12"));
                    comp.put("C12", new StringsCompleter("arg11", "arg12", "arg13"));
                    comp.put("C2", new StringsCompleter("cmd2"));
                    comp.put("C21", new StringsCompleter("--opt21", "--opt22"));
                    comp.put("C22", new StringsCompleter("arg21", "arg22", "arg23"));
                    completer = new Completers.RegexCompleter("C1 C11* C12+ | C2 C21* C22+", comp::get);
                    break label;
                case "color":
                    color = true;
                    prompt = new AttributedStringBuilder()
                            .style(AttributedStyle.DEFAULT.background(AttributedStyle.GREEN))
                            .append("foo")
                            .style(AttributedStyle.DEFAULT)
                            .append("@bar")
                            .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                            .append("\nbaz")
                            .style(AttributedStyle.DEFAULT)
                            .append("> ").toAnsi();
                    rightPrompt = new AttributedStringBuilder()
                            .style(AttributedStyle.DEFAULT.background(AttributedStyle.RED))
                            .append(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                            .append("\n")
                            .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED | AttributedStyle.BRIGHT))
                            .append(LocalTime.now().format(new DateTimeFormatterBuilder()
                                    .appendValue(HOUR_OF_DAY, 2)
                                    .appendLiteral(':')
                                    .appendValue(MINUTE_OF_HOUR, 2)
                                    .toFormatter()))
                            .toAnsi();
                    completer = new StringsCompleter("\u001B[1mfoo\u001B[0m", "bar", "\u001B[32mbaz\u001B[0m", "foobar");
                    break label;
                case "mouse":
                    mouse = 1;
                    break label;
                case "mousetrack":
                    mouse = 2;
                    break label;
                default:
                    usage();
                    return;
            }
        }

        if (args.length == index + 2) {
            mask = args[index+1].charAt(0);
            trigger = args[index];
        }

        try {
            terminal = builder.build();

            reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(completer)
                    .parser(parser)
                    .variable(LineReader.SECONDARY_PROMPT_PATTERN, "%M%P > ")
                    .build();

            if (timer) {
                Executors.newScheduledThreadPool(1)
                        .scheduleAtFixedRate(() -> {
                            reader.callWidget(LineReader.CLEAR);
                            reader.getTerminal().writer().println("Hello world!");
                            reader.callWidget(LineReader.REDRAW_LINE);
                            reader.callWidget(LineReader.REDISPLAY);
                            reader.getTerminal().writer().flush();
                        }, 1, 1, TimeUnit.SECONDS);
            }
            if (mouse != 0) {
                reader.setOpt(LineReader.Option.MOUSE);
                if (mouse == 2) {
                    reader.getWidgets().put(LineReader.CALLBACK_INIT, () -> {
                        terminal.trackMouse(Terminal.MouseTracking.Any);
                        return true;
                    });
                    reader.getWidgets().put(LineReader.MOUSE, () -> {
                        MouseEvent event = reader.readMouseEvent();
                        StringBuilder tsb = new StringBuilder();
                        Cursor cursor = terminal.getCursorPosition(c -> tsb.append((char) c));
                        reader.runMacro(tsb.toString());
                        String msg = "          " + event.toString();
                        int w = terminal.getWidth();
                        terminal.puts(InfoCmp.Capability.cursor_address, 0, Math.max(0, w - msg.length()));
                        terminal.writer().append(msg);
                        terminal.puts(InfoCmp.Capability.cursor_address, cursor.getY(), cursor.getX());
                        terminal.flush();
                        return true;
                    });
                }
            }
            callbacks.forEach(c -> c.accept(reader));
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    void run(CommandsSelector commandsSelector, String[] commands, String prompt, T object) throws Exception {

        LineReader reader = setSimpleCompleter(commands);

        while (true) {
            String line = null;
            try {
                line = reader.readLine(prompt, rightPrompt, (MaskingCallback) null, null);
            } catch (UserInterruptException e) {
                // Ignore
            } catch (EndOfFileException e) {
                return;
            }
            if (line == null) {
                continue;
            }
            line = line.trim();
            if (color) {
                terminal.writer().println(
                        AttributedString.fromAnsi("\u001B[33m======>\u001B[0m\"" + line + "\"")
                                .toAnsi(terminal));
            } else {
                terminal.writer().println("======>\"" + line + "\"");
            }
            terminal.flush();
            // If we input the special word then we will mask
            // the next line.
            if ((trigger != null) && (line.compareTo(trigger) == 0)) {
                line = reader.readLine("password > ", mask);
            }
            if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                break;
            }
            ParsedLine pl = reader.getParser().parse(line, 0);
            String[] argv = pl.words().subList(1, pl.words().size()).toArray(new String[0]);
            try {
                commandsSelector.choose(this, pl, argv, reader, terminal, object);
            }catch (Options.HelpException e) {
                Options.HelpException.highlight(e.getMessage(), Options.HelpException.defaultStyle()).print(terminal);
            }
        }
    }

    LineReader setSimpleCompleter(String[] commands){
        Completer completer = new StringsCompleter(commands);
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .parser(parser)
                .variable(LineReader.SECONDARY_PROMPT_PATTERN, "%M%P > ")
                .build();
        return reader;
    }

    void setPrompt(String _prompt){ prompt = _prompt; }

    private static void usage() {
        System.out.println("Usage: java " + CommandLineInterface.class.getName()
                + " [none/simple/files/dictionary [trigger mask]]");
        System.out.println("  none - no completors");
        System.out.println("  simple - a simple completor that comples "
                + "\"foo\", \"bar\", and \"baz\"");
        System.out
                .println("  files - a completor that comples " + "file names");
        System.out.println("  classes - a completor that comples "
                + "java class names");
        System.out
                .println("  trigger - a special word which causes it to assume "
                        + "the next line is a password");
        System.out.println("  mask - is the character to print in place of "
                + "the actual password character");
        System.out.println("  color - colored prompt and feedback");
        System.out.println("\n  E.g - java Example simple su '*'\n"
                + "will use the simple completor with 'su' triggering\n"
                + "the use of '*' as a password mask.");
    }
}