package raw.jdbc;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import static org.apache.oltu.oauth2.client.request.OAuthClientRequest.authorizationLocation;

public class RawConnection implements Connection {

    private String accessToken = null;
    private Long expiresIn = null;
    private String executerUrl;

    RawConnection(String url, String oAuthUri, String redirectUri, String user)
            throws OAuthSystemException, OAuthProblemException {

        OAuthClientRequest request = OAuthClientRequest
                .authorizationLocation(oAuthUri)
                .setClientId(user)
                .setRedirectURI(redirectUri)
                .buildQueryMessage();

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);
        accessToken = oAuthResponse.getAccessToken();
        expiresIn = oAuthResponse.getExpiresIn();
        executerUrl = url;
    }

    public Statement createStatement() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return null;
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public String nativeSQL(String sql) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public boolean getAutoCommit() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public void commit() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public void rollback() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public void close() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public boolean isClosed() throws SQLException {
        return false;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return null;
    }

    public void setReadOnly(boolean readOnly) throws SQLException {

    }

    public boolean isReadOnly() throws SQLException {
        return false;
    }

    public void setCatalog(String catalog) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public String getCatalog() throws SQLException {
        return null;
    }

    public void setTransactionIsolation(int level) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public int getTransactionIsolation() throws SQLException {
        return 0;
    }

    public SQLWarning getWarnings() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public void clearWarnings() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return null;
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {

    }

    public void setHoldability(int holdability) throws SQLException {

    }

    public int getHoldability() throws SQLException {
        return 0;
    }

    public Savepoint setSavepoint() throws SQLException {
        return null;
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        return null;
    }

    public void rollback(Savepoint savepoint) throws SQLException {

    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {

    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public Clob createClob() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public Blob createBlob() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    @SuppressWarnings("Since15")
    public NClob createNClob() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    @SuppressWarnings("Since15")
    public SQLXML createSQLXML() throws SQLException {
        return null;
    }

    public boolean isValid(int timeout) throws SQLException {
        return false;
    }

    @SuppressWarnings("Since15")
    public void setClientInfo(String name, String value) throws SQLClientInfoException {

    }

    @SuppressWarnings("Since15")
    public void setClientInfo(Properties properties) throws SQLClientInfoException {

    }

    public String getClientInfo(String name) throws SQLException {
        return null;
    }

    public Properties getClientInfo() throws SQLException {
        return null;
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return null;
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return null;
    }

    public void setSchema(String schema) throws SQLException {

    }

    public String getSchema() throws SQLException {
        return null;
    }

    public void abort(Executor executor) throws SQLException {

    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

    }

    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
