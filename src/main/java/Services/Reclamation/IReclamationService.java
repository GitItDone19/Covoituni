package Services.Reclamation;

import entities.Reclamation;
import java.sql.SQLException;
import java.util.List;

public interface IReclamationService extends IService<Reclamation> {
    // Additional methods specific to Reclamation
    List<Reclamation> getReclamationsByStatus(String status) throws SQLException;
} 