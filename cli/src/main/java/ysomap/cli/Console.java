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
import ysomap.common.annotation.Require;
import ysomap.common.exception.ArgumentsMissMatchException;
import ysomap.common.exception.BaseException;
import ysomap.common.exception.YsoClassNotFoundException;
import ysomap.common.exception.YsoFileNotFoundException;
import ysomap.common.util.ColorStyle;
import ysomap.common.util.Logger;
import ysomap.core.serializer.SerializerTypeCodes;
import ysomap.core.util.DetailHelper;
import ysomap.core.util.FileHelper;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private static String version = "v0.1.5";

    public void init(){
        sessions = new HashMap<>();
        prompt = Status.YSOMAP + " > ";
        exploits = loadMetaData("ysomap.exploits", Exploits.class);
        payloads = loadMetaData("ysomap.payloads", Payloads.class);
        bullets = loadMetaData("ysomap.bullets", Bullets.class);
        Logger.success("version: "+version+", exploits("+exploits.values().size()+") payloads("+payloads.values().size()+") bullets("+bullets.values().size()+")");
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
            case "search":
                search();
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
            case "dump":
                dump();
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
            throw new ArgumentsMissMatchException("use {exploit/payload/bullet} <name>");
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
        Completer searchCompleter = new Completers.TreeCompleter(
                node("search",
                        node("exploits", "payloads", "bullets")
                        
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
        return new AggregateCompleter(useCompleter, listCompleter, searchCompleter, showCompleter, setCompleter, sessionCompleter,commonCompleter);
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
        String type = null;
        String clazzString = null;
        Class<?> clazz = null;
        if (args.size() == 1) {   // 可直接使用 use xxx 来调用所有的exploit、payload、bullet
            clazzString = args.get(0);
            type = "exploit";
            clazz = getClassFromLoadedClasses(type, clazzString);
            
            if (clazz == null) {
                type = "payload";
                clazz = getClassFromLoadedClasses(type, clazzString);
            }
            
            if (clazz == null) {
                type = "bullet";
                clazz = getClassFromLoadedClasses(type, clazzString);
            }
            
        } else if (args.size() == 2) { // 保留原来的方式，use {exploit/payload/bullet} xxx
            type = args.get(0);
            clazzString = args.get(1);
            clazz = getClassFromLoadedClasses(type, clazzString);
        } else {
            throw new ArgumentsMissMatchException("use {payload/exploit/bullet} <name> or use <name>");
        }
        
        if (clazz == null) {
            throw new YsoClassNotFoundException(type, clazzString);
        }
        
        if ("exploit".equals(type)) {
            Logger.success("Create a new session.");
            curSession = newSession(true);
        } else if ("payload".equals(type) && (curSession == null || !curSession.isExploit())) {
            Logger.success("Create a new session.");
            curSession = newSession(true);
        }
        
        curSession.create(type, clazz);
        curSession.getStatus().addPrompt(type, clazzString);
        
        if ("exploit".equals(type)){
            Printer.printCandidates("payloads", clazz, false, null); // use exploit 后自动打印可用 payloads
            autoSetPayloadOrBullet("payload", clazz);   // 自动调用 payload
        } else if ("payload".equals(type)) {
            Printer.printCandidates("bullets", clazz, false, null); // use payload 后自动打印可用 bullets
            autoSetPayloadOrBullet("bullet", clazz);   // 自动调用 bullet
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
        if (args.isEmpty()) {
            Printer.printExploitsInfo(exploits.values());
            Printer.printPayloadsInfo(payloads.values());
            Printer.printBulletsInfo(bullets.values());
            return;
        } else if (args.size() == 1) {
            String type = args.get(0);
            switch (type) {
                case "exploit":
                case "exploits":
                    Printer.printExploitsInfo(exploits.values());
                    return;
                case "payload":
                case "payloads":
                    Printer.printPayloadsInfo(payloads.values());
                    return;
                case "bullet":
                case "bullets":
                    Printer.printBulletsInfo(bullets.values());
                    return;
            }
        }
        throw new ArgumentsMissMatchException("list {payloads/exploits/bullets}");
    }

    public void dump(){
        if(args.isEmpty()){
            args.add("dumped.yso");
        }

        if(args.size() == 1 && curSession != null){
            StringBuilder sb = new StringBuilder();
            if(curSession.exploit != null){
                sb.append("# exploit settings\n");
                sb.append(curSession.exploit.dump());
                Map<String, String> parameters = curSession.exploit.getAllParameters();

                for(Map.Entry<String, String> entry:parameters.entrySet()){
                    sb.append(String.format("set %s %s\n", entry.getKey(), entry.getValue()));
                }
            }

            if(curSession.payload != null){
                if(curSession.bullet != null){
                    curSession.payload.setBullet(curSession.bullet);
                }
                sb.append("\n");
                sb.append("# payload settings\n");
                sb.append(curSession.payload.dump());
                sb.append("# serializer settings\n");
                sb.append(String.format("set serializeType %s\n", curSession.payload.getSerializeType()));
                sb.append(String.format("set encoder %s\n", curSession.payload.getEncoder()));
                sb.append(String.format("set output %s\n", curSession.payload.getOutputType()));
                sb.append(String.format("set serialVersionUID %s\n", curSession.payload.getSerialVersionUID()));
                sb.append(String.format("set checkRunning %s\n", curSession.payload.getCheckRunning()));
                Map<String, String> parameters = curSession.payload.getAllParameters();
                sb.append("# bullet settings\n");
                for(Map.Entry<String, String> entry:parameters.entrySet()){
                    sb.append(String.format("set %s %s\n", entry.getKey(), entry.getValue()));
                }
            }

            if(sb.toString().isEmpty()){
                Logger.normal("Nothing to dump.");
                return;
            }

            sb.append("# start to run\n");
            sb.append("run\n");
            try {
                FileHelper.filePutContent(args.get(0), sb.toString().getBytes());
                Logger.success(String.format("Dump settings to '%s'.", args.get(0)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void search() throws ArgumentsMissMatchException {
        String tips = "search <keyword>\n"+
                "search {payload/exploit/bullet} <keyword>\n";
        if (args.size() == 1) {  // list keyword 全局搜索
            String keyword = args.get(0);
            if ("-h".equals(keyword) || "help".equals(keyword)){
                Logger.normal(tips);
                return;
            }
            Printer.printExploitsInfo(getFilterList(exploits, keyword));
            Printer.printPayloadsInfo(getFilterList(payloads, keyword));
            Printer.printBulletsInfo(getFilterList(bullets, keyword));
            return;
        } else if (args.size() == 2) {  // search exploit/payload/bullet <keyword>
            String type = args.get(0);
            String keyword = args.get(1);
    
            switch (type) {
                case "exploit":
                case "exploits":
                    Printer.printExploitsInfo(getFilterList(exploits, keyword));
                    return;
                case "payload":
                case "payloads":
                    Printer.printPayloadsInfo(getFilterList(payloads, keyword));
                    return;
                case "bullet":
                case "bullets":
                    Printer.printBulletsInfo(getFilterList(bullets, keyword));
                    return;
            }
        }
        
        throw new ArgumentsMissMatchException(tips);
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
    
    public void help() {
        String usage =
                        "help                    print this message\n" +
                        "list [type]             list exploits, bullets and payloads\n" +
                        "use <type> <name>       choose a exploit/payload/bullet\n" +
                        "set <key> <value>       set exploit/bullet's arguments\n" +
                        "run                     run current session\n" +
                        "exploit                 same as the run command\n" +
                        "search                  search exploit/payload/bullet/ keyword\n" +
                        "show <type>             show payload/bullet/exploit details\n" +
                        "clear                   clear current sessions\n" +
                        "session {c|i}           recover to a session or create a new session\n" +
                        "sessions                print current running exploit sessions\n" +
                        "stop                    stop current session\n" +
                        "dump /path/to/yso       dump current session's parameters\n" +
                        "script /path/to/yso     load a yso script\n" +
                        "kill {uuid|all}         kill sessions, like 'kill uuid' or 'kill all'\n" +
                        "exit                    exit ysomap\n";
        System.out.println(usage);
    }
    
    // 如果可选项仅有一个，那么自动设置payload或bullet
    public void autoSetPayloadOrBullet(String type, Class<?> clazz) throws Exception {
        List<String> candidates = Arrays.asList(Require.Utils.getRequiresFromClass(clazz));
        if(candidates.size() != 1) return;

        String candidate = candidates.get(0);
        if(DetailHelper.ALL_PAYLOAD.equals(candidate)
                || DetailHelper.ALL_JAVA_PAYLOAD.equals(candidate)
                || DetailHelper.ALL_HESSIAN_PAYLOAD.equals(candidate)
                || DetailHelper.NO_NEED_PAYLOAD.equals(candidate)
                || candidate.isEmpty()
        ) return;

        Logger.normal(
                String.format("Auto set %s [%s]", type,
                        ColorStyle.makeWordRedAndBoldAndUnderline(candidate)));

        List<String> list = new ArrayList<>();
        list.add(type);
        list.add(candidate);
        args = list;
        use();
    }
    
    // 过滤筛选
    public Set<MetaData> getFilterList(Map<String, MetaData> type, String keyword) {
        Stream<MetaData> metaDataStream = type.values().parallelStream().filter(x -> x.getSimpleName().toLowerCase().contains(keyword.toLowerCase()));
        Set<MetaData> filteredList = metaDataStream.collect(Collectors.toSet());
        return filteredList;
    }
    
    public void setArgs(List<String> args) {
        this.args = args;
    }
}
