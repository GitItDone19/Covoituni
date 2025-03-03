package config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Configuration class for Auth0 authentication
 */
public class Auth0Config {
    private static Auth0Config instance;
    private final String domain;
    private final String clientId;
    private final String clientSecret;
    private final String audience;
    
    private Auth0Config() {
        Dotenv dotenv = Dotenv.load();
        this.domain = dotenv.get("AUTH0_DOMAIN");
        this.clientId = dotenv.get("AUTH0_CLIENT_ID");
        this.clientSecret = dotenv.get("AUTH0_CLIENT_SECRET");
        this.audience = dotenv.get("AUTH0_AUDIENCE");
    }
    
    /**
     * Get the singleton instance of Auth0Config
     * @return The Auth0Config instance
     */
    public static Auth0Config getInstance() {
        if (instance == null) {
            instance = new Auth0Config();
        }
        return instance;
    }
    
    /**
     * Get the Auth0 domain
     * @return The Auth0 domain
     */
    public String getDomain() {
        return domain;
    }
    
    /**
     * Get the Auth0 client ID
     * @return The Auth0 client ID
     */
    public String getClientId() {
        return clientId;
    }
    
    /**
     * Get the Auth0 client secret
     * @return The Auth0 client secret
     */
    public String getClientSecret() {
        return clientSecret;
    }
    
    /**
     * Get the Auth0 audience
     * @return The Auth0 audience
     */
    public String getAudience() {
        return audience;
    }
} 