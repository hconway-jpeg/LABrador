package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.AnimalRepository;
import org.launchcode.LABrador.data.GenotypeRepository;
import org.launchcode.LABrador.data.LabRepository;
import org.launchcode.LABrador.models.Animal;
import org.launchcode.LABrador.models.Genotype;
import org.launchcode.LABrador.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("colony")
public class AnimalController {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private GenotypeRepository genotypeRepository;

    @Autowired
    LabRepository labRepository;

    Logger logger = LoggerFactory.getLogger(AnimalController.class);

    @GetMapping
    public String displayUserAnimal(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        model.addAttribute("title", "LAB_NAME Animal Colony");

        int userId = userFromSession.getId();
        List<Animal> colony = new ArrayList<>();
        for (Animal animal : animalRepository.findAll()) {
            if (animal.getUser() != null && animal.getUser().getId() == userId){
                colony.add(animal);
            }
        }
        model.addAttribute("animals", colony);
        return "colony/index";
    }

    @GetMapping("{labId}")
    public String displayLabAnimals(Model model, @PathVariable int labId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        model.addAttribute("title", "LAB_NAME Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));

        List<Animal> colony = new ArrayList<>();
        for (Animal animal : animalRepository.findAll()) {
            if (animal.getLab() != null && animal.getLab().getId() == labId){
                colony.add(animal);
            }
        }
        labRepository.findLabById(labId).setColony(colony);
        model.addAttribute("animals", colony);
        return "colony/index";
    }

    @GetMapping("add/{labId}")
    public String displayAddLabAnimalForm(Model model, @PathVariable int labId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        if (genotypeRepository.findByName("") == null) {
            Genotype blankGenotype = new Genotype("");
            genotypeRepository.save(blankGenotype);
        }

        model.addAttribute("title", "Add Entry");
        model.addAttribute("genotype", genotypeRepository.findAll());
        model.addAttribute("lab", labRepository.findLabById(labId));
        model.addAttribute(new Animal());
        return "colony/add";
    }

    @GetMapping("add")
    public String displayAddUserAnimalForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        if (genotypeRepository.findByName("") == null) {
            Genotype blankGenotype = new Genotype("");
            genotypeRepository.save(blankGenotype);
        }

        model.addAttribute("title", "Add Entry");
        model.addAttribute("genotype", genotypeRepository.findAll());
        model.addAttribute(new Animal());
        return "colony/add";
    }

    @PostMapping("add/{labId}")
    public String processAddLabAnimalForm(@PathVariable int labId, @ModelAttribute @Valid Animal newAnimal, Errors errors, HttpServletRequest request, Model model) {
        logger.info("AddAnimalForm process pending.");
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Add Entry");
            model.addAttribute("genotype", genotypeRepository.findAll());
            return "colony/add";
        }

        if (!newAnimal.getNotesDescription().isEmpty() && newAnimal.getNotesKeyword().isEmpty() ) {
            errors.rejectValue("notesKeyword", "notesKeyword.isblank", "Must enter a keyword.");
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Add Entry");
            model.addAttribute("genotype", genotypeRepository.findAll());
            return "colony/add";
        }

