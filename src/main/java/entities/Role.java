package entities;

public class Role {
    private int id;
    private String code;
    private String displayName;
    
    public static final String ADMIN_CODE = "admin";
    public static final String DRIVER_CODE = "conducteur";
    public static final String PASSENGER_CODE = "passager";
    
    public Role() {}
    
    public Role(int id, String code, String displayName) {
        this.id = id;
        this.code = code;
        this.displayName = displayName;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Role role = (Role) obj;
        return code.equals(role.code);
    }
} 