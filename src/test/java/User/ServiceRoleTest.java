package User;

import entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.DatabaseTestSupport;

import static org.junit.jupiter.api.Assertions.*;

class ServiceRoleTest extends DatabaseTestSupport {

    private ServiceRole serviceRole;

    @BeforeEach
    void setUp() {
        serviceRole = new ServiceRole();
    }

    @Test
    void initializeDefaultRolesCreatesAdminDriverAndPassenger() throws Exception {
        serviceRole.initializeDefaultRoles();

        assertEquals(3, serviceRole.readAll().size());
        assertNotNull(serviceRole.findByCode(Role.ADMIN_CODE));
        assertNotNull(serviceRole.findByCode(Role.DRIVER_CODE));
        assertNotNull(serviceRole.findByCode(Role.PASSENGER_CODE));
    }

    @Test
    void initializeDefaultRolesDoesNotDuplicateExistingRoles() throws Exception {
        serviceRole.initializeDefaultRoles();
        serviceRole.initializeDefaultRoles();

        assertEquals(3, serviceRole.readAll().size());
    }

    @Test
    void findByCodeReturnsNullForUnknownRole() throws Exception {
        serviceRole.initializeDefaultRoles();

        assertNull(serviceRole.findByCode("inconnu"));
    }

    @Test
    void updateChangesDisplayName() throws Exception {
        serviceRole.initializeDefaultRoles();
        Role driver = serviceRole.findByCode(Role.DRIVER_CODE);

        driver.setDisplayName("Chauffeur");
        serviceRole.update(driver);

        assertEquals("Chauffeur", serviceRole.findByCode(Role.DRIVER_CODE).getDisplayName());
    }
}
