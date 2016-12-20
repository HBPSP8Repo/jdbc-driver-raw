package raw.jdbc;

import raw.jdbc.rawclient.RawRestClient;

import java.sql.*;

public class RawStatement implements Statement {
    private RawRestClient client;
    private RawConnection connection;
    private int queryTimeout;
    private int fetchSize = 1000;
    private ResultSet result;

    RawStatement(RawRestClient client, RawConnection parent) {
        this.client = client;
        this.connection = parent;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        RawResultSet rs = new RawResultSet(client, sql, this);
        rs.setFetchSize(fetchSize);
        return rs;
    }

    public int executeUpdate(String sql) throws SQLException {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public void close() throws SQLException {
        //TODO: close all living queries?
    }

    public int getMaxFieldSize() throws SQLException {
        //TODO: check the correct value
        return 100000;
    }

    public void setMaxFieldSize(int max) throws SQLException {
        throw new UnsupportedOperationException("Not implemented setMaxFieldSize");
    }

    public int getMaxRows() throws SQLException {
        //TODO: check the correct value
        return 100000;
    }

    public void setMaxRows(int max) throws SQLException {
        //TODO: implement this
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        throw new UnsupportedOperationException("Not implemented setEscapeProcessing");
    }

    public int getQueryTimeout() throws SQLException {
        return queryTimeout;
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        //TODO: implement this
        this.queryTimeout = seconds;
    }

    public void cancel() throws SQLException {
        throw new UnsupportedOperationException("Not implemented cancel");
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void clearWarnings() throws SQLException {

    }

    public void setCursorName(String name) throws SQLException {
        throw new UnsupportedOperationException("Not implemented setCursorName");
    }

    public boolean execute(String sql) throws SQLException {
        this.result = new RawResultSet(client, sql, this);
        return true;
    }

    public ResultSet getResultSet() throws SQLException {
        return this.result;
    }

    public int getUpdateCount() throws SQLException {
        return 0;
    }

    public boolean getMoreResults() throws SQLException {
        return false;
    }

    public void setFetchDirection(int direction) throws SQLException {
        throw new UnsupportedOperationException("Not implemented setFetchDirection");
    }

    public int getFetchDirection() throws SQLException {
        return ResultSet.FETCH_FORWARD;
    }

    public void setFetchSize(int rows) throws SQLException {
        this.fetchSize = rows;
    }

    public int getFetchSize() throws SQLException {
        return this.fetchSize;
    }

    public int getResultSetConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    public int getResultSetType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    public void addBatch(String sql) throws SQLException {
        throw new UnsupportedOperationException("Not implemented addBatch");
    }

    public void clearBatch() throws SQLException {
        throw new UnsupportedOperationException("Not implemented clearBatch");
    }

    public int[] executeBatch() throws SQLException {
        throw new UnsupportedOperationException("Not implemented getGeneratedKeys");
    }

    public Connection getConnection() throws SQLException {
        return this.connection;
    }

    public boolean getMoreResults(int current) throws SQLException {
        throw new UnsupportedOperationException("Not implemented getMoreResults");
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        throw new UnsupportedOperationException("Not implemented getGeneratedKeys");
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        this.result = new RawResultSet(client,sql, this);
        return true;
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        this.result = new RawResultSet(client,sql, this);
        return true;
    }

    public boolean execute(String sql, String[] columnNames) throws SQLException {
        this.result = new RawResultSet(client,sql, this);
        return true;
    }

    public int getResultSetHoldability() throws SQLException {
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    public boolean isClosed() throws SQLException {
        return false;
    }

    public void setPoolable(boolean poolable) throws SQLException {

    }

    public boolean isPoolable() throws SQLException {
        return false;
    }

    public void closeOnCompletion() throws SQLException {

    }

    public boolean isCloseOnCompletion() throws SQLException {
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}