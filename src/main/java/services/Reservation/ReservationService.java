package services.Reservation;

import java.util.List;
import entities.Reservation.Reservation;
import repositories.ReservationRepository;
import entities.Annonce.Annonce;
import services.Annonce.AnnonceService;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import entities.Trajet.Trajet;
import services.Trajet.TrajetService;

/**
 * Service gérant les opérations CRUD pour les réservations
 * Cette classe gère toutes les interactions avec la base de données pour les réservations
 */
public class ReservationService {
    private ReservationRepository reservationRepository;
    private AnnonceService annonceService;
    private TrajetService trajetService;
    private Connection connection;
    
    public ReservationService() {
        this.reservationRepository = new ReservationRepository();
        this.annonceService = new AnnonceService();
        this.trajetService = new TrajetService();
    }
    
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id);
    }
    
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }
    
    public List<Reservation> getReservationsByAnnonceId(Long annonceId) {
        return reservationRepository.findByAnnonceId(annonceId);
    }
    
    public Reservation updateReservation(Long id, String status) {
        Reservation reservation = reservationRepository.findById(id);
        if (reservation != null) {
            reservation.setStatus(status);
            return reservationRepository.save(reservation);
        }
        return null;
    }
    
    public boolean deleteReservation(Long id) {
        return reservationRepository.delete(id);
    }

    public Reservation updateStatus(Long id, String status) {
        try {
            // 1. Récupérer la réservation
            Reservation reservation = reservationRepository.findById(id);
            if (reservation == null) {
                throw new IllegalArgumentException("Réservation non trouvée");
            }

            // 2. Si la réservation passe à CANCELLED ou REFUSED, mettre à jour les places disponibles
            if (("CANCELLED".equals(status) || "REFUSED".equals(status)) 
                && !"CANCELLED".equals(reservation.getStatus()) 
                && !"REFUSED".equals(reservation.getStatus())) {
                
                // Récupérer l'annonce et le trajet
                Annonce annonce = annonceService.readById(reservation.getAnnonceId());
                if (annonce != null) {
                    Trajet trajet = trajetService.readById(annonce.getTrajetId());
                    if (trajet != null) {
                        // Incrémenter le nombre de places disponibles
                        trajet.setAvailableSeats(trajet.getAvailableSeats() + 1);
                        trajetService.update(trajet);
                        
                        // Si l'annonce était CLOSED et qu'il y a maintenant une place, la réouvrir
                        if ("CLOSED".equals(annonce.getStatus()) && trajet.getAvailableSeats() > 0) {
                            annonce.setStatus("OPEN");
                            annonceService.update(annonce);
                        }
                    }
                }
            }

            // 3. Mettre à jour le statut de la réservation
            reservation.setStatus(status);
            return reservationRepository.save(reservation);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour du statut", e);
        }
    }

    public Reservation createReservation(Long annonceId, Long userId, String comment) {
        try {
            // 1. Récupérer l'annonce
            Annonce annonce = annonceService.readById(annonceId);
            if (annonce == null) {
                throw new IllegalArgumentException("Annonce non trouvée");
            }

            // 2. Récupérer le trajet et vérifier les places disponibles
            Trajet trajet = trajetService.readById(annonce.getTrajetId());
            if (trajet == null) {
                throw new IllegalArgumentException("Trajet non trouvé");
            }

            if (trajet.getAvailableSeats() <= 0) {
                throw new IllegalStateException("Plus de places disponibles");
            }

            // 3. Décrémenter le nombre de places disponibles
            trajet.setAvailableSeats(trajet.getAvailableSeats() - 1);
            trajetService.update(trajet);

            // 4. Si c'était la dernière place, mettre l'annonce en CLOSED
            if (trajet.getAvailableSeats() == 0) {
                annonce.setStatus("CLOSED");
                annonceService.update(annonce);
            }

            // 5. Créer la réservation
            Reservation reservation = new Reservation();
            reservation.setAnnonceId(annonceId);
            reservation.setUserId(userId);
            reservation.setComment(comment != null ? comment : "");
            reservation.setStatus("PENDING");
            reservation.setDateReservation(new Timestamp(System.currentTimeMillis()));

            return reservationRepository.save(reservation);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de la réservation", e);
        }
    }

    public void updateComment(Long id, String comment) {
        try {
            String sql = "UPDATE reservation SET comment = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, comment);
                pstmt.setLong(2, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour du commentaire", e);
        }
    }
} 