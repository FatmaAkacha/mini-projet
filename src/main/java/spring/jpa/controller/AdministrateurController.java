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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import spring.jpa.model.Administrateur;
import spring.jpa.model.Employe;
import spring.jpa.model.Utilisateur;
import spring.jpa.repository.AdministrateurRepository;
import spring.jpa.repository.UtilisateurRepository;

@Controller 
@RequestMapping(value = "/admin")
public class AdministrateurController {
	
    @Autowired 
    private AdministrateurRepository adminRepos;

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
    	Page<Administrateur> pageAdministrateurs = adminRepos.findAll(PageRequest.of(p, s));
        int totalPages = pageAdministrateurs.getTotalPages();
        int[] pages = new int[totalPages];
        for (int i = 0; i < totalPages; i++) {
            pages[i] = i;
        }
        model.addAttribute("pages", pages);
    	model.addAttribute("pageCourante",p);
        model.addAttribute("pageAdministrateurs", pageAdministrateurs);
        model.addAttribute("currentPage", p);
        model.addAttribute("pageSize", s);

        addUserTypeToModel(model, username, type_utilisateur, dateE);

        return "admin";
    }

    @RequestMapping(value = "/form")
    public String formAdmin(Model model,
                            @RequestParam(name = "username", required = false) String username,
                            @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
                            @RequestParam(name = "dateE", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE) {
        model.addAttribute("admin", new Administrateur()); 
        addUserTypeToModel(model, username, type_utilisateur, dateE);
        return "formadmin"; 
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Model model,
                       @RequestParam("dateE") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE,
                       @ModelAttribute("admin") Administrateur admin, BindingResult result,
                       @RequestParam(name = "username", required = false) String username,
                       @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur) {

        if (dateE == null) {
            result.rejectValue("dateE", "required", "Date is required");
        }

        admin.setDateE(dateE);
        admin.setNbjc(30);

        validator.validate(admin, result);

        addUserTypeToModel(model, username, type_utilisateur, dateE);

       adminRepos.save(admin);

        String redirectUrl = "redirect:/admin/index?username=" + (username != null ? username : "") +
                             "&type_utilisateur=" + (type_utilisateur != null ? type_utilisateur : "") +
                             "&dateE=" + new SimpleDateFormat("yyyy-MM-dd").format(dateE);

        return redirectUrl;
    }


    private void addUserTypeToModel(Model model, String username, String typeUtilisateur, Date dateE) {
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
