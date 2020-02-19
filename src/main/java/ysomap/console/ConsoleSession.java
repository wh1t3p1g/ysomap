package ysomap.console;

import org.jline.reader.EndOfFileException;
import ysomap.exception.ArgumentsMissMatchException;
import ysomap.util.ColorStyle;
import ysomap.util.OutputHelper;
import ysomap.util.enums.BulletEnums;
import ysomap.util.enums.ExploitEnums;
import ysomap.util.enums.PayloadEnums;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConsoleSession {

    private String current;
    private String command;
    private List<String> args;
    private HashMap<String,Session> sessions;
    private List<String> prompt;

    public ConsoleSession() {
    }

    public void accept(List<String> words, List<String> prompt) throws Exception {
        this.prompt = prompt;
        parse(words);
        switch (command){
            case "help":
                // help
                parseHelp();
                break;
            case "use":
                // use payload/exploit name
                parseUse();
                break;
            case "set":
                // set key value, args
                parseSet();
                break;
            case "list":
                // list exploits, bullets and payloads
                parseList();
                break;
            case "run":
                // run current payload
                run();
                break;
            case "show":
                // show payload/bullet details
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

        }
    }

    public void parseUse() throws Exception {
        if(args.size() == 2){
            Session session = new ConsoleObjectSession(args.get(0));
            if(!args.get(0).equals("bullet")){// new exploit or payload
                clear();
                prompt.add(args.get(0)+"("+ ColorStyle.makeWordRed(args.get(1))+")");
            }
            session.accept(args.get(1));
            sessions.put(args.get(0), session);
            return;
        }
        throw new ArgumentsMissMatchException("use [payload/exploit] [name]");
    }

    public void parseList() throws ArgumentsMissMatchException {
        if(args.size() == 1){
            String type = args.get(0);
            System.out.println("* show all "+type);
            switch(type){
                case "exploits":
                    OutputHelper.printConsoleTable("Exploits", ExploitEnums.values());
                    return;
                case "payloads":
                    OutputHelper.printConsoleTable("Payload", PayloadEnums.values());
                    return;
                case "bullets":
                    OutputHelper.printConsoleTable("Bullets", BulletEnums.values());
                    return;
            }
        }
        throw new ArgumentsMissMatchException("list [payloads/exploits/bullets]");
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

    public void clearPrompt(){
        if(prompt.size() == 2){
            prompt.remove(1);
        }
    }

    public void clear(){
        System.out.println("* clear current sessions");
        clearPrompt();
        sessions = new HashMap<>();
    }

    public void run() throws Exception {
        if(sessions.containsKey("exploit")){
            sessions.get("exploit").run();
        }else if(sessions.containsKey("payloads")){

        }
    }

    public void parseSet() throws Exception {
        if(args.size() == 2){
            for(Map.Entry<String, Session> item : sessions.entrySet()){
                if(item.getValue().has(args.get(0))){
                    item.getValue().set(args.get(0),args.get(1));
                    return;
                }
            }
            throw new ArgumentsMissMatchException("no key("+ColorStyle.makeWordRed(args.get(0))+") found");
        }
        throw new ArgumentsMissMatchException("set key value");
    }

    public void parseHelp(){
        String usage = "help                print this message\n" +
                "list <type>         list exploits, bullets and payloads\n" +
                "use <type> <name>   choose a exploit/payload\n" +
                "set <key> <value>   set exploit/payload's arguments\n" +
                "run                 run current session\n" +
                "show <type>         show payload/bullet details\n" +
                "clear               clear current sessions\n" +
                "exit                exit ysomap\n";
        System.out.println(usage);
    }

    public static void main(String[] args) {
        List<String> words = new LinkedList<>();
        words.add("use");
        words.add("exploit");
        words.add("SimpleHTTPServer");
        List<String> prompt = new LinkedList<>();
        prompt.add("ysomap");
        try {
            new ConsoleSession().accept(words, prompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
