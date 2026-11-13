package Services.Avis;

import entities.Avis;
import entities.Role;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.DatabaseTestSupport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AvisServiceTest extends DatabaseTestSupport {

    private AvisService avisService;
    private User author;

    @BeforeEach
    void setUp() throws Exception {
        insertDefaultRoles();
        author = insertUser("passenger@esprit.tn", Role.PASSENGER_CODE, 5.0);
        avisService = new AvisService();
    }

    @Test
    void createThenReadAllReturnsReviewWithAuthor() throws Exception {
        avisService.create(new Avis("Trajet très agréable", 5, author));

        List<Avis> reviews = avisService.readAll();

        assertEquals(1, reviews.size());
        Avis saved = reviews.get(0);
        assertEquals("Trajet très agréable", saved.getCommentaire());
        assertEquals(5, saved.getRating());
        assertEquals("passenger@esprit.tn", saved.getUser().getEmail());
        assertNotNull(saved.getDateAvis());
    }

    @Test
    void replyStoresAdminResponse() throws Exception {
        avisService.create(new Avis("Retard de 20 minutes", 2, author));
        Avis saved = avisService.readAll().get(0);

        avisService.reply(saved, "Merci, nous avons contacté le conducteur.");

        assertEquals("Merci, nous avons contacté le conducteur.", queryString("SELECT reponse_avis FROM avis"));
    }

    @Test
    void updateChangesCommentAndRating() throws Exception {
        avisService.create(new Avis("Correct", 3, author));
        Avis saved = avisService.readAll().get(0);

        saved.setCommentaire("Finalement excellent");
        saved.setRating(5);
        avisService.update(saved);

        Avis updated = avisService.readAll().get(0);
        assertEquals("Finalement excellent", updated.getCommentaire());
        assertEquals(5, updated.getRating());
    }

    @Test
    void deleteRemovesReview() throws Exception {
        avisService.create(new Avis("À supprimer", 1, author));

        avisService.delete(avisService.readAll().get(0));

        assertEquals(0, queryInt("SELECT COUNT(*) FROM avis"));
    }
}
