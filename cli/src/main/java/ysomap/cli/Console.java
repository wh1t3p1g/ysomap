package ysomap.cli;

import org.apache.commons.io.FileUtils;
import org.jline.builtins.Completers;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.reflections.Reflections;
import ysomap.cli.model.MetaData;
import ysomap.cli.model.Status;
import ysomap.cli.utils.Printer;
import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.Exploits;
import ysomap.common.annotation.Payloads;
import ysomap.common.exception.ArgumentsMissMatchException;
import ysomap.common.exception.BaseException;
import ysomap.common.exception.YsoClassNotFoundException;
import ysomap.common.exception.YsoFileNotFoundException;
import ysomap.common.util.Logger;
import ysomap.core.serializer.SerializerTypeCodes;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

import static org.jline.builtins.Completers.TreeCompleter.node;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
public class Console {

    public Map<String, MetaData> exploits;
    public Map<String, MetaData> payloads;
    public Map<String, MetaData> bullets;

    private Session curSession;
    private Map<String, Session> sessions;

    private String command;
    private List<String> args;
    private String prompt;

    public void init(){
        sessions = new HashMap<>();
        prompt = Status.YSOMAP + " > ";
        exploits = loadMetaData("ysomap.exploits", Exploits.class);
        payloads = loadMetaData("ysomap.payloads", Payloads.class);
        bullets = loadMetaData("ysomap.bullets", Bullets.class);
        Logger.success("exploits("+exploits.values().size()+") payloads("+payloads.values().size()+") bullets("+bullets.values().size()+")");
    }

