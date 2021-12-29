package ysomap.core.secmng;

import ysomap.common.exception.PermissionDenyException;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author wh1t3p1g
 * @since 2021/12/29
 */
public class YsoSecurityManager extends SecurityManager{

    private List<String> fileWhiteList = null;
    private List<String> execWhiteList = null;

    public YsoSecurityManager() {
        fileWhiteList = new ArrayList<>();
        execWhiteList = new ArrayList<>();
    }

    @Override
    public void checkExec(String cmd) {
        super.checkExec(cmd);
        if(cmd != null && !"stty".equals(cmd) && !execWhiteList.contains(cmd)){
            throw new PermissionDenyException("Exec "+cmd);
        }
    }

    @Override
    public void checkDelete(String file) {
        super.checkDelete(file);
        throw new PermissionDenyException("Delete "+file);
    }

    @Override
    public void checkWrite(String file) {
        super.checkWrite(file);
        if(!fileWhiteList.contains(file)){
            throw new PermissionDenyException("Write "+file);
        }
    }

    @Override
    public void checkPermission(Permission perm) {
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
    }

    public static <T> T callWrapped(final Callable<T> callable, SecurityManager securityManager) throws Exception {
        SecurityManager sm = System.getSecurityManager(); // save sm
        if(securityManager != null){
            System.setSecurityManager(securityManager);
        }
        try {
            return callable.call();
        } finally {
            System.setSecurityManager(sm); // restore sm
        }
    }
}
