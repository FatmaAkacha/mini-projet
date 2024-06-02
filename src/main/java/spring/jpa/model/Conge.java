package spring.jpa.model;



import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Conge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String description;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;  

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Date getDateRupture() {
		return dateRupture;
	}

	public void setDateRupture(Date dateRupture) {
		this.dateRupture = dateRupture;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	@Temporal(TemporalType.DATE)
    private Date dateFin;      

    public Conge(Long id, String description, Date dateDebut,Date dateFin, Date dateRupture, String etat,
			Employe employe) {
		super();
		this.id = id;
		this.description = description;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.dateRupture = dateRupture;
		this.etat = etat;
		this.employe = employe;
	}

	@Temporal(TemporalType.DATE)
    private Date dateRupture;

    @Column(length = 20)
    private String etat;
    
    public Employe getEmploye() {
		return employe;
	}

	public void setEmploye(Employe employe) {
		this.employe = employe;
	}

	@ManyToOne
	private Employe employe;
    
 

    public Conge() {
        super();
    }
   
}
