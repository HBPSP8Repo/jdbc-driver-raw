package raw.jdbc;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import raw.jdbc.rawclient.requests.TokenResponse;
import raw.jdbc.rawclient.requests.PasswordTokenRequest;
import raw.jdbc.rawclient.RawRestClient;


import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class RawDriver implements Driver {

    private static final String AUTH_SERVER_URL = "http://localhost:9000/oauth2/access_token";
    private static final String JDBC_CLIENT_ID = "raw-jdbc";
    private static final String GRANT_TYPE = "password";
    private static final String EXEC_PROPERTY = "executor";
    private static final String AUTH_PROPERTY = "auth_url";
    private static final String USER_PROPERTY = "username";
    private static final String PASSWD_PROPERTY = "password";
    static Logger logger = Logger.getLogger(RawDriver.class.getName());

    public Connection connect(String url, Properties info) throws SQLException {

        Properties props = parseProperties(url, info);
        PasswordTokenRequest credentials = new PasswordTokenRequest();
        credentials.client_id = JDBC_CLIENT_ID;
        credentials.client_secret = null;
        credentials.grant_type = GRANT_TYPE;
        credentials.username = props.getProperty(USER_PROPERTY);
        credentials.password = props.getProperty(PASSWD_PROPERTY);

        String authUrl = props.getProperty(AUTH_PROPERTY);
        String executer = props.getProperty(EXEC_PROPERTY);
        try {
            TokenResponse token = RawRestClient.getPasswdGrantToken(authUrl, credentials);
            return new RawConnection(executer, authUrl, token);
        } catch (IOException e) {
            throw new SQLException("Unable to get bearer token for jdbc connection: " + e.getMessage());
        }
    }

    public static Properties parseUrl(String url) throws SQLException {
        if (!url.startsWith("jdbc:raw:")) {
            throw new SQLException("Invalid url, expected url starting with 'jdbc:raw:'");
        }
        try {
            Properties properties = new Properties();
            // removes the jdbc:raw:
            String cleaned = url.substring(9);
            URL uri = new URL(cleaned);
            List<NameValuePair> paramList = URLEncodedUtils.parse(new URI(cleaned), "UTF-8");
            for (NameValuePair nvp : paramList) {
                properties.put(nvp.getName(), nvp.getValue());
            }

            String executor = uri.getProtocol() + "://" + uri.getHost();
            if (uri.getPort() > 0) {
                executor += ":" + uri.getPort();
            }

            properties.setProperty(EXEC_PROPERTY, executor);
            String userinfo = uri.getUserInfo();
            if (userinfo != null) {
                int idx = userinfo.indexOf(':');
                String username;
                if (idx > 0) {
                    username = URLDecoder.decode(userinfo.substring(0, idx));
                    String password = URLDecoder.decode(userinfo.substring(idx + 1));
                    properties.setProperty("password", password);
                } else {
                    username = uri.getUserInfo();
                }
                properties.setProperty("username", username);
            }
            return properties;
        } catch (MalformedURLException e) {
            throw new SQLException("Invalid url: " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new SQLException("Invalid url: " + e.getMessage());
        }
    }

    /**
     * Parses the URL and Properties adding default values
     * the properties defined in the url have priority
     *
     * @param url  url with parameters to parse
     * @param info properties provided by the system
     * @return New properties with default values
     */
    private static Properties parseProperties(String url, Properties info) throws SQLException {
        //Parses url parameters into a Map
        Properties urlParams = parseUrl(url);

        Properties finalInfo = new Properties();
        finalInfo.setProperty(AUTH_PROPERTY, AUTH_SERVER_URL);

        // copies all relevant properties giving priority to the ones defined in the url
        String[] properties = {
                AUTH_PROPERTY,
                USER_PROPERTY,
                PASSWD_PROPERTY
        };

        for (String prop : properties) {
            if (urlParams.containsKey(prop)) {
                finalInfo.setProperty(prop, urlParams.getProperty(prop));
            } else if (info.containsKey(prop)) {
                finalInfo.setProperty(prop, info.getProperty(prop));
            }
        }
        // sets the executor property from the url parameters
        finalInfo.setProperty(EXEC_PROPERTY, urlParams.getProperty(EXEC_PROPERTY));
        if (finalInfo.getProperty(AUTH_PROPERTY) == null) {
            throw new SQLException("Could not find authorization url property");
        }
        return finalInfo;
    }

    public boolean acceptsURL(String url) {

        try {
            parseUrl(url);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    public int getMajorVersion() {
        return 0;
    }

    public int getMinorVersion() {
        return 0;
    }

    public boolean jdbcCompliant() {
        return false;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
