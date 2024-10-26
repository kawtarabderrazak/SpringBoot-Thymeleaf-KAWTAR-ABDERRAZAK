package com.example.demo.controller;

import com.example.demo.entities.User;
import com.example.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Affiche le formulaire de création d'utilisateur
    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";  // Vue pour le formulaire d'ajout d'utilisateur
    }

    // Gère l'ajout d'un utilisateur après validation
    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";  // Retourne à la vue de formulaire si la validation échoue
        }
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());  // Met à jour la liste d'utilisateurs
        return "index";  // Redirige vers la vue principale avec la liste mise à jour
    }

    // Affiche le formulaire de mise à jour de l'utilisateur
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);  // Passe l'utilisateur au formulaire de mise à jour
        return "update-user";  // Vue pour le formulaire de mise à jour d'utilisateur
    }

    // Met à jour l'utilisateur après validation
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);  // En cas d'erreur, remet l'ID de l'utilisateur et retourne au formulaire
            return "update-user";
        }
        userRepository.save(user);  // Sauvegarde l'utilisateur mis à jour
        model.addAttribute("users", userRepository.findAll());  // Met à jour la liste d'utilisateurs
        return "index";
    }

    // Supprime l'utilisateur et met à jour la vue
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);  // Supprime l'utilisateur
        model.addAttribute("users", userRepository.findAll());  // Met à jour la liste d'utilisateurs
        return "index";
    }
}