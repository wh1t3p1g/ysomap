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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
public class ConsoleRunner implements ObjectRunner{

    private String ysomap = ColorStyle.makeWordBoldAndUnderline("ysomap");

    private Map<String,String> prompt;

    private Completer commandCompleter =
            new StringsCompleter("help", "use", "set", "list", "show", "run","exit");


    @Override
    public void run(){
        Terminal terminal = null;
        LineReader lineReader = null;
        prompt = new LinkedHashMap<>();
        Parser parser = new DefaultParser();
        ConsoleSession session = new ConsoleSession();
        try {
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
                    System.out.println(words);
                    session.accept(words, prompt);
                } catch (UserInterruptException e) {
                    // Do nothing
                } catch (EndOfFileException e) {
                    System.out.println("\nBye.");
                    System.exit(0);
                } catch (Exception e) {
                    if(e.getMessage() == null){
                        e.printStackTrace();
                    }else{
                        System.out.println("\n"+e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String makePrompt(){
        List<String> prompts = new LinkedList<>();
        prompts.add(ysomap);
        prompts.addAll(prompt.values());
        String temp = Strings.join(prompts, " ", "", "");
        return temp+" > ";
    }
}
