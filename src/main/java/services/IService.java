package services;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface générique pour les services CRUD
 * @param <T> Type d'entité à gérer
 */
public interface IService<T> {
    /**
     * Crée une nouvelle entité
     * @param t Entité à créer
     * @throws SQLException En cas d'erreur SQL
     */
    void create(T t) throws SQLException;

    /**
     * Met à jour une entité existante
     * @param t Entité à mettre à jour
     * @throws SQLException En cas d'erreur SQL
     */
    void update(T t) throws SQLException;

    /**
     * Supprime une entité
     * @param t Entité à supprimer
     * @throws SQLException En cas d'erreur SQL
     */
    void delete(T t) throws SQLException;

    /**
     * Récupère toutes les entités
     * @return Liste des entités
     * @throws SQLException En cas d'erreur SQL
     */
    List<T> readAll() throws SQLException;
}
