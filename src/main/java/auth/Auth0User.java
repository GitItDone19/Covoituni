package auth;

/**
 * Represents a user authenticated through Auth0
 */
public class Auth0User {
    private final String userId;
    private final String email;
    private final String name;
    private final String picture;
    
    /**
     * Constructor for Auth0User
     * @param userId Unique identifier for the user
     * @param email User's email address
     * @param name User's full name
     * @param picture URL to user's profile picture
     */
    public Auth0User(String userId, String email, String name, String picture) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
    
    /**
     * Get the user's unique identifier
     * @return User ID
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * Get the user's email address
     * @return Email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Get the user's full name
     * @return Full name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the URL to the user's profile picture
     * @return Profile picture URL
     */
    public String getPicture() {
        return picture;
    }
    
    /**
     * Extract first name from the full name
     * @return First name or full name if parsing fails
     */
    public String getFirstName() {
        if (name != null && name.contains(" ")) {
            return name.split(" ")[0];
        }
        return name;
    }
    
    /**
     * Extract last name from the full name
     * @return Last name or empty string if parsing fails
     */
    public String getLastName() {
        if (name != null && name.contains(" ")) {
            String[] parts = name.split(" ");
            if (parts.length > 1) {
                return parts[parts.length - 1];
            }
        }
        return "";
    }
    
    @Override
    public String toString() {
        return "Auth0User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
} 