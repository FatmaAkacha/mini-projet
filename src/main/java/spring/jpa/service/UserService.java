package spring.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.jpa.model.Utilisateur;
import spring.jpa.repository.UtilisateurRepository;

@Service
public class UserService {

	@Autowired
	private UtilisateurRepository userRepository;

	public Utilisateur authenticate(String username, String password) {
		Utilisateur user = userRepository.findByLogin(username);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}

	

}
