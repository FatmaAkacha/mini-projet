package spring.jpa.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.jpa.model.Utilisateur;
public interface UtilisateurRepository extends
JpaRepository<Utilisateur, Long> {
	/* Optional<Utilisateur> findByLogin(String username); */
    Utilisateur findByLoginAndPassword(String username, String motdepasse);
    Utilisateur findByLogin(String username);
}