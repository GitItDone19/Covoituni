package User;

import Services.IService;
import entities.User.Role;
import main.MyConnection;
import java.sql.*;
import java.util.ArrayList;

public class ServiceRole implements IService<Role> {
    private Connection connection;
    
    public ServiceRole() {
        this.connection = MyConnection.getInstance().getCnx();
    }
    
    @Override
    public void create(Role role) throws SQLException {
        String sql = "INSERT INTO role (code, display_name) VALUES (?, ?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, role.getCode());
        pst.setString(2, role.getDisplayName());
        pst.executeUpdate();
    }
    
    @Override
    public void update(Role role) throws SQLException {
        String sql = "UPDATE role SET code=?, display_name=? WHERE id=?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, role.getCode());
        pst.setString(2, role.getDisplayName());
        pst.setInt(3, role.getId());
        pst.executeUpdate();
    }
    
    @Override
    public void delete(Role role) throws SQLException {
        String sql = "DELETE FROM role WHERE id=?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, role.getId());
        pst.executeUpdate();
    }
    
    @Override
    public ArrayList<Role> readAll() throws SQLException {
        ArrayList<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM role";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while (rs.next()) {
            Role role = new Role(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("display_name")
            );
            roles.add(role);
        }
        return roles;
    }
    
    public Role findByCode(String code) throws SQLException {
        String sql = "SELECT * FROM role WHERE code = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, code);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            return new Role(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("display_name")
            );
        }
        return null;
    }
    
    public void initializeDefaultRoles() throws SQLException {
        // Check if roles exist
        if (readAll().isEmpty()) {
            // Add default roles
            create(new Role(0, Role.ADMIN_CODE, "Administrateur"));
            create(new Role(0, Role.DRIVER_CODE, "Conducteur"));
            create(new Role(0, Role.PASSENGER_CODE, "Passager"));
        }
    }
} 