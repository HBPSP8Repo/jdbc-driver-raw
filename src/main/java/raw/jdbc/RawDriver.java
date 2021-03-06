package raw.jdbc;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import raw.jdbc.rawclient.RawRestClient;
import raw.jdbc.rawclient.requests.PasswordTokenRequest;
import raw.jdbc.rawclient.requests.TokenResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
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
    private static final String USER_PROPERTY = "user";
    private static final String PASSWD_PROPERTY = "password";

    private static Logger logger = Logger.getLogger(RawDriver.class.getName());

    static {
        register();
    }

    public static void register() {
        try {
            logger.fine("registering RawDriver");
            DriverManager.registerDriver(new RawDriver());
        } catch (SQLException e) {
            throw new RuntimeException("Could not load RawDriver");
        }
    }

    public Connection connect(String url, Properties info) throws SQLException {
        Properties props = parseProperties(url, info);
        String authUrl = props.getProperty(RawDriver.AUTH_PROPERTY);
        String executor = props.getProperty(RawDriver.EXEC_PROPERTY);

        PasswordTokenRequest credentials = new PasswordTokenRequest();
        credentials.client_id = RawDriver.JDBC_CLIENT_ID;
        credentials.client_secret = null;
        credentials.grant_type = RawDriver.GRANT_TYPE;
        credentials.username = props.getProperty(RawDriver.USER_PROPERTY);
        credentials.password = props.getProperty(RawDriver.PASSWD_PROPERTY);
        try {
            TokenResponse token = RawRestClient.getPasswdGrantToken(authUrl, credentials);
            RawRestClient client = new RawRestClient(executor, token);
            return new RawConnection(url, client, credentials.username);
        } catch (IOException e) {
            throw new SQLException("Could not get authorization token " + e.getMessage());
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
            executor += uri.getPath();
            properties.setProperty(EXEC_PROPERTY, executor);

            String userinfo = uri.getUserInfo();
            if (userinfo != null) {
                int idx = userinfo.indexOf(':');
                String username;
                if (idx > 0) {
                    username = URLDecoder.decode(userinfo.substring(0, idx), "UTF-8");
                    String password = URLDecoder.decode(userinfo.substring(idx + 1), "UTF-8");
                    properties.setProperty(PASSWD_PROPERTY, password);
                } else {
                    username = uri.getUserInfo();
                }
                properties.setProperty(USER_PROPERTY, username);
            }
            return properties;
        } catch (IOException e) {
            throw new SQLException("Could not parser URL: " + e.getMessage());
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

    public Logger getParentLogger() {
        return logger.getParent();
    }
}
