package ysomap.console;

import org.apache.commons.lang3.StringUtils;
import ysomap.exception.ArgumentsMissMatchException;
import ysomap.exception.SessionIDNotFoundException;
import ysomap.exception.YsoClassNotFoundException;
import ysomap.util.ColorStyle;
import ysomap.util.Logger;
import ysomap.util.OutputHelper;

import java.util.Map;

/**
 * @author wh1t3P1g
 * @since 2020/2/20
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ConsoleHandler {

    public static void use(ConsoleSession cs) throws Exception {
        if(cs.args.size() == 2){
            String type = cs.args.get(0);
            String currentClazz = cs.args.get(1);
            Session session = new ConsoleObjectSession(type);
            String promptStr = type+"("+ ColorStyle.makeWordRed(cs.args.get(1))+")";
            Class<?> clazz = null;

            // update session data
            switch (type){
                case "bullet":
                    cs.clear("bullet");
                    clazz = cs.bullets.getOrDefault(currentClazz, null);
                    break;
                case "exploit":// exploit 为最高级 当重新选择exploit时，讲删除payload、exploit、bullet
                    cs.clear("bullet");
                    cs.clear("payload");
                    cs.clear("exploit");
                    clazz = cs.exploits.getOrDefault(currentClazz, null);
                    break;
                case "payload":// payload为次高级 当重新选择exploit时，将删除payload、bullet
                    // reset bullet
                    cs.clear("bullet");
                    cs.clear("payload");
                    clazz = cs.payloads.getOrDefault(currentClazz, null);
                    break;
                default:
                    throw new ArgumentsMissMatchException("use [payload/exploit] [name]");
            }
            if(clazz == null){
                throw new YsoClassNotFoundException(type, currentClazz);
            }
            session.accept(clazz);
            cs.prompt.put(type, promptStr);
            cs.sessions.put(type, session);
            return;
        }
        throw new ArgumentsMissMatchException("use [payload/exploit] [name]");
    }

    public static void set(ConsoleSession cs) throws Exception {
        if(cs.args.size() == 2 && !cs.args.get(0).equals("bullet")){
            for(Map.Entry<String, Session> item : cs.sessions.entrySet()){
                String key = cs.args.get(0);
                String value = cs.args.get(1);
                Session session = item.getValue();
                if(session.has(key)){
                    cs.settings.get(item.getKey()).put(key, value);
                    session.set(key, value);
                    return;
                }
            }
            throw new ArgumentsMissMatchException("no key("+ColorStyle.makeWordRed(cs.args.get(0))+") found");
        }
        throw new ArgumentsMissMatchException("set key value");
    }

    public static void list(ConsoleSession cs) throws ArgumentsMissMatchException {
        if(cs.args.size() == 1){
            String type = cs.args.get(0);
            Logger.success("* show all "+type);
            switch(type){
                case "exploits":
                    OutputHelper.printConsoleTable("Exploits", cs.exploits.values());
                    return;
                case "payloads":
                    OutputHelper.printConsoleTable("Payload", cs.payloads.values());
                    return;
                case "bullets":
                    OutputHelper.printConsoleTable("Bullets", cs.bullets.values());
                    return;
            }
        }
        throw new ArgumentsMissMatchException("list [payloads/exploits/bullets]");
    }

    public static void show(ConsoleSession cs) throws ArgumentsMissMatchException {
        if(cs.args.size() == 1 && cs.args.get(0).equals("options")){
            if(cs.sessions.containsKey("exploit")){
                ConsoleObjectSession session = (ConsoleObjectSession) cs.sessions.get("exploit");
                Logger.strongWarn("* print exploit details:\n");
                OutputHelper.printEorBDetails(session.getClazz(), cs.settings.get("exploit"));
                OutputHelper.printPorEDetails(session.getClazz(), "payload", false);
            }

            if(cs.sessions.containsKey("payload")){
                ConsoleObjectSession session = (ConsoleObjectSession) cs.sessions.get("payload");
                Logger.strongWarn("* print payload details:\n");
                OutputHelper.printPorEDetails(session.getClazz(), "bullet", true);
            }

            if(cs.sessions.containsKey("bullet")){
                Logger.strongWarn("* print bullet details:\n");
                ConsoleObjectSession bullet = (ConsoleObjectSession) cs.sessions.get("bullet");
                OutputHelper.printEorBDetails(bullet.getClazz(), cs.settings.get("bullet"));
            }
        }else{
            throw new ArgumentsMissMatchException("show options");
        }
    }

    public static void run(ConsoleSession cs) throws Exception {
        Object payloadObj = null;
        String payloadName = null;

        if(cs.sessions.containsKey("payload")){
            Session payloadSession = cs.sessions.get("payload");
            Session bulletSession = cs.sessions.get("bullet");
            payloadSession.set("bullet", bulletSession.getObj());
            payloadSession.run();
            payloadObj = ((ConsoleObjectSession)payloadSession).getRetObj();
            payloadName = payloadSession.getObj().getClass().getSimpleName();
        }

        if(cs.sessions.containsKey("exploit")){// multi threads to run exploit
            Session exploitSession = cs.sessions.get("exploit");
            if(exploitSession.has("payload")){
                exploitSession.set("payload", payloadObj);
            }
            if(exploitSession.has("payloadName")){
                exploitSession.set("payloadName", payloadName);
            }

            cs.running.add(exploitSession);// add to running sessions
            exploitSession.run();
            Logger.success("* exploit is running");
        }
    }

    public static void sessions(ConsoleSession cs){
        // clear stopped sessions
        cs.removeStoppedSessions();
        // print running sessions
        OutputHelper.printSessions(cs.running);
    }

    public static void kill(ConsoleSession cs) throws SessionIDNotFoundException {
        if(!cs.args.isEmpty()){
            for(String s:cs.args){
                if(s.equals("all")){
                    cs.stopAllSessions();
                    Logger.success("* killed all sessions");
                }else if(StringUtils.isNumeric(s)){
                    try {
                        cs.running.get(Integer.parseInt(s)).close();
                        Logger.success("* killed "+s);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.error("[-] kill "+s+" failed");
                    }
                }else{
                    throw new SessionIDNotFoundException(s);
                }
            }
            cs.removeStoppedSessions();
        }
    }

    public static void help(){
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
