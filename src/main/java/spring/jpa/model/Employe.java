package spring.jpa.model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("employe")
public class Employe extends Utilisateur {

    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL)
    private Collection<Conge> conges = new ArrayList<>();

    public Employe() {
        super();
    }

    public Employe(Date dateE, Long id, String password, String code, String login, String nom, String prenom,int nbjc) {
        super(dateE, id, password, code, login, nom, prenom,nbjc);
    }

    public Collection<Conge> getConges() {
        return conges;
    }

    public void addConge(Conge conge) {
        conges.add(conge);
        conge.setEmploye(this);
    }

    public void removeConge(Conge conge) {
        conges.remove(conge);
        conge.setEmploye(null);
    }
}
