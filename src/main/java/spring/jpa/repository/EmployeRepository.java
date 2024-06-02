package spring.jpa.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.jpa.model.Employe;
public interface EmployeRepository extends
JpaRepository<Employe, Long> {
	
}