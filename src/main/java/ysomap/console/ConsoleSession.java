package ysomap.console;

import org.jline.reader.EndOfFileException;
import ysomap.exception.ArgumentsMissMatchException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConsoleSession {

    public String current;
    public String command;
    public List<String> args;
    public HashMap<String, String> settings;
    public HashMap<String,Session> sessions;
    public List<Session> running;
    public List<String> prompt;// from ConsoleRunner

    public ConsoleSession() {
        settings = new LinkedHashMap<>();
        sessions = new LinkedHashMap<>();
        running = new LinkedList<>();
    }

    public void accept(List<String> words, List<String> prompt) throws Exception {
        this.prompt = prompt;
        parse(words);
        switch (command){
            case "use":
                // use payload/exploit/bullet name
                ConsoleHandler.use(this);
                break;
            case "set":
                // set key value, args from exploit or bullet
                ConsoleHandler.set(this);
                break;
            case "list":
                // list exploits, bullets and payloads
                ConsoleHandler.list(this);
                break;
            case "show":
                // show payload/bullet/exploit details
                ConsoleHandler.show(this);
                break;
            case "run":
                // run current payload
                ConsoleHandler.run(this);
                break;
            case "help":
                // help
                ConsoleHandler.help();
                break;
            case "sessions":
                // print current running exploit sessions
                ConsoleHandler.sessions(this);
                break;
            case "clear":
                // clear current sessions
                clear();
                break;
            case "exit":
                // exit ysomap
                throw new EndOfFileException();
            default:
                // unknown command
                throw new ArgumentsMissMatchException("help");
        }
    }

    public void parse(List<String> words){
        int size = words.size();
        if(size == 1){
            command = words.get(0);
        }else if(size > 1){
            command = words.get(0);
            args = words.subList(1, size);
        }
    }

    public void clear(){
        sessions.clear();
        settings.clear();
        prompt.clear();
    }




}
