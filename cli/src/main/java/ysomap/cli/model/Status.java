package ysomap.cli.model;

import ysomap.common.util.ColorStyle;
import ysomap.common.util.Strings;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wh1t3P1g
 * @since 2021/6/14
 */
public class Status {
    public static String YSOMAP = ColorStyle.makeWordBoldAndUnderline("ysomap");

    private Map<String, String> prompt = new LinkedHashMap<>();

    public String makePrompt(){
        List<String> prompts = new LinkedList<>();
        prompts.add(YSOMAP);
        prompts.addAll(prompt.values());
        String temp = Strings.join(prompts, " ", "", "");
        return temp+" > ";
    }

    public void addPrompt(String type, String value){
        prompt.put(type, type+"("+ ColorStyle.makeWordRed(value)+")");
    }

    public void removePrompt(String type){
        prompt.remove(type);
    }

    public void clear(){
        prompt.clear();
    }
}
