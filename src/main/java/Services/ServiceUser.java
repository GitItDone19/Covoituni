package Services;

import entities.MyConnection;
import entities.User;

import java.sql.*;
import java.util.ArrayList;

public class ServiceUser implements IService<User> {
    private Connection connection;

    public ServiceUser() {
        this.connection = MyConnection.getConnection().getCnx();
    }

    @Override
    public void ajouter(User user) {
        try {
            PreparedStatement pre = connection.prepareStatement("INSERT INTO utilisateur (nom,prenom,tel,mdp,email,role,verificationcode) VALUES (?,?,?,?,?,?,?)");
            pre.setString(1, user.getNom());
            pre.setString(2, user.getPrenom());
            pre.setString(3, user.getMdp());
            pre.setString(4, user.getTel());
            pre.setString(5, user.getEmail());
            pre.setString(6, user.getRole());
            pre.setString(7, user.getVerificationCode());

            pre.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE person SET nom=? WHERE id=?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, user.getNom());
        pst.setInt(2, user.getId());  // Added missing parameter for ID
        pst.executeUpdate();
    }

    @Override

    public void delete(User user) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE id=?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, user.getId());  // Use the user object to get the ID
        pst.executeUpdate();
    }


    @Override
    public ArrayList<User> afficherAll() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            String tel= rs.getString("tel");
            String email = rs.getString("email");
            String mdp = rs.getString("mdp");
            String role = rs.getString("role");
            String verificationcode = rs.getString("verificationcode");
            User user = new User(id, nom, prenom, tel, email,mdp,role,verificationcode);
            users.add(user);
        }
        return users;
    }
}
