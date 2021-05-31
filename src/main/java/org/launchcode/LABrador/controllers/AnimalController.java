package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.*;
import org.launchcode.LABrador.models.*;
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
    private UserRepository userRepository;

    @Autowired
    LabRepository labRepository;

    Logger logger = LoggerFactory.getLogger(AnimalController.class);

//    @GetMapping("{username}")
//    public String displayUserAnimal(@PathVariable String username, Model model, HttpServletRequest request) {
//        logger.info("666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666");
//        HttpSession session = request.getSession();
//        User userFromSession = authenticationController.getUserFromSession(session);
//        model.addAttribute("user", userFromSession);
//        model.addAttribute("title", "LAB_NAME Animal Colony");
//        model.addAttribute("users", userRepository.findByUsername(username));
//
//        int userId = userFromSession.getId();
//        List<Animal> colony = new ArrayList<>();
//        for (Animal animal : animalRepository.findAll()) {
//            if (animal.getUser() != null && animal.getUser().getId() == userId){
//                colony.add(animal);
//            }
//        }
//
//        User user = userRepository.findByUsername(username);
//        if (user.getLab() == null) {
//            user.setColony(colony);
//        }
//
//        model.addAttribute("animals", colony);
//        logger.info("777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777");
//        return "colony/index";
//    }

    @GetMapping("{username}/{labId}")
    public String displayLabAnimals(Model model, @PathVariable String username, @PathVariable(required = false) int labId, HttpServletRequest request) {
        logger.info("44444444444444444444444444444444444444444444444444444444444444444444444444444444444");
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

        if (userFromSession.getLab() != null) {
            labRepository.findLabById(labId).setColony(colony);
        }
        userFromSession.setColony(colony);


        model.addAttribute("animals", colony);
        logger.info("555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555");
        return "colony/index";
    }

    @GetMapping("add/{username}/{labId}")
    public String displayAddLabAnimalForm(Model model, @PathVariable String username, @PathVariable(required = false) int labId, HttpServletRequest request) {
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

//    @GetMapping("add")
//    public String displayAddUserAnimalForm(Model model, HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        User userFromSession = authenticationController.getUserFromSession(session);
//        model.addAttribute("user", userFromSession);
//
//        if (genotypeRepository.findByName("") == null) {
//            Genotype blankGenotype = new Genotype("");
//            genotypeRepository.save(blankGenotype);
//        }
//        model.addAttribute("title", "Add Entry");
//        model.addAttribute("genotype", genotypeRepository.findAll());
//        model.addAttribute(new Animal());
//        return "colony/add";
//    }

    @PostMapping("add/{username}/{labId}")
    public String processAddLabAnimalForm(@PathVariable String username, @PathVariable(required = false) int labId, @ModelAttribute @Valid Animal newAnimal, Errors errors, HttpServletRequest request, Model model) {
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

        Lab userLab = labRepository.findLabById(labId);
        newAnimal.setUser(userFromSession);

        if (userFromSession.getLab() != null) {
            newAnimal.setLab(userLab);
            colony.add(newAnimal);
            userLab.setColony(colony);
            animalRepository.save(newAnimal);
        }
        colony.add(newAnimal);
        animalRepository.save(newAnimal);


        model.addAttribute("lab", labRepository.findLabById(labId));
        model.addAttribute("user", userFromSession);
        model.addAttribute("title", "LAB_NAME Animal Colony");
        model.addAttribute("animals", colony);
        logger.info("AddAnimalForm process successful");
        return "colony/index";
    }

//    @PostMapping("add")
//    public String processAddUserAnimalForm(@ModelAttribute @Valid Animal newAnimal, Errors errors, HttpServletRequest request, Model model) {
//        logger.info("AddAnimalForm process pending.");
//        HttpSession session = request.getSession();
//        User userFromSession = authenticationController.getUserFromSession(session);
//
//        if (errors.hasErrors()) {
//            model.addAttribute("user", userFromSession);
//            model.addAttribute("title", "Add Entry");
//            model.addAttribute("genotype", genotypeRepository.findAll());
//            return "colony/add";
//        }
//
//        if (!newAnimal.getNotesDescription().isEmpty() && newAnimal.getNotesKeyword().isEmpty() ) {
//            errors.rejectValue("notesKeyword", "notesKeyword.isblank", "Must enter a keyword.");
//            model.addAttribute("user", userFromSession);
//            model.addAttribute("title", "Add Entry");
//            model.addAttribute("genotype", genotypeRepository.findAll());
//            return "colony/add";
//        }
//
//        int userId = userFromSession.getId();
//        List<Animal> colony = new ArrayList<>();
//        for (Animal animal : animalRepository.findAll()) {
//            if (animal.getUser() != null && animal.getUser().getId() == userId){
//                colony.add(animal);
//            }
//        }
//        colony.add(newAnimal);
//        newAnimal.setUser(userFromSession);
//        userFromSession.setColony(colony);
//        animalRepository.save(newAnimal);
//        model.addAttribute("user", userFromSession);
//        model.addAttribute("title", "USERNAME Animal Colony");
//        model.addAttribute("animals", colony);
//        logger.info("AddAnimalForm process successful");
//        return "colony/index";
//    }

    @GetMapping("edit/{animalId}/{username}/{labId}")
    public String displayEditLabAnimalForm(Model model, @PathVariable String username, @PathVariable(required = false) int animalId, @PathVariable int labId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        model.addAttribute("title", "Edit Entry");
        model.addAttribute(animalRepository.findById(animalId));
        model.addAttribute("genotype", genotypeRepository.findAll());
        model.addAttribute("lab", labRepository.findLabById(labId));
        logger.info("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        return "colony/edit";
    }

//    @GetMapping("edit/{animalId}")
//    public String displayEditUserAnimalForm(Model model, @PathVariable int animalId, HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        User userFromSession = authenticationController.getUserFromSession(session);
//        model.addAttribute("user", userFromSession);
//        model.addAttribute("title", "Edit Entry");
//        model.addAttribute(animalRepository.findById(animalId));
//        model.addAttribute("genotype", genotypeRepository.findAll());
//        return "colony/edit";
//    }

    @PostMapping("edit/{animalId}/{username}/{labId}")
    public String processEditLabAnimalForm(@ModelAttribute @Valid Animal animal, Errors errors, HttpServletRequest request, Model model, @PathVariable String username, @PathVariable(required = false) int animalId, @PathVariable int labId) {
        logger.info("22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
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
        model.addAttribute("lab", labRepository.findLabById(labId));
        logger.info("3333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
        return "redirect:/colony/{labId}";
    }

//    @PostMapping("edit/{animalId}")
//    public String processEditUserAnimalForm(@ModelAttribute @Valid Animal animal, Errors errors, HttpServletRequest request, Model model, @PathVariable int animalId) {
//        HttpSession session = request.getSession();
//        User userFromSession = authenticationController.getUserFromSession(session);
//
//        if (errors.hasErrors()) {
//            model.addAttribute("user", userFromSession);
//            model.addAttribute("title", "Edit Entry");
//            model.addAttribute("genotype", genotypeRepository.findAll());
//            return "colony/edit";
//        }
//
//        if (!animal.getNotesDescription().isEmpty() && animal.getNotesKeyword().isEmpty() ) {
//            errors.rejectValue("notesKeyword", "notesKeyword.isblank", "Must enter a keyword.");
//            model.addAttribute("user", userFromSession);
//            model.addAttribute("title", "Edit Entry");
//            model.addAttribute("genotype", genotypeRepository.findAll());
//            return "colony/edit";
//        }
//
//        Animal animalTmp = animalRepository.findById(animalId);
//        animalTmp.setTag(animal.getTag());
//        animalTmp.setCageNumber(animal.getCageNumber());
//        animalTmp.setCageType(animal.getCageType());
//        animalTmp.setSex(animal.getSex());
//        animalTmp.setDateOfBirth(animal.getDateOfBirth());
//        animalTmp.setGenotype(animal.getGenotype());
//        animalTmp.setLitter(animal.getLitter());
//        animalTmp.setNotesKeyword(animal.getNotesKeyword());
//        animalTmp.setNotesDescription(animal.getNotesDescription());
//        animalRepository.save(animalTmp);
//        return "redirect:../";
//    }

    @PostMapping("{username}/{labId}")
    public String processDeleteLabAnimalForm(@PathVariable String username, @PathVariable(required = false) int labId, Model model, HttpServletRequest request, @RequestParam(required = false) int[] animalIds) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        if (animalIds != null) {
            for (int id : animalIds) {
                animalRepository.deleteById(id);
            }
        }
        model.addAttribute("title", "LAB_NAME Animal Colony");
        return "redirect:/colony/{labId}";
    }

//    @PostMapping("{username}")
//    public String processDeleteUserAnimalForm(Model model, HttpServletRequest request, @RequestParam(required = false) int[] animalIds) {
//        HttpSession session = request.getSession();
//        User userFromSession = authenticationController.getUserFromSession(session);
//        model.addAttribute("user", userFromSession);
//
//        if (animalIds != null) {
//            for (int id : animalIds) {
//                animalRepository.deleteById(id);
//            }
//        }
//        model.addAttribute("title", "LAB_NAME Animal Colony");
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
