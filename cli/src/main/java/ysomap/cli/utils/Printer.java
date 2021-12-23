package ysomap.cli.utils;

import de.vandermeer.asciitable.AT_ColumnWidthCalculator;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_FixedWidth;
import de.vandermeer.asciitable.CWC_LongestLine;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import ysomap.cli.Session;
import ysomap.cli.model.MetaData;
import ysomap.common.annotation.Require;
import ysomap.common.util.ColorStyle;
import ysomap.common.util.Logger;

import java.util.*;

/**
 * @author wh1t3P1g
 * @since 2021/6/15
 */
public class Printer {

    public static void printSessions(String curSession, Map<String, Session> sessions){
        Logger.success("List all sessions");
        Logger.success("You can use `sessions uuid` to recover a selected session.");
        Logger.success("Or you can use `kill uuid` to kill/remove a running session.");
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("UUID", "Status", "Details");
        at.addRule();
        for(Map.Entry<String, Session> entry:sessions.entrySet()){
            String uuid = entry.getKey();
            Session session = entry.getValue();
            if(uuid.equals(curSession)) {
                at.addRow("*current*", session.isRunning()?"running":"stopped", session.toString());
            }else{
                at.addRow(uuid, session.isRunning()?"running":"stopped", session.toString());
            }
            at.addRule();
        }
        CWC_FixedWidth cwc = new CWC_FixedWidth();
        cwc.add(40);
        cwc.add(15);
        cwc.add(80);
        printTable(at, cwc);
    }

    public static void printCandidates(String type, Class<?> clazz, boolean detail, Map<String, MetaData> dataMap){
        List<String> candidates = Arrays.asList(Require.Utils.getRequiresFromClass(clazz));
        if(candidates.size() > 0){
            String c = collect(candidates);
            Logger.normal("You can choose "+type+": "+c);
            if(detail){
                List<MetaData> candidatesMetaData = getTargetMetaData(candidates, dataMap);
                if("payloads".equals(type)){
                    printPayloadsInfo(candidatesMetaData);
                }else if("bullets".equals(type)){
                    printBulletsInfo(candidatesMetaData);
                }
            }
        }else{
            Logger.normal("No need to set a "+type);
        }
    }

    public static List<MetaData> getTargetMetaData(List<String> candidates, Map<String, MetaData> dataMap){
        List<MetaData> ret = new ArrayList<>();
        for(String candidate:candidates){
            ret.add(dataMap.get(candidate));
        }
        return ret;
    }

    public static String collect(List<String> candidates){
        List<String> ret = new ArrayList<>();
        for(String candidate:candidates){
            ret.add(ColorStyle.makeWordRedAndBoldAndUnderline(candidate));
        }
        return Arrays.toString(ret.toArray());
    }

    public static void printSettings(Class<?> clazz, HashMap<String, Object> settings){
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Arguments", "Types", "Values", "Details");
        at.addRule();
        Map<String,String[]> bullets = Require.Utils.getRequiresFromFields(clazz);
        for(Map.Entry<String,String[]> item: bullets.entrySet()){
            String key = item.getKey();
            String[] detail = item.getValue();
            String value = (String)settings.get(key);
            at.addRow(
                    key,
                    detail[0],
                    value == null?"":value,
                    detail[1]);
            at.addRule();
        }
        CWC_FixedWidth cwc = new CWC_FixedWidth();
        cwc.add(25);
        cwc.add(20);
        cwc.add(30);
        cwc.add(50);
        printTable(at, cwc);
    }

    public static void printExploitsInfo(Collection<MetaData> data){
        Logger.success("List all exploits!");
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Exploit", "Author", "Require", "Details");
        at.addRule();
        for(MetaData metaData:data){
            at.addRow(metaData.getSimpleName(),
                    metaData.getAuthor(),
                    metaData.getRequires(),
                    metaData.getDetail()
                    );
            at.addRule();
        }
        CWC_FixedWidth cwc = new CWC_FixedWidth();
        cwc.add(25);
        cwc.add(20);
        cwc.add(30);
        cwc.add(80);
        printTable(at, cwc);
    }

    public static void printPayloadsInfo(Collection<MetaData> data){
        Logger.success("List all payloads!");
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Payloads", "Author", "Targets", "Dependencies");
        at.addRule();
        for(MetaData metaData:data){
            if(metaData == null)continue;
            at.addRow(metaData.getSimpleName(),
                    metaData.getAuthor(),
                    metaData.getTarget(),
                    metaData.getDependencies()
            );
            at.addRule();
        }
        printTable(at, new CWC_LongestLine());
    }

    public static void printBulletsInfo(Collection<MetaData> data){
        Logger.success("List all bullets!");
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Bullet", "Targets", "Dependencies", "Details");
        at.addRule();
        for(MetaData metaData:data){
            at.addRow(metaData.getSimpleName(),
                    metaData.getTarget(),
                    metaData.getDependencies(),
                    metaData.getDetail()
                    );
            at.addRule();
        }
        printTable(at, new CWC_LongestLine());
    }

    public static void printTable(AsciiTable at, AT_ColumnWidthCalculator cwc){
        at.setTextAlignment(TextAlignment.LEFT);
        at.getRenderer().setCWC(cwc);
        at.getContext().setGrid(A8_Grids.lineDobuleTripple());
        System.out.println(at.render());
        Logger.success("print current table done!");
    }
}
