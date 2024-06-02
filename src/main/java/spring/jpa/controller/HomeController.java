package spring.jpa.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

import spring.jpa.model.Utilisateur;
import spring.jpa.repository.UtilisateurRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

@Controller
public class HomeController {

    @Autowired
    private UtilisateurRepository userRepository;
    @GetMapping("/home")
    public String home(@RequestParam(required = false) String username,
                       @RequestParam(required = false) String type_utilisateur,
                       @RequestParam(required = false) LocalDate dateE,
                       @RequestParam(required = false) Integer nbjc,
                       Model model) {

        Utilisateur user = userRepository.findByLogin(username);

        if (user != null) {
            model.addAttribute("username", user.getLogin());
            model.addAttribute("type_utilisateur", user.getTypeUtilisateur());
            model.addAttribute("dateE", user.getDateE());
            model.addAttribute("nbjc", user.getNbjc());
            System.out.println(dateE);

            if (dateE != null) {
                LocalDate currentDate = LocalDate.now();
                System.out.println(currentDate);
                Period period = Period.between(dateE, currentDate);
                System.out.println(period);
                int years = period.getYears();
                System.out.println(years);
                if (years >= 1) {
                    model.addAttribute("workedOneYear", true);
                } else {
                    model.addAttribute("workedOneYear", false);
                }
            } else {
                model.addAttribute("workedOneYear", false); // dateE is null, so user hasn't worked for a year
            }

            return "accueil";
        } else {
            model.addAttribute("error", "Utilisateur non trouvé pour le login : " + username);
            return "error";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login"; 
    }
}