        List<Animal> colony = new ArrayList<>();
        for (Animal animal : animalRepository.findAll()) {
            if (animal.getLab() != null && animal.getLab().getId() == labId){
                colony.add(animal);
            }
        }
        newAnimal.setLab(labRepository.findLabById(labId));
        colony.add(newAnimal);
        labRepository.findLabById(labId).setColony(colony);
        animalRepository.save(newAnimal);
        model.addAttribute("lab", labRepository.findLabById(labId));
        model.addAttribute("user", userFromSession);
        model.addAttribute("title", "LAB_NAME Animal Colony");
        model.addAttribute("animals", colony);
        logger.info("AddAnimalForm process successful");
        return "colony/index";
    }

    @PostMapping("add")
    public String processAddUserAnimalForm(@ModelAttribute @Valid Animal newAnimal, Errors errors, HttpServletRequest request, Model model) {
        logger.info("AddAnimalForm process pending.");
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Add Entry");
            model.addAttribute("genotype", genotypeRepository.findAll());
            return "colony/add";
        }

        if (!newAnimal.getNotesDescription().isEmpty() && newAnimal.getNotesKeyword().isEmpty() ) {
            errors.rejectValue("notesKeyword", "notesKeyword.isblank", "Must enter a keyword.");
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Add Entry");
            model.addAttribute("genotype", genotypeRepository.findAll());
            return "colony/add";
        }

        int userId = userFromSession.getId();
        List<Animal> colony = new ArrayList<>();
        for (Animal animal : animalRepository.findAll()) {
            if (animal.getUser() != null && animal.getUser().getId() == userId){
                colony.add(animal);
            }
        }

        colony.add(newAnimal);
        newAnimal.setUser(userFromSession);
        userFromSession.setColony(colony);
        animalRepository.save(newAnimal);
        model.addAttribute("user", userFromSession);
        model.addAttribute("title", "USERNAME Animal Colony");
        model.addAttribute("animals", colony);
        logger.info("AddAnimalForm process successful");
        return "colony/index";
    }

    @GetMapping("edit/{animalId}")
    public String displayEditAnimalForm(Model model, @PathVariable int animalId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "Edit Entry");
        model.addAttribute(animalRepository.findById(animalId));
        model.addAttribute("genotype", genotypeRepository.findAll());
        return "colony/edit";
    }

    @PostMapping("edit/{animalId}")
    public String processEditAnimalForm(@ModelAttribute @Valid Animal animal, Errors errors, HttpServletRequest request, Model model, @PathVariable int animalId) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Edit Entry");
            model.addAttribute("genotype", genotypeRepository.findAll());
            return "colony/edit";
        }

        if (!animal.getNotesDescription().isEmpty() && animal.getNotesKeyword().isEmpty() ) {
            errors.rejectValue("notesKeyword", "notesKeyword.isblank", "Must enter a keyword.");
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Edit Entry");
            model.addAttribute("genotype", genotypeRepository.findAll());
            return "colony/edit";
        }

        Animal animalTmp = animalRepository.findById(animalId);

        animalTmp.setTag(animal.getTag());
        animalTmp.setCageNumber(animal.getCageNumber());
        animalTmp.setCageType(animal.getCageType());
        animalTmp.setSex(animal.getSex());
        animalTmp.setDateOfBirth(animal.getDateOfBirth());
        animalTmp.setGenotype(animal.getGenotype());
        animalTmp.setLitter(animal.getLitter());
        animalTmp.setNotesKeyword(animal.getNotesKeyword());
        animalTmp.setNotesDescription(animal.getNotesDescription());

        animalRepository.save(animalTmp);

        return "redirect:../";
    }

    @PostMapping
    public String processDeleteAnimalForm(Model model, HttpServletRequest request, @RequestParam(required = false) int[] animalIds) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        if (animalIds != null) {
            for (int id : animalIds) {
                animalRepository.deleteById(id);
            }
        }
        model.addAttribute("title", "LAB_NAME Animal Colony");
        model.addAttribute("animals", animalRepository.findAll());
        return "colony/index";
    }

//    @GetMapping("tag/{labId}")
//    public String labSortByTag(Model model, HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        User userFromSession = authenticationController.getUserFromSession(session);
//        model.addAttribute("user", userFromSession);
//
//        model.addAttribute("title", "tag sort");
//        model.addAttribute("animals", animalRepository.findAll(Sort.by("tag")));
//        return "colony/index";
//    }
//
//    @GetMapping("tag")
//    public String userSortByTag(Model model, HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        User userFromSession = authenticationController.getUserFromSession(session);
//        model.addAttribute("user", userFromSession);
//        model.addAttribute("title", "tag sort");
//
//        int userId = userFromSession.getId();
//        List<Animal> colony = new ArrayList<>();
//
//        for (Animal animal : animalRepository.findAll()) {
//            if (animal.getUser() != null && animal.getUser().getId() == userId){
//                colony.add(animal);
//            }
//        }
//        Iterable<Animal> animals = colony;
//
//        model.addAttribute("animals", colony.(Sort.by("tag", colony.));
//        return "colony/index";
//    }

    @GetMapping("tag")
    public String sortByTag(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "tag sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("tag")));
        return "colony/index";
    }

    @GetMapping("cagenumber")
    public String sortByCageNumber(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "cage number sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("cageNumber")));
        return "colony/index";
    }

    @GetMapping("type")
    public String sortByCageType(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "cage type sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("cageType")));
        return "colony/index";
    }

    @GetMapping("sex")
    public String sortBySex(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "sex sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("sex")));
        return "colony/index";
    }

    @GetMapping("dateofbirth")
    public String sortByDateOfBirth(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "date of birth sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("dateOfBirth")));
        return "colony/index";
    }


    @GetMapping("genotype1")
    public String sortByGenotype(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "genotype sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("genotype")));
        return "colony/index";
    }

    @GetMapping("litter")
    public String sortByLitter(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "litter sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("litter")));
        return "colony/index";
    }

}
