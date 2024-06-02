package spring.jpa.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.jpa.model.Administrateur;
public interface AdministrateurRepository extends
JpaRepository<Administrateur, Long> {}