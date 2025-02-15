package Services;

import entities.Avis;
import java.sql.SQLException;

public interface IAvisService extends IService<Avis> {
    void reply(Avis avis, String response) throws SQLException;
} 