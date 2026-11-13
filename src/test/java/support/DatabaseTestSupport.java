package support;

import entities.Role;
import entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import utils.MyConnection;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Gives each test a fresh in-memory H2 database (MySQL mode) with the app schema,
 * so the services can be tested without a running MySQL server.
 */
public abstract class DatabaseTestSupport {

    protected Connection connection;

    private MockedStatic<MyConnection> myConnection;

    @BeforeEach
    void openDatabase() throws Exception {
        String url = "jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE";
        connection = DriverManager.getConnection(url, "sa", "");
        try (Statement st = connection.createStatement()) {
            for (String sql : readSchema().split(";")) {
                if (!sql.isBlank()) {
                    st.execute(sql);
                }
            }
        }

        // Services call MyConnection.getInstance().getCnx(): hand them the H2 connection instead of MySQL
        MyConnection testConnection = mock(MyConnection.class);
        when(testConnection.getCnx()).thenReturn(connection);
        myConnection = mockStatic(MyConnection.class);
        myConnection.when(MyConnection::getInstance).thenReturn(testConnection);
    }

    @AfterEach
    void closeDatabase() throws SQLException {
        myConnection.close();
        connection.close();
    }

    private String readSchema() throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/schema.sql")) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    protected void insertDefaultRoles() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("INSERT INTO role (code, display_name) VALUES "
                    + "('admin', 'Administrateur'), ('conducteur', 'Conducteur'), ('passager', 'Passager')");
        }
    }

    /** Inserts a user directly in the database and returns it with its generated id. */
    protected User insertUser(String email, String roleCode, double rating) throws SQLException {
        String sql = "INSERT INTO utilisateur (nom, prenom, tel, email, mdp, role_code, rating, trips_count, username) "
                + "VALUES ('Nom', 'Prenom', '22000000', ?, 'secret', ?, ?, 0, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            ps.setString(2, roleCode);
            ps.setDouble(3, rating);
            ps.setString(4, email.split("@")[0]);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                User user = new User(keys.getInt(1), "Nom", "Prenom", "22000000", email, "secret",
                        new Role(0, roleCode, roleCode), null);
                user.setRating(rating);
                return user;
            }
        }
    }

    protected String queryString(String sql) throws SQLException {
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getString(1) : null;
        }
    }

    protected int queryInt(String sql) throws SQLException {
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
