package User;

import entities.Role;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.DatabaseTestSupport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceUserTest extends DatabaseTestSupport {

    private ServiceUser serviceUser;

    @BeforeEach
    void setUp() throws Exception {
        insertDefaultRoles();
        serviceUser = new ServiceUser();
    }

    @Test
    void createStoresUserWithRole() throws Exception {
        Role passenger = new Role(0, Role.PASSENGER_CODE, "Passager");
        serviceUser.create(new User(0, "Ben Ali", "Sami", "22123456", "sami@esprit.tn", "secret", passenger, null));

        List<User> users = serviceUser.readAll();

        assertEquals(1, users.size());
        User saved = users.get(0);
        assertEquals("sami@esprit.tn", saved.getEmail());
        assertEquals(Role.PASSENGER_CODE, saved.getRoleCode());
        assertEquals("Passager", saved.getRole().getDisplayName());
        assertEquals("sami", queryString("SELECT username FROM utilisateur"));
    }

    @Test
    void emailExistsDetectsRegisteredEmailsOnly() throws Exception {
        insertUser("driver@esprit.tn", Role.DRIVER_CODE, 4.5);

        assertTrue(serviceUser.emailExists("driver@esprit.tn"));
        assertFalse(serviceUser.emailExists("unknown@esprit.tn"));
    }

    @Test
    void getDriversByRatingReturnsOnlyDriversAboveThreshold() throws Exception {
        insertUser("good.driver@esprit.tn", Role.DRIVER_CODE, 4.8);
        insertUser("bad.driver@esprit.tn", Role.DRIVER_CODE, 2.0);
        insertUser("passenger@esprit.tn", Role.PASSENGER_CODE, 5.0);

        List<User> drivers = serviceUser.getDriversByRating(4.0);

        assertEquals(1, drivers.size());
        assertEquals("good.driver@esprit.tn", drivers.get(0).getEmail());
    }

    @Test
    void updateUserRatingAndIncrementTripsCount() throws Exception {
        User driver = insertUser("driver@esprit.tn", Role.DRIVER_CODE, 4.0);

        serviceUser.updateUserRating(driver.getId(), 3.5);
        serviceUser.incrementTripsCount(driver.getId());
        serviceUser.incrementTripsCount(driver.getId());

        User saved = serviceUser.readAll().get(0);
        assertEquals(3.5, saved.getRating());
        assertEquals(2, saved.getTripsCount());
    }

    @Test
    void updateChangesProfileFields() throws Exception {
        User user = insertUser("old@esprit.tn", Role.PASSENGER_CODE, 5.0);

        user.setTel("99887766");
        user.setEmail("new@esprit.tn");
        serviceUser.update(user);

        User saved = serviceUser.readAll().get(0);
        assertEquals("99887766", saved.getTel());
        assertEquals("new@esprit.tn", saved.getEmail());
    }

    @Test
    void deleteRemovesUser() throws Exception {
        User user = insertUser("leaving@esprit.tn", Role.PASSENGER_CODE, 5.0);

        serviceUser.delete(user);

        assertTrue(serviceUser.readAll().isEmpty());
    }
}
