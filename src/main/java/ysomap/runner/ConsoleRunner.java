package ysomap.runner;

import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ysomap.console.ConsoleSession;
import ysomap.util.ColorStyle;
import ysomap.util.Strings;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
public class ConsoleRunner implements ObjectRunner{

    private List<String> prompt;

    private Completer commandCompleter =
            new StringsCompleter("help", "use", "set", "list", "show", "run","exit");


    @Override
    public void run(){
        Terminal terminal = null;
        LineReader lineReader = null;
        prompt = new LinkedList<>();
        Parser parser = new DefaultParser();
        ConsoleSession session = new ConsoleSession();
        try {
            prompt.add(ColorStyle.makeWordBoldAndUnderline("ysomap"));
            terminal = TerminalBuilder.builder()
                                    .system(true)
                                    .build();
            lineReader = LineReaderBuilder.builder()
                                    .terminal(terminal)
                                    .completer(commandCompleter)
                                    .parser(parser)
                                    .build();
            while (true) {
                List<String> words;
                try {
                    lineReader.readLine(makePrompt());
                    words = lineReader.getParsedLine().words();
                    session.accept(words, prompt);
                    System.out.println(words);
                } catch (UserInterruptException e) {
                    // Do nothing
                } catch (EndOfFileException e) {
                    System.out.println("\nBye.");
                    System.exit(0);
                } catch (Exception e) {
                    System.out.println("\n"+e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String makePrompt(){
        if(prompt != null){
            String temp = Strings.join(prompt, " ", "", "");
            return temp+" > ";
        }
        return "";
    }

    public static void main(String[] args) {
        new ConsoleRunner().run();
    }
}
