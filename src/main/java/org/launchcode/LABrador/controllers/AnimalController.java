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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        //prevent access to non-lab members and users not affiliated with a lab
        if (!userFromSession.getLab().contains(labRepository.findLabById(labId)) || userFromSession.getLab() == null) {
            List<Lab> currentLabs = userFromSession.getLab();
            model.addAttribute("labs", currentLabs);
            model.addAttribute("allLabs", labRepository.findAll());
            model.addAttribute("user", userFromSession);

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

        //prevent access to non-lab members and users not affiliated with a lab
        if (!userFromSession.getLab().contains(labRepository.findLabById(labId)) || userFromSession.getLab() == null) {
            List<Lab> currentLabs = userFromSession.getLab();
            model.addAttribute("labs", currentLabs);
            model.addAttribute("allLabs", labRepository.findAll());
            model.addAttribute("user", userFromSession);

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

        //prevent access to non-lab members and users not affiliated with a lab
        if (!userFromSession.getLab().contains(labRepository.findLabById(labId)) || userFromSession.getLab() == null) {
            List<Lab> currentLabs = userFromSession.getLab();
            model.addAttribute("labs", currentLabs);
            model.addAttribute("allLabs", labRepository.findAll());
            model.addAttribute("user", userFromSession);

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

    @GetMapping("id/{labId}")
    public String sortById(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));

        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by("id")));

        return "colony/sort";
    }
    @GetMapping("id2/{labId}")
    public String sortByIdD(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));
//
        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by(Sort.Direction.DESC,"id")));

        return "colony/sort";
    }

    @GetMapping("tag/{labId}")
    public String sortByTag(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));

        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by("tag")));

        return "colony/sort";
    }
    @GetMapping("tag2/{labId}")
    public String sortByTagD(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));
//
        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by(Sort.Direction.DESC,"tag")));

        return "colony/sort";
    }

    @GetMapping("cagenumber/{labId}")
    public String sortByCageNumber(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));

        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by("cageNumber")));

        return "colony/sort";
    }
    @GetMapping("cagenumber2/{labId}")
    public String sortByCageNumberD(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));
//
        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by(Sort.Direction.DESC,"cageNumber")));

        return "colony/sort";
    }

    @GetMapping("type/{labId}")
    public String sortByCageType(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));

        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by("cageType")));

        return "colony/sort";
    }
    @GetMapping("type2/{labId}")
    public String sortByCageTypeD(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));
//
        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by(Sort.Direction.DESC,"cageType")));

        return "colony/sort";
    }

    @GetMapping("sex/{labId}")
    public String sortBySex(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));

        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by("sex")));

        return "colony/sort";
    }
    @GetMapping("sex2/{labId}")
    public String sortBySexD(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));
//
        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by(Sort.Direction.DESC,"sex")));

        return "colony/sort";
    }

    @GetMapping("dateofbirth/{labId}")
    public String sortByDateOfBirth(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));

        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by("dateOfBirth")));

        return "colony/sort";
    }
    @GetMapping("dateofbirth2/{labId}")
    public String sortByDateOfBirthD(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));
//
        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by(Sort.Direction.DESC,"dateOfBirth")));

        return "colony/sort";
    }

    @GetMapping("genotype1/{labId}")
    public String sortByGenotype(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));

        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by("genotype.name")));

        return "colony/sort";
    }
    @GetMapping("genotype2/{labId}")
    public String sortByGenotype2(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));
//
        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by(Sort.Direction.DESC,"genotype.name")));

        return "colony/sort";
    }

    @GetMapping("litter/{labId}")
    public String sortByLitter(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));

        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by("litter")));

        return "colony/sort";
    }
    @GetMapping("litter2/{labId}")
    public String sortByLitterD(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));
//
        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by(Sort.Direction.DESC,"litter")));

        return "colony/sort";
    }

    @GetMapping("notes/{labId}")
    public String sortByNotes(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));

        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by("notesKeyword")));

        return "colony/sort";
    }
    @GetMapping("notes2/{labId}")
    public String sortByNotes2(@PathVariable int labId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("title", labName + " Animal Colony");
        model.addAttribute("lab", labRepository.findLabById(labId));
//
        model.addAttribute("animals", animalRepository.findByLabId(labId, Sort.by(Sort.Direction.DESC,"notesKeyword")));

        return "colony/sort";
    }
}
