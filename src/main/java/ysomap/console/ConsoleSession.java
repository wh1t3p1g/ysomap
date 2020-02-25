package ysomap.console;

import org.jline.reader.EndOfFileException;
import ysomap.exception.ArgumentsMissMatchException;

import java.util.*;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConsoleSession {

    public String current;
    public String command;
    public List<String> args;
    public HashMap<String, Map<String, String>> settings;
    public HashMap<String,Session> sessions;
    public List<Session> running;
    public Map<String, String> prompt;// from ConsoleRunner

    public ConsoleSession() {
        args = new LinkedList<>();
        settings = new LinkedHashMap<>();
        settings.put("payload", new LinkedHashMap<>());
        settings.put("exploit", new LinkedHashMap<>());
        settings.put("bullet", new LinkedHashMap<>());
        sessions = new LinkedHashMap<>();
        running = new LinkedList<>();
    }

    public void accept(List<String> words, Map<String, String> prompt) throws Exception {
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
                clearAll();
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
            args = new LinkedList<>(words.subList(1, size));
            args.removeIf(String::isEmpty);
        }
    }

    public void clearAll(){
        sessions.clear();
        for(Map.Entry<String, Map<String,String>> item:settings.entrySet()){
            item.getValue().clear();
        }
        prompt.clear();
    }

    public void clear(String type){
        settings.get("bullet").clear();
        sessions.remove(type);
        prompt.remove(type);
    }


}
