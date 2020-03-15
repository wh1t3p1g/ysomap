package ysomap.console;

import org.apache.commons.lang3.StringUtils;
import org.jline.reader.EndOfFileException;
import org.reflections.Reflections;
import ysomap.annotation.Bullets;
import ysomap.annotation.Exploits;
import ysomap.annotation.Payloads;
import ysomap.exception.ArgumentsMissMatchException;
import ysomap.exception.SessionIDNotFoundException;
import ysomap.exception.YsoClassNotFoundException;
import ysomap.util.ColorStyle;
import ysomap.util.Logger;
import ysomap.util.OutputHelper;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConsoleSession {

    /**
     * 当前需要调用的命令，如set、use等
     */
    public String command;
    /**
     * 当前除command之外的参数
     */
    public List<String> args;
    /**
     * 将需要设置的参数，以类型区分，如bullet有bullet需要设置的参数，exploit有exploit要设置的参数
     */
    public HashMap<String, Map<String, String>> settings;
    /**
     * 当前存在的sessions，如当前的payload session的相关设置以ConsoleObjectSession对象为存储
     */
    public HashMap<String,Session> sessions;
    /**
     * 当前正在运行的session
     */
    public List<Session> running;
    /**
     * 每次的格式头
     */
    public Map<String, String> prompt;// from ConsoleRunner
    public Map<String, Class<?>> bullets;
    public Map<String, Class<?>> payloads;
    public Map<String, Class<?>> exploits;

    public ConsoleSession() {
        args = new LinkedList<>();
        settings = new LinkedHashMap<>();
        settings.put("payload", new LinkedHashMap<>());
        settings.put("exploit", new LinkedHashMap<>());
        settings.put("bullet", new LinkedHashMap<>());
        sessions = new LinkedHashMap<>();
        running = new LinkedList<>();
        Reflections reflections = new Reflections("ysomap.core");
        bullets = loadClass(reflections, Bullets.class);
        payloads = loadClass(reflections, Payloads.class);
        exploits = loadClass(reflections, Exploits.class);
        Logger.success("Load bullets<"+bullets.size()+"> " +
                        "payloads<"+payloads.size()+"> " +
                        "exploits<"+exploits.size()+">");
    }

    public void accept(List<String> words, Map<String, String> prompt) throws Exception {
        this.prompt = prompt;
        parse(words);
        switch (command){
            case "use":
                // use payload/exploit/bullet name
                use();
                break;
            case "set":
                // set key value, args from exploit or bullet
                set();
                break;
            case "list":
                // list exploits, bullets and payloads
                list();
                break;
            case "show":
                // show payload/bullet/exploit details
                show();
                break;
            case "run":
                // run current payload
                run();
                break;
            case "help":
                // help
                help();
                break;
            case "sessions":
                // print current running exploit sessions
                sessions();
                break;
            case "kill":
                // kill running exploit sessions
                kill();
                break;
            case "clear":
                // clear current sessions
                clearAll();
                break;
            case "":
                // do nothing
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
        if(settings.containsKey(type)){
            settings.get(type).clear();
        }
        sessions.remove(type);
        prompt.remove(type);
    }

    public void stopAllSessions(){
        for(Session session:running){
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        running.clear();
    }

    public void removeStoppedSessions(){
        List<Session> copy = new LinkedList<>(running);
        for(Session session: copy){
            if(session.isExit()){
                running.remove(session);
            }
        }
    }

    public Map<String, Class<?>> loadClass(Reflections reflections, Class<? extends Annotation> annotation){
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
        Map<String, Class<?>> retMap = new LinkedHashMap<>();
        for(Class<?> clazz: classes){
            retMap.put(clazz.getSimpleName(), clazz);
        }
        return retMap;
    }
//===============================================================
    // handle commands
    public void use() throws Exception {
        if(args.size() == 2){
            String type = args.get(0);
            String currentClazz = args.get(1);
            Session session = new ObjectSession(type);
            String promptStr = type+"("+ ColorStyle.makeWordRed(args.get(1))+")";
            Class<?> clazz = null;

            // update session data
            switch (type){
                case "bullet":
                    clear("bullet");
                    clazz = bullets.getOrDefault(currentClazz, null);
                    break;
                case "exploit":// exploit 为最高级 当重新选择exploit时，讲删除payload、exploit、bullet
                    clear("bullet");
                    clear("payload");
                    clear("exploit");
                    clazz = exploits.getOrDefault(currentClazz, null);
                    break;
                case "payload":// payload为次高级 当重新选择exploit时，将删除payload、bullet
                    // reset bullet
                    clear("bullet");
                    clear("payload");
                    clazz = payloads.getOrDefault(currentClazz, null);
                    break;
                default:
                    throw new ArgumentsMissMatchException("use [payload/exploit] [name]");
            }
            if(clazz == null){
                throw new YsoClassNotFoundException(type, currentClazz);
            }
            session.accept(clazz);
            prompt.put(type, promptStr);
            sessions.put(type, session);
            return;
        }
        throw new ArgumentsMissMatchException("use [payload/exploit] [name]");
    }

    public void set() throws Exception {
        if(args.size() == 2 && !args.get(0).equals("bullet")){
            for(Map.Entry<String, Session> item : sessions.entrySet()){// 从当前的session里获取
                if(item.getKey().equals("payload")){// payload session 不需要设置参数
                    continue;
                }

                String key = args.get(0);
                String value = args.get(1);
                Session session = item.getValue();
                if(session.has(key)){// 如果当前的session需要设置当前的参数
                    settings.get(item.getKey()).put(key, value);
                    session.set(key, value);
                    return;
                }
            }
            throw new ArgumentsMissMatchException("no key("+ColorStyle.makeWordRed(args.get(0))+") found");
        }
        throw new ArgumentsMissMatchException("set key value");
    }

    public void list() throws ArgumentsMissMatchException {
        if(args.size() == 1){
            String type = args.get(0);
            Logger.success("show all "+type);
            switch(type){
                case "exploits":
                    OutputHelper.printConsoleTable("Exploits", exploits.values());
                    return;
                case "payloads":
                    OutputHelper.printConsoleTable("Payload", payloads.values());
                    return;
                case "bullets":
                    OutputHelper.printConsoleTable("Bullets", bullets.values());
                    return;
            }
        }
        throw new ArgumentsMissMatchException("list [payloads/exploits/bullets]");
    }

    public void show() throws ArgumentsMissMatchException {
        if(args.size() == 1 && args.get(0).equals("options")){
            if(sessions.containsKey("exploit")){
                ObjectSession session = (ObjectSession) sessions.get("exploit");
                Logger.strongWarn("print exploit details:\n");
                OutputHelper.printEorBDetails(session.getClazz(), settings.get("exploit"));
                OutputHelper.printPorEDetails(session.getClazz(), "payload", false);
            }

            if(sessions.containsKey("payload")){
                ObjectSession session = (ObjectSession) sessions.get("payload");
                Logger.strongWarn("print payload details:\n");
                OutputHelper.printPorEDetails(session.getClazz(), "bullet", true);
            }

            if(sessions.containsKey("bullet")){
                Logger.strongWarn("print bullet details:\n");
                ObjectSession bullet = (ObjectSession) sessions.get("bullet");
                OutputHelper.printEorBDetails(bullet.getClazz(), settings.get("bullet"));
            }
        }else{
            throw new ArgumentsMissMatchException("show options");
        }
    }

    public void run() throws Exception {
        Object payloadObj = null;
        String payloadName = null;

        if(sessions.containsKey("payload")){
            Session bulletSession = sessions.get("bullet");
            Session payloadSession = sessions.get("payload");
            payloadSession.set("bullet", bulletSession.getObj());
            payloadSession.run();

            payloadObj = ((ObjectSession)payloadSession).getRetObj();
            payloadName = payloadSession.getObj().getClass().getSimpleName();
        }

        if(sessions.containsKey("exploit")){// multi threads to run exploit
            Session exploitSession = sessions.get("exploit");
            exploitSession.setAll();
            if(exploitSession.has("payload")){
                exploitSession.set("payload", payloadObj);
            }
            if(exploitSession.has("payloadName")){
                exploitSession.set("payloadName", payloadName);
            }

            running.add(exploitSession);// add to running sessions
            exploitSession.run();
        }
    }

    public void sessions(){
        // clear stopped sessions
        removeStoppedSessions();
        // print running sessions
        OutputHelper.printSessions(running);
    }

    public void kill() throws SessionIDNotFoundException {
        if(!args.isEmpty()){
            for(String s:args){
                if(s.equals("all")){
                    stopAllSessions();
                    Logger.success("killed all sessions");
                }else if(StringUtils.isNumeric(s)){
                    try {
                        running.get(Integer.parseInt(s)).close();
                        Logger.success("killed "+s);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.error("kill "+s+" failed");
                    }
                }else{
                    throw new SessionIDNotFoundException(s);
                }
            }
            removeStoppedSessions();
        }
    }

    public void help(){
        String usage = "help                print this message\n" +
                "list <type>         list exploits, bullets and payloads\n" +
                "use <type> <name>   choose a exploit/payload/bullet\n" +
                "set <key> <value>   set exploit/bullet's arguments\n" +
                "run                 run current session\n" +
                "show options        show payload/bullet/exploit details\n" +
                "clear               clear current sessions\n" +
                "sessions            print current running exploit sessions\n" +
                "kill <ids/all>      kill sessions, like 'kill 1 2 3' or 'kill all'\n" +
                "exit                exit ysomap\n";
        System.out.println(usage);
    }
}
