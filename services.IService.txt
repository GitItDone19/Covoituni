package services;

import java.sql.SQLException;

import java.util.List;

public interface IService<T> {

    void create(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(T t) throws SQLException;

    List<T> readAll() throws SQLException;
    
    // Nouvelle méthode pour répondre à une réclamation
    void reply(T t, String response) throws SQLException;
}