package org.jboss.as.connector.subsystems.datasources;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author wh1t3p1g
 * @since 2022/9/29
 */
public class WildFlyDataSource implements DataSource, Serializable {

    private static final long serialVersionUID = 1L;
    private transient DataSource delegate;
    private transient String jndiName;

    public WildFlyDataSource(DataSource delegate, String jndiName) {
        this.delegate = delegate;
        this.jndiName = jndiName;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.jndiName);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.jndiName = (String)in.readObject();

        try {
            InitialContext context = new InitialContext();
            DataSource originalDs = (DataSource)context.lookup(this.jndiName);
            this.delegate = originalDs;
        } catch (Exception var4) {
            throw new IOException(var4);
        }
    }
}
