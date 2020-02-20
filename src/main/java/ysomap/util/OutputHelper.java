package ysomap.util;

import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;
import ysomap.util.enums.ObjectEnums;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
public class OutputHelper {

    public static void writeToFile(String filename, Object body){
        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(filename))){
            bw.write(body.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String urlEncode(String body) throws UnsupportedEncodingException {
        return URLEncoder.encode(body, "UTF-8");
    }

    public static String[] printClassDescription(Class<?> clazz, String enumName){
        return new String[] {
                enumName,
            clazz.getSimpleName(),
            Strings.join(Arrays.asList(Authors.Utils.getAuthors(clazz)), ", ", "@", ""),
            Strings.join(Arrays.asList(Dependencies.Utils.getDependencies(clazz)),", ", "", "")
        };
    }

    public static void printTable(List<String[]> rows){
        final List<String> lines = Strings.formatTable(rows);
        for (String line : lines) {
            System.err.println("     " + line);
        }
    }

    public static void printEorBDetails(Class<?> clazz, HashMap<String, String> settings){
        printCommonDetail(clazz);
        System.out.println("\n");
        List<String[]> rows = new LinkedList<String[]>();
        rows.add(new String[] {"Argument", "Type", "Value", "Detail"});
        rows.add(new String[] {"---------------------","----------","---------------------","------------------------------------------"});
        Map<String,String[]> bullets = Require.Utils.getRequiresFromFields(clazz);
        for(Map.Entry<String,String[]> item: bullets.entrySet()){
            String key = item.getKey();
            String[] detail = item.getValue();
            String value = settings.getOrDefault(key, "");
            rows.add(new String[]{
                    key,
                    detail[0],
                    value,
                    detail[1]});
        }
        printTable(rows);
    }

    public static void printPayloadDetails(Class<?> clazz){
        printCommonDetail(clazz);
        System.out.println("\n");
        List<String[]> rows = new LinkedList<String[]>();
        rows.add(new String[] {"Bullet"});
        rows.add(new String[] {"---------------------"});
        String[] bullets = Require.Utils.getRequiresFromClass(clazz);
        for(String bullet:bullets){
            rows.add(new String[]{ColorStyle.makeWordBold(bullet)});
        }
        printTable(rows);
    }

    public static void printCommonDetail(Class<?> clazz){
        List<String[]> rows = new LinkedList<String[]>();
        rows.add(new String[] {"Name", "Authors","Dependencies"});
        rows.add(new String[] {"---------------------", "---------------------", "------------------------------------------"});
        rows.add(new String[]{
                clazz.getSimpleName(),
                Strings.join(Arrays.asList(Authors.Utils.getAuthors(clazz)), ", ", "@", ""),
                Strings.join(Arrays.asList(Dependencies.Utils.getDependencies(clazz)),", ", "", "")}
        );
        printTable(rows);
    }

    public static void printConsoleTable(String type, ObjectEnums[] enums ){
        final List<String[]> rows = new LinkedList<String[]>();
        rows.add(new String[] {"Num", type, "Authors", "Dependencies"});
        rows.add(new String[] {"-----","---------------------", "---------------------", "------------------------------------------"});

        for(int i=0; i< enums.length; i++){
            rows.add(OutputHelper.printClassDescription(enums[i].getClazz(), enums[i].getName()));
        }

        printTable(rows);
    }
}
