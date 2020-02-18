package ysomap.util;

import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.util.enums.ObjectEnums;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    public static void printConsoleTable(String type, ObjectEnums[] enums ){
        final List<String[]> rows = new LinkedList<String[]>();
        rows.add(new String[] {"Num",type, "Authors", "Requires"});
        rows.add(new String[] {"-------","-------", "-----------", "----------------"});

        for(int i=0; i< enums.length; i++){
            rows.add(OutputHelper.printClassDescription(enums[i].getClazz(), enums[i].getName()));
        }

        final List<String> lines = Strings.formatTable(rows);
        for (String line : lines) {
            System.err.println("     " + line);
        }
    }
}
