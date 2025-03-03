package auth;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.auth.UserInfo;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Service for handling Auth0 authentication
 */
public class AuthService {
    private static final String CALLBACK_URL = "http://localhost:8000/callback";
    private static final String STATE = generateRandomState();
    
    private static String accessToken;
    private static String idToken;
    private static Auth0User currentUser;
    
    /**
     * Start the authentication process with Auth0
     * @param onSuccess Callback for successful authentication
     * @param onFailure Callback for authentication failure
     */
    public static void authenticate(Consumer<Auth0User> onSuccess, Consumer<Exception> onFailure) {
        try {
            // Create authorization URL
            String authUrl = createAuthorizationUrl();
            
            // Open WebView for authentication
            openAuthenticationWindow(authUrl, onSuccess, onFailure);
        } catch (Exception e) {
            onFailure.accept(e);
        }
    }
    
    /**
     * Create the Auth0 authorization URL
     * @return URL for Auth0 authorization
     * @throws URISyntaxException if the URL is malformed
     */
    private static String createAuthorizationUrl() throws URISyntaxException {
        AuthAPI auth = Auth0Config.getAuthAPI();
        
        Map<String, String> parameters = new HashMap<>();
        parameters.put("audience", Auth0Config.AUDIENCE);
        parameters.put("scope", "openid profile email");
        parameters.put("state", STATE);
        
        return auth.authorizeUrl(CALLBACK_URL)
                .withParameters(parameters)
                .build();
    }
    
    /**
     * Open a WebView window for Auth0 authentication
     * @param authUrl Auth0 authorization URL
     * @param onSuccess Callback for successful authentication
     * @param onFailure Callback for authentication failure
     */
    private static void openAuthenticationWindow(String authUrl, Consumer<Auth0User> onSuccess, Consumer<Exception> onFailure) {
        Platform.runLater(() -> {
            Stage authStage = new Stage();
            authStage.setTitle("Connexion avec Auth0");
            
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            
            // Handle redirects to capture the authorization code
            webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && newValue.startsWith(CALLBACK_URL)) {
                    try {
                        // Parse the URL to extract code and state
                        URI uri = new URI(newValue);
                        String query = uri.getQuery();
                        Map<String, String> queryParams = parseQueryString(query);
                        
                        // Verify state to prevent CSRF attacks
                        if (!STATE.equals(queryParams.get("state"))) {
                            throw new SecurityException("Invalid state parameter");
                        }
                        
                        String code = queryParams.get("code");
                        if (code != null) {
                            // Close the authentication window
                            authStage.close();
                            
                            // Exchange code for tokens
                            CompletableFuture.runAsync(() -> {
                                try {
                                    TokenHolder holder = Auth0Config.exchangeCodeForTokens(code, CALLBACK_URL);
                                    accessToken = holder.getAccessToken();
                                    idToken = holder.getIdToken();
                                    
                                    // Get user info from token
                                    currentUser = Auth0Config.getUserFromToken(idToken);
                                    
                                    // Call success callback
                                    Platform.runLater(() -> onSuccess.accept(currentUser));
                                } catch (Auth0Exception e) {
                                    Platform.runLater(() -> onFailure.accept(e));
                                }
                            });
                        } else if (queryParams.containsKey("error")) {
                            // Handle authentication error
                            authStage.close();
                            String error = queryParams.get("error");
                            String errorDescription = queryParams.get("error_description");
                            Platform.runLater(() -> onFailure.accept(
                                    new Auth0Exception("Authentication error: " + error + " - " + errorDescription)));
                        }
                    } catch (Exception e) {
                        authStage.close();
                        Platform.runLater(() -> onFailure.accept(e));
                    }
                }
            });
            
            // Load the Auth0 login page
            webEngine.load(authUrl);
            
            // Show the authentication window
            authStage.setScene(new javafx.scene.Scene(webView, 800, 600));
            authStage.show();
        });
    }
    
    /**
     * Parse query string into a map of parameters
     * @param query Query string from URL
     * @return Map of query parameters
     */
    private static Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] parts = param.split("=");
                if (parts.length == 2) {
                    params.put(parts[0], parts[1]);
                }
            }
        }
        return params;
    }
    
    /**
     * Generate a random state parameter for CSRF protection
     * @return Random state string
     */
    private static String generateRandomState() {
        byte[] randomBytes = new byte[32];
        java.security.SecureRandom secureRandom = new java.security.SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
    
    /**
     * Get the current authenticated user
     * @return Current Auth0User or null if not authenticated
     */
    public static Auth0User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Check if a user is currently authenticated
     * @return true if authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        return accessToken != null && idToken != null && currentUser != null;
    }
    
    /**
     * Get the access token for API calls
     * @return Access token or null if not authenticated
     */
    public static String getAccessToken() {
        return accessToken;
    }
    
    /**
     * Log out the current user
     */
    public static void logout() {
        accessToken = null;
        idToken = null;
        currentUser = null;
    }
} 