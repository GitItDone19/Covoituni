package Services;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IService<T> {

    void create(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(T t) throws SQLException;

    ArrayList<T> readAll() throws SQLException;
}
