package spring.jpa.model;

import java.util.Date;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("administrateur")
public class Administrateur extends Utilisateur  {

	public Administrateur() {
		super();
	}
	
	public Administrateur(Date dateE,Long id,String password, String code, String login, String nom,  String prenom,int nbjc) {
        super(dateE, id, password,code, login, nom, prenom,nbjc);
	    }
	

}
