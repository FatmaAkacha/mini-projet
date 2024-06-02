package spring.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;

import java.util.Date;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE_UTILISATEUR")
@DiscriminatorValue("Utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Size(max = 50)
    private String code;

    @Column(name = "TYPE_UTILISATEUR", insertable = false, updatable = false)
    private String typeUtilisateur;

    @Column(length = 50)
    @Size(max = 50)
    private String nom;

    @Column(length = 50)
    @Size(max = 50)
    private String prenom;

    @Column(unique = true, length = 50)
    @Size(max = 50)
    private String login;

    @Column(length = 10)
    @Size(min = 6, max = 10)
    private String password;

    @Temporal(TemporalType.DATE)
    private Date dateE;
    
    private int nbjc=30;

    public int getNbjc() {
		return nbjc;
	}

	public void setNbjc(int nbjc) {
		this.nbjc = nbjc;
	}

	public Utilisateur() {}

    public Utilisateur(Date dateE, Long id, String password, String code, String login, String nom, String prenom,int nbjc) {
        super();
        this.id = id;
        this.code = code;
        this.dateE = dateE;
        this.login = login;
        this.nom = nom;
        this.password = password;
        this.prenom = prenom;
        this.nbjc=nbjc;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateE() {
        return dateE;
    }

    public void setDateE(Date dateE) {
        this.dateE = dateE;
    }

    public String getTypeUtilisateur() {
        return typeUtilisateur;
    }

    public void setTypeUtilisateur(String typeUtilisateur) {
        this.typeUtilisateur = typeUtilisateur;
    }
}
