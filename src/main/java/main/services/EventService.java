package main.services;

import main.entities.Event;
import main.utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class EventService {
    private Connection connection;

    public EventService() {
        connection = MyConnection.getInstance().getCnx();
    }

    public List<Event> readAll() throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event ORDER BY date_event ASC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Event event = new Event();
                event.setIdEvent(rs.getInt("id_event"));
                event.setNom(rs.getString("nom"));
                event.setDateEvent(rs.getString("date_event"));
                event.setHeureEvent(rs.getString("heure_event"));
                event.setLieu(rs.getString("lieu"));
                event.setDescription(rs.getString("description"));
                event.setIdType(rs.getInt("id_type"));
                event.setStatus(rs.getString("status"));
                events.add(event);
            }
        }
        return events;
    }

    public Event readById(int id) throws SQLException {
        String query = "SELECT * FROM event WHERE id_event = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Event event = new Event();
                event.setIdEvent(rs.getInt("id_event"));
                event.setNom(rs.getString("nom"));
                event.setDateEvent(rs.getString("date_event"));
                event.setHeureEvent(rs.getString("heure_event"));
                event.setLieu(rs.getString("lieu"));
                event.setDescription(rs.getString("description"));
                event.setIdType(rs.getInt("id_type"));
                event.setStatus(rs.getString("status"));
                return event;
            }
        }
        return null;
    }

    public void delete(Event event) throws SQLException {
        String query = "DELETE FROM event WHERE id_event = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, event.getIdEvent());
            pstmt.executeUpdate();
        }
    }

    public void terminerEvent(Event event) throws SQLException {
        String query = "UPDATE event SET status = 'TERMINATED' WHERE id_event = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, event.getIdEvent());
            pstmt.executeUpdate();
            event.setStatus("TERMINATED");
        }
    }

    public void update(Event event) throws SQLException {
        String query = "UPDATE event SET nom = ?, date_event = ?, heure_event = ?, " +
                      "lieu = ?, description = ? WHERE id_event = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, event.getNom());
            pstmt.setString(2, event.getDateEvent());
            pstmt.setString(3, event.getHeureEvent());
            pstmt.setString(4, event.getLieu());
            pstmt.setString(5, event.getDescription());
            pstmt.setInt(6, event.getIdEvent());
            pstmt.executeUpdate();
        }
    }

    public List<Event> readAllActive() throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event WHERE status = 'ACTIVE' ORDER BY date_event ASC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Event event = new Event();
                event.setIdEvent(rs.getInt("id_event"));
                event.setNom(rs.getString("nom"));
                event.setDateEvent(rs.getString("date_event"));
                event.setHeureEvent(rs.getString("heure_event"));
                event.setLieu(rs.getString("lieu"));
                event.setDescription(rs.getString("description"));
                event.setIdType(rs.getInt("id_type"));
                event.setStatus(rs.getString("status"));
                events.add(event);
            }
        }
        return events;
    }

    public void create(Event event) throws SQLException {
        String query = "INSERT INTO event (nom, date_event, heure_event, lieu, description, id_type, status) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, event.getNom());
            pstmt.setString(2, event.getDateEvent());
            pstmt.setString(3, event.getHeureEvent());
            pstmt.setString(4, event.getLieu());
            pstmt.setString(5, event.getDescription());
            pstmt.setInt(6, event.getIdType());
            pstmt.setString(7, event.getStatus());
            
            try {
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de la création de l'événement:");
                System.err.println("Query: " + query);
                System.err.println("Values: " + event.getNom() + ", " + event.getDateEvent() + ", " + 
                                 event.getHeureEvent() + ", " + event.getLieu() + ", " + 
                                 event.getDescription() + ", " + event.getIdType() + ", " + 
                                 event.getStatus());
                throw e;
            }
        }
    }

    public Map<String, Integer> getEventTypes() throws SQLException {
        Map<String, Integer> typeMap = new LinkedHashMap<>(); // LinkedHashMap pour préserver l'ordre
        String query = "SELECT id_type, nom FROM typeevent ORDER BY id_type";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                typeMap.put(rs.getString("nom"), rs.getInt("id_type"));
            }
        }
        return typeMap;
    }
} 