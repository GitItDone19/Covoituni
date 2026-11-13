package Services.Reclamation;

import entities.Reclamation;
import entities.Role;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.DatabaseTestSupport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReclamationServiceTest extends DatabaseTestSupport {

    private ReclamationService reclamationService;
    private User author;

    @BeforeEach
    void setUp() throws Exception {
        insertDefaultRoles();
        author = insertUser("passenger@esprit.tn", Role.PASSENGER_CODE, 5.0);
        reclamationService = new ReclamationService();
    }

    @Test
    void createThenReadAllReturnsComplaintWithAuthor() throws Exception {
        reclamationService.create(new Reclamation("Conducteur absent", "EN_ATTENTE", author));

        List<Reclamation> complaints = reclamationService.readAll();

        assertEquals(1, complaints.size());
        Reclamation saved = complaints.get(0);
        assertEquals("Conducteur absent", saved.getDescription());
        assertEquals("EN_ATTENTE", saved.getStatus());
        assertEquals("passenger@esprit.tn", saved.getUser().getEmail());
    }

    @Test
    void getReclamationsByStatusFiltersComplaints() throws Exception {
        reclamationService.create(new Reclamation("Retard", "EN_ATTENTE", author));
        reclamationService.create(new Reclamation("Paiement", "RESOLUE", author));

        List<Reclamation> pending = reclamationService.getReclamationsByStatus("EN_ATTENTE");

        assertEquals(1, pending.size());
        assertEquals("Retard", pending.get(0).getDescription());
    }

    @Test
    void updateMovesComplaintToInProgress() throws Exception {
        reclamationService.create(new Reclamation("Voiture sale", "EN_ATTENTE", author));
        Reclamation saved = reclamationService.readAll().get(0);

        saved.setDescription("Voiture sale et en retard");
        reclamationService.update(saved);

        assertEquals("EN_COURS", saved.getStatus());
        Reclamation updated = reclamationService.readAll().get(0);
        assertEquals("Voiture sale et en retard", updated.getDescription());
        assertEquals("EN_COURS", updated.getStatus());
    }

    @Test
    void replyResolvesComplaint() throws Exception {
        reclamationService.create(new Reclamation("Trajet annulé", "EN_ATTENTE", author));
        Reclamation saved = reclamationService.readAll().get(0);

        reclamationService.reply(saved, "Vous avez été remboursé.");

        Reclamation resolved = reclamationService.readAll().get(0);
        assertEquals("RESOLUE", resolved.getStatus());
        assertEquals("Vous avez été remboursé.", resolved.getAdminReply());
    }

    @Test
    void deleteRemovesComplaint() throws Exception {
        reclamationService.create(new Reclamation("Doublon", "EN_ATTENTE", author));

        reclamationService.delete(reclamationService.readAll().get(0));

        assertTrue(reclamationService.readAll().isEmpty());
    }
}
