package spring.jpa.controller;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import spring.jpa.model.Conge;
import spring.jpa.model.Employe;
import spring.jpa.model.Utilisateur;
import spring.jpa.repository.CongeRepository;
import spring.jpa.repository.EmployeRepository;
import spring.jpa.repository.UtilisateurRepository;

@Controller
@RequestMapping(value = "/conge")
public class CongeController {

    @Autowired
    private CongeRepository congeRepos;

    @Autowired
    private EmployeRepository empRepos;

    @Autowired
    private UtilisateurRepository userRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private int nbjc = 30;

    @ModelAttribute("motCle")
    public String motCle() {
        return "";
    }
    @ModelAttribute("motCle2")
    public String motCle2() {
        return "";
    }

    
    @RequestMapping(value = "/index")
    public String index(Model model,
                        @RequestParam(name = "etat", defaultValue = "") String etat,
                        @RequestParam(name = "annee", required = false) Integer annee,
                        @RequestParam(name = "username", required = false) String username,
                        @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
                        @RequestParam(name = "dateE", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE) {
        addUserTypeToModel(model, username, type_utilisateur, dateE);
System.out.println(username);
        List<Conge> conges;

        // Remove exception handling temporarily
        // try {
        if ("employe".equals(type_utilisateur) && username != null) {
            if (annee != null) {
                conges = congeRepos.findByEmployeLoginAndDateDebutYearAndEtatLike(username, annee, "%" + etat + "%");
            } else {
                conges = congeRepos.findByEmployeLoginAndEtatLike(username, "%" + etat + "%");
            }
        } else {
            if (annee != null) {
                conges = congeRepos.findByDateDebutYearAndEtatLike(annee, "%" + etat + "%");
            } else {
                if (!etat.isEmpty()) {
                    conges = congeRepos.findByEtatLike("%" + etat + "%");
                } else {
                    conges = congeRepos.findAll();
                }
            }
            conges = congeRepos.findAll();
        }
        
        // } catch (Exception e) {
        //    e.printStackTrace(); // Log or handle the exception appropriately
        //    return "error";
        // }

        model.addAttribute("conges", conges);
        model.addAttribute("etat", etat);

        addUserTypeToModel(model, username, type_utilisateur, dateE);

        return "conges";
    }
    @RequestMapping(value = "/index", params = {"motCle2"})
    public String findByEmployeLogin(Model model,
                                     @RequestParam(name = "motCle2") String mc2,
                                     @RequestParam(name = "username", required = false) String username,
                                     @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
                                     @RequestParam(name = "dateE", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE) {

        List<Conge> conges = congeRepos.findByEmployeLogin(mc2);
        Collections.sort(conges, (c1, c2) -> c2.getDateDebut().compareTo(c1.getDateDebut()));


        model.addAttribute("conges", conges);
        addUserTypeToModel(model, username, type_utilisateur, dateE);

        return "conges";
    }


    @RequestMapping(value = "/form")
    public String formConge(Model model,
                            @RequestParam(name = "username", required = false) String username,
                            @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
                            @RequestParam(name = "dateE", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE) {
        List<Employe> employe = empRepos.findAll();
        model.addAttribute("employe", employe);
        model.addAttribute("conge", new Conge());
        addUserTypeToModel(model, username, type_utilisateur, dateE);

        return "formconge";
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Model model,
                       @RequestParam("dateDebut") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
                       @RequestParam("dateFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin,
                       @Valid @ModelAttribute("conge") Conge conge, BindingResult result,
                       @RequestParam(name = "username", required = false) String username,
                       @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
                       @RequestParam(name = "dateE", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE) {

        // Debugging: Print the dates to check if they are parsed correctly
        System.out.println("Date Debut: " + dateDebut);
        System.out.println("Date Fin: " + dateFin);

        // Validate if dateDebut is before dateFin
        if (dateDebut == null || dateFin == null || dateDebut.after(dateFin)) {
            if (dateDebut == null) {
                result.rejectValue("dateDebut", "error.conge", "La date de début est requise et doit être valide");
            }
            if (dateFin == null) {
                result.rejectValue("dateFin", "error.conge", "La date de fin est requise et doit être valide");
            }
            if (dateDebut != null && dateFin != null && dateDebut.after(dateFin)) {
                result.rejectValue("dateDebut", "error.conge", "La date de début doit être antérieure à la date de fin");
                result.rejectValue("dateFin", "error.conge", "La date de fin doit être postérieure à la date de début");
            }
            List<Employe> employeList = empRepos.findAll();
            model.addAttribute("employe", employeList);
            addUserTypeToModel(model, username, type_utilisateur, dateE);
            return "formconge";
        }

        // Calculate the number of days between dateDebut and dateFin
        LocalDate localDateDebut = dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateFin = dateFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long nbJours = ChronoUnit.DAYS.between(localDateDebut, localDateFin) ; 

        // Debugging: Print the number of days calculated
        System.out.println("Nombre de jours: " + nbJours);
       

        Employe employe = conge.getEmploye();
        if (employe == null) {
            result.rejectValue("employe", "error.conge", "L'employé est requis");
            List<Employe> employeList = empRepos.findAll();
            model.addAttribute("employe", employeList);
            addUserTypeToModel(model, username, type_utilisateur, dateE);
            return "formconge";
        }

        // Debugging: Check employee's available days
        System.out.println("Employe nbjc (available days): " + employe.getNbjc());
        if (nbJours > employe.getNbjc()) {
            result.rejectValue("dateFin", "error.conge", "La durée du congé ne peut pas dépasser " + employe.getNbjc() + " jours.");
            List<Employe> employeList = empRepos.findAll();
            model.addAttribute("employe", employeList);
            addUserTypeToModel(model, username, type_utilisateur, dateE);
            return "formconge";
        }

        employe.setNbjc(employe.getNbjc() - (int) nbJours);

    
        conge.setEtat("Sollicitee");

        empRepos.save(employe);
        congeRepos.save(conge);
        addUserTypeToModel(model, username, type_utilisateur, dateE);
        return "redirect:/conge/index";
    }







    @PostMapping(value = "/arrete")
    public String arreteConge(Model model,
                              @RequestParam(name = "username", required = false) String username,
                              @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
                              @RequestParam(name = "dateE", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE,
                              @RequestParam("id") Long id) {
        Conge conge = congeRepos.findById(id).orElse(null);
        model.addAttribute("conge", conge);
        addUserTypeToModel(model, username, type_utilisateur, dateE);
        return "arrete";
    }

    @PostMapping(value = "/submit")
    public String submitConge(Model model,
                              @RequestParam(name = "username", required = false) String username,
                              @RequestParam(name = "dateRupture", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateRupture,
                              @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
                              @RequestParam(name = "dateE", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE,
                              @RequestParam("id") Long id) {
        Conge conge = congeRepos.findById(id).orElse(null);
        if (conge != null) {
            conge.setEtat("arrete");
            conge.setDateRupture(dateRupture);
            congeRepos.save(conge);
        }
        model.addAttribute("conge", conge);
        addUserTypeToModel(model, username, type_utilisateur, dateE);
        String redirectUrl = "redirect:/conge/index?username=" + (username != null ? username : "") +
                             "&type_utilisateur=" + (type_utilisateur != null ? type_utilisateur : "") +
                             "&dateE=" + new SimpleDateFormat("yyyy-MM-dd").format(dateE);

        return redirectUrl;
    }

    @RequestMapping("/valider")
    public String validerConge(Model model,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
            @RequestParam(name = "dateE", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE,
            @RequestParam("id") Long id)  {
        Conge conge = congeRepos.findById(id).orElse(null);
        if (conge != null) {
            System.out.println("Etat actuel: " + conge.getEtat());
            if ("Sollicitee".equals(conge.getEtat())) {
                conge.setEtat("VALIDE");
                congeRepos.save(conge);
            }
        }

        String redirectUrl = "redirect:/conge/index?username=" + (username != null ? username : "") +
                             "&type_utilisateur=" + (type_utilisateur != null ? type_utilisateur : "") +
                             "&dateE=" + new SimpleDateFormat("yyyy-MM-dd").format(dateE);

        return redirectUrl;
    }

    @PostMapping("/refuser")
    public String refuserConge(Model model,
    		@RequestParam("id") Long id, @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
            @RequestParam(name = "dateE", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE) {
        Conge conge = congeRepos.findById(id).orElse(null);
        if (conge != null) {
            System.out.println("Etat actuel: " + conge.getEtat());
            if ("Sollicitee".equals(conge.getEtat()) || "VALIDE".equals(conge.getEtat())) {
                conge.setEtat("REFUSE");
                congeRepos.save(conge);
            }
        }
        addUserTypeToModel(model, username, type_utilisateur, dateE);

        String redirectUrl = "redirect:/conge/index?username=" + (username != null ? username : "") +
                             "&type_utilisateur=" + (type_utilisateur != null ? type_utilisateur : "") +
                             "&dateE=" + new SimpleDateFormat("yyyy-MM-dd").format(dateE);

        return redirectUrl;
    }

    @PostMapping("/annuler")
    public String annulerConge(Model model,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "type_utilisateur", required = false) String type_utilisateur,
            @RequestParam(name = "dateE", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateE,
            @RequestParam("id") Long id) {
        Conge conge = congeRepos.findById(id).orElse(null);
        if (conge != null) {
            System.out.println("Etat actuel: " + conge.getEtat());
            if ("Sollicitee".equals(conge.getEtat())) {
                conge.setEtat("ANNULE");
                congeRepos.save(conge);
            }
        }
        addUserTypeToModel(model, username, type_utilisateur, dateE);
        String redirectUrl = "redirect:/conge/index?username=" + (username != null ? username : "") +
                             "&type_utilisateur=" + (type_utilisateur != null ? type_utilisateur : "") +
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
    @Scheduled(fixedRate = 20000)
    @Transactional
    public void updateCongesEnCours() {
        LocalDate today = LocalDate.now();
        List<Conge> conges = congeRepos.findByEtat("VALIDE");

        for (Conge conge : conges) {
            Date dateDebutSQL = conge.getDateDebut();
            java.util.Date dateDebutUtil = new java.util.Date(dateDebutSQL.getTime());

            LocalDate dateDebut = dateDebutUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (dateDebut.isEqual(today) || dateDebut.isBefore(today)) {
                conge.setEtat("EN_COURS");
                congeRepos.save(conge);
            }
        }
    }
	
	  @PostConstruct
	  @Transactional public void updateCongesEnCoursToFinished() { LocalDate today
	  = LocalDate.now(); List<Conge> conges = congeRepos.findByEtat("EN_COURS");
	  
	  for (Conge conge : conges) { Date dateFinSQL = conge.getDateFin();
	  java.util.Date dateFinUtil = new java.util.Date(dateFinSQL.getTime());
	  
	  LocalDate dateFin =
	  dateFinUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	  
	  if (dateFin.isBefore(today)) { conge.setEtat("FINI"); congeRepos.save(conge);
	  } } }
	  
	  @PostConstruct
	  @Transactional public void updateCongesSolliciteeToRefuse() { LocalDate today
	  = LocalDate.now(); List<Conge> conges = congeRepos.findByEtat("Sollicitee");
	  
	  for (Conge conge : conges) { Date dateFinSQL = conge.getDateFin();
	  java.util.Date dateFinUtil = new java.util.Date(dateFinSQL.getTime());
	  
	  LocalDate dateFin =
	  dateFinUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	  
	  if (dateFin.isBefore(today)) { conge.setEtat("REFUSE");
	  congeRepos.save(conge); } } }
	 

   
       
    }
