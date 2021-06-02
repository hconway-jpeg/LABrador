package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.*;
import org.launchcode.LABrador.models.*;
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
    private LabRepository labRepository;


    @GetMapping("{labId}")
    public String displayLabAnimals(Model model, @PathVariable int labId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        //prevent access to non-lab members
        if (!userFromSession.getLab().contains(labRepository.findLabById(labId))) {
            List<Lab> currentLabs = userFromSession.getLab();
            model.addAttribute("labs", currentLabs);
            model.addAttribute("allLabs", labRepository.findAll());
            return "lab/index";
        }

        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");

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

        //prevent access to non-lab members
        if (!userFromSession.getLab().contains(labRepository.findLabById(labId))) {
            List<Lab> currentLabs = userFromSession.getLab();
            model.addAttribute("labs", currentLabs);
            model.addAttribute("allLabs", labRepository.findAll());
            return "lab/index";
        }


        List<Genotype> genotypes = new ArrayList<>();
        for (Genotype genotype : genotypeRepository.findAll()) {
            if (genotype.getLab() != null && genotype.getLab().getId() == labId){
                genotypes.add(genotype);
            }
        }
        labRepository.findLabById(labId).setGenotypes(genotypes);


        model.addAttribute("title", "Add Entry");
        model.addAttribute("genotype", genotypes);
        model.addAttribute("lab", labRepository.findLabById(labId));
        model.addAttribute(new Animal());

        return "colony/add";
    }

    @PostMapping("add/{labId}")
    public String processAddLabAnimalForm(@PathVariable int labId, @ModelAttribute @Valid Animal newAnimal, Errors errors, HttpServletRequest request, Model model) {
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
        newAnimal.setLab(userLab);
        colony.add(newAnimal);
        userLab.setColony(colony);
        animalRepository.save(newAnimal);

        model.addAttribute("lab", labRepository.findLabById(labId));
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("animals", colony);
        return "colony/index";
    }


    @GetMapping("edit/{animalId}/{labId}")
    public String displayEditLabAnimalForm(Model model, @PathVariable int animalId, @PathVariable int labId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        //prevent access to non-lab members
        if (!userFromSession.getLab().contains(labRepository.findLabById(labId))) {
            List<Lab> currentLabs = userFromSession.getLab();
            model.addAttribute("labs", currentLabs);
            model.addAttribute("allLabs", labRepository.findAll());
            return "lab/index";
        }

        List<Genotype> genotypes = new ArrayList<>();
        for (Genotype genotype : genotypeRepository.findAll()) {
            if (genotype.getLab() != null && genotype.getLab().getId() == labId){
                genotypes.add(genotype);
            }
        }
        labRepository.findLabById(labId).setGenotypes(genotypes);

        model.addAttribute("title", "Edit Entry");
        model.addAttribute(animalRepository.findById(animalId));
        model.addAttribute("genotype", genotypes);
        model.addAttribute("lab", labRepository.findLabById(labId));
        return "colony/edit";
    }


    @PostMapping("edit/{animalId}/{labId}")
    public String processEditLabAnimalForm(@ModelAttribute @Valid Animal animal, Errors errors, HttpServletRequest request, Model model, @PathVariable int animalId, @PathVariable int labId) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

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
        return "redirect:/colony/{labId}";
    }

    @PostMapping("{labId}")
    public String processDeleteLabAnimalForm(@PathVariable int labId, Model model, HttpServletRequest request, @RequestParam(required = false) int[] animalIds) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        if (animalIds != null) {
            for (int id : animalIds) {
                animalRepository.deleteById(id);
            }
        }
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        return "redirect:/colony/{labId}";
    }



        //sort methods

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
