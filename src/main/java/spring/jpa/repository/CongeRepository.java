package spring.jpa.repository;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import spring.jpa.model.Conge;
import spring.jpa.model.Employe;
public interface CongeRepository extends
JpaRepository<Conge, Long> {
	List<Conge> findByEtatLike(String mc);
	@Query("SELECT c FROM Conge c WHERE YEAR(c.dateDebut) = ?1 AND c.etat LIKE ?2")
	List<Conge> findByDateDebutYearAndEtatLike(Integer annee, String mc);
	List<Conge> findByEmploye(Employe foundUser);
	@Query("SELECT c FROM Conge c WHERE YEAR(c.dateDebut) = ?2 AND c.etat LIKE ?3 AND c.employe.login = ?1")
	List<Conge> findByEmployeLoginAndDateDebutYearAndEtatLike(String login, int year, String etat);
	List<Conge> findByEmployeLoginAndEtatLike(String login, String etat);
	List<Conge> findByEmployeLogin(String mc2);
	List<Conge> findByEtat(String etat);
	}
