package ysomap.console;

import ysomap.exception.ArgumentsMissMatchException;
import ysomap.util.ColorStyle;
import ysomap.util.Logger;
import ysomap.util.OutputHelper;
import ysomap.util.enums.BulletEnums;
import ysomap.util.enums.ExploitEnums;
import ysomap.util.enums.PayloadEnums;

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
            Session session = new ConsoleObjectSession(type);
            String promptStr = type+"("+ ColorStyle.makeWordRed(cs.args.get(1))+")";
            session.accept(cs.args.get(1));
            // update session data
            switch (type){
                case "bullet":
                case "exploit":
                case "payload":
                    // reset bullet
                    cs.clear("bullet");
                    cs.prompt.put(type, promptStr);
                    break;
                default:
                    throw new ArgumentsMissMatchException("use [payload/exploit] [name]");
            }
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

        if(cs.sessions.containsKey("payload")){
            Session payloadSession = cs.sessions.get("payload");
            Session bulletSession = cs.sessions.get("bullet");
            payloadSession.set("bullet", bulletSession.getObj());
            payloadSession.run();
            payloadObj = ((ConsoleObjectSession)payloadSession).getRetObj();
            Logger.success("* run payload success");
        }

        if(cs.sessions.containsKey("exploit")){// multi threads to run exploit
            Session exploitSession = cs.sessions.get("exploit");
            if(exploitSession.has("payload")){
                exploitSession.set("payload", payloadObj);
            }
            cs.running.add(exploitSession);// add to running sessions
            exploitSession.run();
            Logger.success("* run exploit success");
        }
    }

    public static void sessions(ConsoleSession cs){

    }

    public static void help(){
        String usage = "help                print this message\n" +
                "list <type>         list exploits, bullets and payloads\n" +
                "use <type> <name>   choose a exploit/payload/bullet\n" +
                "set <key> <value>   set exploit/bullet's arguments\n" +
                "run                 run current session\n" +
                "show <type>         show payload/bullet/exploit details\n" +
                "clear               clear current sessions\n" +
                "exit                exit ysomap\n";
        System.out.println(usage);
    }

}
