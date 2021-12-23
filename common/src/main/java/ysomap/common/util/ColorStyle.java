package ysomap.common.util;

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

    public static String makeWordRedAndBoldAndUnderline(String word){
        AttributedStringBuilder asb = new AttributedStringBuilder();
        asb.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
        asb.style(asb.style().bold());
        asb.style(asb.style().underline());
        asb.append(word);
        asb.style(asb.style().underlineOff());
        asb.style(asb.style().boldOff());
        return asb.toAnsi();
    }

    public static String makeWordGreen(String word){
        return new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                .append(word)
                .style(AttributedStyle.DEFAULT)
                .toAnsi();
    }

    public static String makeWordBold(String word){
        AttributedStringBuilder asb = new AttributedStringBuilder();
        asb.style(asb.style().bold());
        asb.append(word);
        asb.style(asb.style().boldOff());
        return asb.toAnsi();
    }
    public static String makeWordBoldAndUnderline(String word){
        AttributedStringBuilder asb = new AttributedStringBuilder();
        asb.style(asb.style().bold());
        asb.style(asb.style().underline());
        asb.append(word);
        asb.style(asb.style().underlineOff());
        asb.style(asb.style().boldOff());
        return asb.toAnsi();
    }
}