    public void run(){
        Terminal terminal = null;
        LineReader lineReader = null;
        Parser parser = new DefaultParser();

        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();
            lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(makeCompleters())
                    .parser(parser)
                    .build();
            curSession = newSession(true);
            while (true) {
                List<String> words;
                try {
                    lineReader.readLine(prompt);
                    words = lineReader.getParsedLine().words();
                    dispatch(words);
                    prompt = curSession.getStatus().makePrompt();
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

    public void dispatch(List<String> words) throws Exception {
        parse(words);
        switch (command){
            case "use":
                use();
                break;
            case "set":
                set();
                break;
            case "list":
                list();
                break;
            case "show":
                show();
                break;
            case "sessions":
                sessions();
                break;
            case "session":
                session();
                break;
            case "kill":
                kill();
                break;
            case "script":
                script();
                break;
            case "run":
                if(curSession != null){
                    curSession.run();
                }
                break;
            case "stop":
                Logger.success("Stop current session!");
                if(curSession != null){
                    curSession.close();
                }
                break;
            case "":
                //do nothing
                break;
            case "clear":
                Logger.success("Clear all settings on current session.");
                if(curSession != null){
                    curSession.clear();
                }
                break;
            case "help":
                help();
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
        command = "";
        args = new LinkedList<>();
        int size = words.size();
        if(size == 1){
            command = words.get(0);
        }else if(size > 1){
            command = words.get(0);
            args = new LinkedList<>(words.subList(1, size));
            args.removeAll(Arrays.asList("", null));
        }
    }

    public Class<?> getClassFromLoadedClasses(String type, String name) throws ArgumentsMissMatchException {
        MetaData metaData = null;
        if("exploit".equals(type)) {
            metaData = exploits.get(name);
        } else if ("payload".equals(type)) {
            metaData = payloads.get(name);
        } else if ("bullet".equals(type)) {
            metaData = bullets.get(name);
        } else {
            throw new ArgumentsMissMatchException("use [payload/exploit] [name]");
        }
        if(metaData != null){
            return metaData.getClazz();
        }
        return null;
    }

    public Map<String, MetaData> loadMetaData(String classpath, Class<? extends Annotation> annotation){
        Reflections reflections = new Reflections(classpath);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
        Map<String, MetaData> retMap = new LinkedHashMap<>();
        for(Class<?> clazz: classes){
            retMap.put(clazz.getSimpleName(), new MetaData(clazz, annotation));
        }
        return retMap;
    }

    public Completer makeCompleters(){
        Completer useCompleter = new Completers.TreeCompleter(
                node("use",
                        node("payload",
                                node(new StringsCompleter(payloads.keySet()))
                        ),
                        node("exploit",
                                node(new StringsCompleter(exploits.keySet()))
                        ),
                        node("bullet",
                                node(new StringsCompleter(bullets.keySet()))
                        )
                )
        );
        Completer listCompleter = new Completers.TreeCompleter(
                node("list",
                        node("exploits","payloads","bullets")
                )
        );
        Completer showCompleter = new Completers.TreeCompleter(
                node("show",
                        node("options", "payloads", "bullets")
                )
        );

        Completer sessionCompleter = new Completers.TreeCompleter(
                node("session",
                        node("create", "insert")
                )
        );

        Set<String> params = getAllParams();
        List<String> serializeTypes =
                Arrays.asList(SerializerTypeCodes.getAllSerializerType());
        List<String> encoders = Arrays.asList("base64");
        List<String> output = Arrays.asList("file", "console");

        Completer setCompleter = new Completers.TreeCompleter(
                node("set",
                        node("checkRunning", node(new StringsCompleter("true", "false"))),
                        node("serializeType", node(new StringsCompleter(serializeTypes))),
                        node("encoder", node(new StringsCompleter(encoders))),
                        node("output", node(new StringsCompleter(output))),
                        node("serialVersionUID"), // set serialVersionUID oldUID1:newUID1;oldUID2:newUID2
                        node(params.toArray())
                )
        );

        Completer commonCompleter = new Completers.TreeCompleter(
                node("help","exit","run","sessions","kill","stop","script"));
        return new AggregateCompleter(useCompleter, listCompleter, showCompleter, setCompleter, sessionCompleter,commonCompleter);
    }

    public Set<String> getAllParams(){
        Set<String> params = new HashSet<>();
        for(MetaData data:exploits.values()){
            params.addAll(data.getFields());
        }
        for(MetaData data:bullets.values()){
            params.addAll(data.getFields());
        }
        return params;
    }

    public Session newSession(boolean toSessions){
        if(curSession != null && curSession.isEmpty()) return curSession;
        Session session = new Session(this);
        if(toSessions){
            sessions.put(session.getUuid(), session);
        }
        return session;
    }

    //===============================================================
    // handle commands
    public void use() throws Exception {
        if(args.size() == 2){
            String type = args.get(0);
            Class<?> clazz = getClassFromLoadedClasses(type, args.get(1));

            if(clazz == null){
                throw new YsoClassNotFoundException(type, args.get(1));
            }

            if("exploit".equals(type)){
                Logger.success("Create a new session.");
                curSession = newSession(true);
            }else if("payload".equals(type) && (curSession == null || !curSession.isExploit())){
                Logger.success("Create a new session.");
                curSession = newSession(true);
            }

            curSession.create(type, clazz);
            curSession.getStatus().addPrompt(type, args.get(1));
        }else{
            throw new ArgumentsMissMatchException("use [payload/exploit] [name]");
        }
    }

    public void set() throws Exception {
        if(args.size() == 2){
            curSession.setValue(args.get(0), args.get(1));
            return;
        }
        throw new ArgumentsMissMatchException("set key value");
    }

    public void list() throws ArgumentsMissMatchException {
        if(args.size() == 1){
            String type = args.get(0);
            switch(type){
                case "exploits":
                    Printer.printExploitsInfo(exploits.values());
                    return;
                case "payloads":
                    Printer.printPayloadsInfo(payloads.values());
                    return;
                case "bullets":
                    Printer.printBulletsInfo(bullets.values());
                    return;
            }
        }
        throw new ArgumentsMissMatchException("list [payloads/exploits/bullets]");
    }

    public void show() throws ArgumentsMissMatchException {
        if(args.size() == 1){
            String type = args.get(0);
            if("options".equals(type)){
                curSession.printSessionDetails();
                return;
            }else if("payloads".equals(type) || "bullets".equals(type)){
                curSession.printCandidates(type);
                return;
            }
        }
        throw new ArgumentsMissMatchException("show [options|payloads|bullets]");
    }

    public void session() throws ArgumentsMissMatchException {
        if(args.size() == 2) {
            String uuid = args.get(1);
            Logger.success("Recover to session {" + uuid + "}");
            if (sessions.containsKey(uuid)) {
                curSession = sessions.get(uuid);
                Logger.success("Success!");
            } else {
                Logger.error("Session {" + uuid + "} not found, recover failed!");
            }
        }else if(args.size() == 1){// new a session
            Logger.success("Create a new session.");
            curSession = newSession(true);
        }else{
            throw new ArgumentsMissMatchException("session create | session insert <uuid>");
        }
    }

    public void sessions(){
        // print all sessions
        Printer.printSessions(curSession.getUuid(), sessions);
    }

    public void kill() throws ArgumentsMissMatchException {
        if(args.size() == 1){
            String uuid = args.get(0);
            if("all".equals(uuid)){
                for(Session session:sessions.values()){
                    session.close();
                }
                sessions.clear();
            }else if(sessions.containsKey(uuid)){
                Session session = sessions.get(uuid);
                session.close();
                sessions.remove(uuid);
            }else{
                Logger.error("Session {"+uuid+"} not found, kill session failed!");
            }

        }else{
            throw new ArgumentsMissMatchException("kill [uuid|all]");
        }
    }

    public void script() throws Exception {
        if(args.size() == 1){
            String filepath = args.get(0);
            File file = new File(filepath);
            if(!file.exists()){
                throw new YsoFileNotFoundException(filepath);
            }
            Logger.success("Start to parse script file{"+filepath+"}");
            List<String> contents = FileUtils.readLines(file);
            Parser parser = new DefaultParser();
            ParsedLine parsedLine = null;
            List<String> words;
            for(String line:contents){
                if(line.startsWith("#"))continue;
                parsedLine = parser.parse(line, line.length()+1, Parser.ParseContext.ACCEPT_LINE);
                words = parsedLine.words();
                dispatch(words);
            }
            Logger.success("Script loaded!");
        }else{
            throw new ArgumentsMissMatchException("script /path/to/script");
        }
    }

    public void help(){
        String usage = "help                print this message\n" +
                "list <type>         list exploits, bullets and payloads\n" +
                "use <type> <name>   choose a exploit/payload/bullet\n" +
                "set <key> <value>   set exploit/bullet's arguments\n" +
                "run                 run current session\n" +
                "show <type>         show payload/bullet/exploit details\n" +
                "clear               clear current sessions\n" +
                "session [c|i]       recover to a session or create a new session\n" +
                "sessions            print current running exploit sessions\n" +
                "stop                stop current session\n" +
                "kill [uuid|all]     kill sessions, like 'kill uuid' or 'kill all'\n" +
                "exit                exit ysomap\n";
        System.out.println(usage);
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
