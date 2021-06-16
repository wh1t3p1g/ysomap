package ysomap.common.util;

import java.util.*;

public class Strings {
    public static String join(Iterable<String> strings, String sep, String prefix, String suffix) {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : strings) {
            if (! first) sb.append(sep);
            if (prefix != null) sb.append(prefix);
            sb.append(s);
            if (suffix != null) sb.append(suffix);
            first = false;
        }
        return sb.toString();
    }

    public static String repeat(String str, int num) {
        final String[] strs = new String[num];
        Arrays.fill(strs, str);
        return join(Arrays.asList(strs), "", "", "");
    }

    public static List<String> formatTable(List<String[]> rows) {
        final Integer[] maxLengths = new Integer[rows.get(0).length];
        for (String[] row : rows) {
            if (maxLengths.length != row.length) throw new IllegalStateException("mismatched columns");
            for (int i = 0; i < maxLengths.length; i++) {
                if (maxLengths[i] == null || maxLengths[i] < row[i].length()) {
                    maxLengths[i] = row[i].length();
                }
            }
        }

        final List<String> lines = new LinkedList<String>();
        for (String[] row : rows) {
            for (int i = 0; i < maxLengths.length; i++) {
                final String pad = repeat(" ", maxLengths[i] - row[i].length());
                row[i] = row[i] + pad;
            }
            lines.add(join(Arrays.asList(row), " ", "", ""));
        }
        return lines;
    }

    public static class ToStringComparator implements Comparator<Object> {
        public int compare(Object o1, Object o2) { return o1.toString().compareTo(o2.toString()); }
    }

    public static String randomString(int length){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
