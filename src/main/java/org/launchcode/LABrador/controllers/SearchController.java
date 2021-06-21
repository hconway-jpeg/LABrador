package org.launchcode.LABrador.controllers;


import org.launchcode.LABrador.data.AnimalRepository;
import org.launchcode.LABrador.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private AnimalRepository animalRepo;

    @Autowired
    private AuthenticationController authenticationController;


    @GetMapping
    public String searchByNotes(Model model, HttpServletRequest request, @RequestParam String query) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        model.addAttribute("title", "LAB_NAME Animal Colony");
        model.addAttribute("animals", animalRepo.findByNotesKeyword(query));
        return "colony/search";

    }


}

