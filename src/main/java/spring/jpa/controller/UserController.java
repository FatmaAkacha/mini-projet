package spring.jpa.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import spring.jpa.model.Utilisateur;
import spring.jpa.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) String type_utilisateur,@RequestParam(required = false) Date dateE) {
        Utilisateur user = userService.authenticate(username, password);
        if (user != null) {
            return new ModelAndView("redirect:/home")
                    .addObject("username", username)
                    .addObject("type_utilisateur", type_utilisateur)
            		.addObject("dateE", dateE);

            
        }
        return new ModelAndView("login").addObject("error", "Invalid username or password");
    }

	
	
	

}