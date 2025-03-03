package auth;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Configuration class for Auth0 authentication
 */
public class Auth0Config {
    private static final Dotenv dotenv = Dotenv.load();
    
    // Auth0 credentials from .env file
    private static final String DOMAIN = dotenv.get("AUTH0_DOMAIN", "your-domain.auth0.com");
    private static final String CLIENT_ID = dotenv.get("AUTH0_CLIENT_ID", "your-client-id");
    private static final String CLIENT_SECRET = dotenv.get("AUTH0_CLIENT_SECRET", "your-client-secret");
    private static final String AUDIENCE = dotenv.get("AUTH0_AUDIENCE", "https://your-api-identifier");
    
    private static AuthAPI authAPI;
    
    /**
     * Get the Auth0 API client
     * @return AuthAPI instance
     */
    public static AuthAPI getAuthAPI() {
        if (authAPI == null) {
            authAPI = new AuthAPI(DOMAIN, CLIENT_ID, CLIENT_SECRET);
        }
        return authAPI;
    }
    
    /**
     * Exchange authorization code for tokens
     * @param code Authorization code from Auth0
     * @param redirectUri Redirect URI used in the authorization request
     * @return TokenHolder containing access and ID tokens
     * @throws Auth0Exception if token exchange fails
     */
    public static TokenHolder exchangeCodeForTokens(String code, String redirectUri) throws Auth0Exception {
        return getAuthAPI().exchangeCode(code, redirectUri).execute();
    }
    
    /**
     * Decode and verify a JWT token
     * @param token JWT token to decode
     * @return DecodedJWT containing token claims
     */
    public static DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }
    
    /**
     * Get user info from ID token
     * @param idToken ID token from Auth0
     * @return Auth0User object containing user information
     */
    public static Auth0User getUserFromToken(String idToken) {
        DecodedJWT jwt = decodeToken(idToken);
        
        String userId = jwt.getSubject();
        String email = jwt.getClaim("email").asString();
        String name = jwt.getClaim("name").asString();
        String picture = jwt.getClaim("picture").asString();
        
        return new Auth0User(userId, email, name, picture);
    }
} 