package ysomap.common.util;

import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Require;

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

    public static String[] printClassDescription(Class<?> clazz, String index){
        return new String[] {
                index,
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

    public static void printEorBDetails(Class<?> clazz, Map<String, String> settings){
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

    public static void printPorEDetails(Class<?> clazz, String type, boolean flag){
        if(flag){// print common details
            printCommonDetail(clazz);
        }
        System.out.println("\n");
        List<String[]> rows = new LinkedList<String[]>();
        rows.add(new String[] {type});
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

    public static void printConsoleTable(String type, Collection<Class<?>> enums ){
        final List<String[]> rows = new LinkedList<String[]>();
        rows.add(new String[] {"Num", type, "Authors", "Dependencies"});
        rows.add(new String[] {"-----","---------------------", "---------------------", "------------------------------------------"});
        int index = 1;
        for(Class<?> clazz: enums){
            rows.add(printClassDescription(clazz, index+""));
            index++;
        }

        printTable(rows);
    }

}
