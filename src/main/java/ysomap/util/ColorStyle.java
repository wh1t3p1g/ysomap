package ysomap.util;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
public class ColorStyle {

    public static String makeWordRed(String word){
        return new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                .append(word)
                .style(AttributedStyle.DEFAULT)
                .toAnsi();
    }

    public static String makeWordGreen(String word){
        return new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                .append(word)
                .style(AttributedStyle.DEFAULT)
                .toAnsi();
    }

    public static String makeWordBold(String word){
        return new AttributedStringBuilder()
                .style(AttributedStyle::bold)
                .append(word)
                .style(AttributedStyle::boldOff)
                .toAnsi();
    }
    public static String makeWordBoldAndUnderline(String word){
        return new AttributedStringBuilder()
                .style(AttributedStyle::bold)
                .style(AttributedStyle::underline)
                .append(word)
                .style(AttributedStyle::underlineOff)
                .style(AttributedStyle::boldOff)
                .toAnsi();
    }
}
