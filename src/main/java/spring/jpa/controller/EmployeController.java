package spring.jpa.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import org.springframework.ui.Model;

import spring.jpa.model.Conge;
import spring.jpa.model.Employe;
import spring.jpa.model.Utilisateur;
import spring.jpa.repository.CongeRepository;
import spring.jpa.repository.EmployeRepository;
import spring.jpa.repository.UtilisateurRepository;

@Controller 
@RequestMapping(value = "/employe")
public class EmployeController {

    @Autowired 
    private EmployeRepository employeRepos;
    
    @Autowired 
    private CongeRepository congeRepos;

    @Autowired
    private UtilisateurRepository userRepository;


    @Autowired
    private Validator validator;

    @RequestMapping(value = "/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int p,
                        @RequestParam(name = "size", defaultValue = "5") int s,
                        @RequestParam(name = "username", required = false) String username,
                        @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
                        @RequestParam(name = "dateE", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE) {
        Page<Employe> pageEmployes = employeRepos.findAll(PageRequest.of(p, s));
        int totalPages = pageEmployes.getTotalPages();
        int[] pages = new int[totalPages];
        for (int i = 0; i < totalPages; i++) {
            pages[i] = i;
        }
        model.addAttribute("pages", pages);
        model.addAttribute("pageEmployes", pageEmployes);
        model.addAttribute("currentPage", p);
        model.addAttribute("pageSize", s);

        addUserTypeToModel(model, username, type_utilisateur, dateE);
        return "employes";
    }

    @RequestMapping(value = "/form")
    public String formEmploye(Model model,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
            @RequestParam(name = "dateE", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE) {
model.addAttribute("employe", new Employe()); 
addUserTypeToModel(model, username, type_utilisateur, dateE);
return "formemploye"; 
}

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Model model,
                       @RequestParam("dateE") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE,
                       @ModelAttribute("employe") Employe employe, BindingResult result,
                       @RequestParam(name = "username", required = false) String username,
                       @RequestParam(name = "type_utilisateur", required = false) String typeUtilisateur) {

        // Check if dateE is provided
        if (dateE == null) {
            result.rejectValue("dateE", "required", "Date is required");
        }

        employe.setDateE(dateE);
        employe.setNbjc(30);

        // Validate the employe object
        validator.validate(employe, result);

     
        // Save the employe object to the repository
        employeRepos.save(employe);

        // Redirect to index page with parameters, formatting the dateE
        String redirectUrl = "redirect:/employe/index?username=" + (username != null ? username : "") +
                             "&type_utilisateur=" + (typeUtilisateur != null ? typeUtilisateur : "") +
                             "&dateE=" + new SimpleDateFormat("yyyy-MM-dd").format(dateE);

        return redirectUrl;
    }


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    private void addUserTypeToModel(Model model, String username, String type_utilisateur, Date dateE) {
        if (username != null) {
            Utilisateur user = userRepository.findByLogin(username);
            if (user != null) {
                model.addAttribute("username", user.getLogin());
                model.addAttribute("type_utilisateur", user.getTypeUtilisateur());
                model.addAttribute("dateE", user.getDateE());
            } else {
                model.addAttribute("error", "Utilisateur non trouvé pour le login : " + username);
            }
        }
    }
}
