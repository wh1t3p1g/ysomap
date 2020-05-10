package ysomap.cli.runner;

import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ysomap.cli.console.ConsoleSession;
import ysomap.common.annotation.Require;
import ysomap.common.exception.BaseException;
import ysomap.common.util.ColorStyle;
import ysomap.common.util.Strings;

import java.io.IOException;
import java.util.*;

import static org.jline.builtins.Completers.TreeCompleter;
import static org.jline.builtins.Completers.TreeCompleter.node;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
public class ConsoleRunner implements ObjectRunner{

    private String ysomap = ColorStyle.makeWordBoldAndUnderline("ysomap");

    private Map<String,String> prompt;

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
                                    .completer(makeCompleters(session))
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
                } catch (BaseException e){
                    if(e.getMessage() == null){
                        e.printStackTrace();
                    }else{
                        System.out.println("\n"+e.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // 直接打印 防止某些应用返回的错误信息有用
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

    public Completer makeCompleters(ConsoleSession session){
        Completer useCompleter = new TreeCompleter(
                node("use",
                        node("payload",
                                node(new StringsCompleter(session.payloads.keySet()))
                        ),
                        node("exploit",
                                node(new StringsCompleter(session.exploits.keySet()))
                        ),
                        node("bullet",
                                node(new StringsCompleter(session.bullets.keySet()))
                        )
                )
        );
        Completer listCompleter = new TreeCompleter(
                node("list",
                        node("exploits","payloads","bullets")
                )
        );
        Completer showCompleter = new TreeCompleter(
                node("show",
                        node("options")
                )
        );

        Set<String> params = new HashSet<>();
        getParams(params, session.bullets);
        getParams(params, session.exploits);
        Completer setCompleter = new TreeCompleter(
                node("set",
                        node(params.toArray()))
        );

        Completer commonCompleter = new TreeCompleter(
                node("help","exit","run","sessions","kill"));
        return new AggregateCompleter(useCompleter, listCompleter, showCompleter, setCompleter, commonCompleter);
    }

    public void getParams(Set<String> params, Map<String, Class<?>> targets){
        for(Class<?> clazz:targets.values()){
            params.addAll(Require.Utils.getFieldNames(clazz));
        }
    }
}
