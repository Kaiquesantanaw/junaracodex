package com.portfoliosass.controller;

import com.portfoliosass.dto.ProfileRequest;
import com.portfoliosass.model.Profile;
import com.portfoliosass.model.User;
import com.portfoliosass.repository.UserRepository;
import com.portfoliosass.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileService profileService;

    @GetMapping
    public String profilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Profile profile = profileService.getOrCreateProfile(user.getUsername());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("role", user.getRole().name());
        model.addAttribute("profile", profile);
        return "profile";
    }

    @PostMapping("/save")
    public String saveProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "") String fullName,
            @RequestParam(defaultValue = "") String jobTitle,
            @RequestParam(defaultValue = "") String location,
            @RequestParam(defaultValue = "") String bio,
            @RequestParam(defaultValue = "") String skills,
            @RequestParam(defaultValue = "") String contactEmail,
            @RequestParam(defaultValue = "") String contactPhone,
            @RequestParam(defaultValue = "") String contactLinkedin,
            @RequestParam(defaultValue = "") String contactGithub,
            @RequestParam(defaultValue = "") String contactWebsite,
            @RequestParam(defaultValue = "false") boolean publicVisible,
            // projetos
            @RequestParam(value = "proj_id", required = false) List<Long> projIds,
            @RequestParam(value = "proj_name", required = false) List<String> projNames,
            @RequestParam(value = "proj_desc", required = false) List<String> projDescs,
            @RequestParam(value = "proj_url", required = false) List<String> projUrls,
            @RequestParam(value = "proj_repo", required = false) List<String> projRepos,
            @RequestParam(value = "proj_tech", required = false) List<String> projTechs,
            // experiências
            @RequestParam(value = "exp_id", required = false) List<Long> expIds,
            @RequestParam(value = "exp_company", required = false) List<String> expCompanies,
            @RequestParam(value = "exp_role", required = false) List<String> expRoles,
            @RequestParam(value = "exp_start", required = false) List<String> expStarts,
            @RequestParam(value = "exp_end", required = false) List<String> expEnds,
            @RequestParam(value = "exp_desc", required = false) List<String> expDescs,
            // livros
            @RequestParam(value = "book_id", required = false) List<Long> bookIds,
            @RequestParam(value = "book_title", required = false) List<String> bookTitles,
            @RequestParam(value = "book_author", required = false) List<String> bookAuthors,
            @RequestParam(value = "book_year", required = false) List<String> bookYears,
            @RequestParam(value = "book_review", required = false) List<String> bookReviews,
            RedirectAttributes redirectAttributes) {

        List<ProfileRequest.ProjectRequest> projects = new ArrayList<>();
        if (projNames != null) {
            for (int i = 0; i < projNames.size(); i++) {
                projects.add(new ProfileRequest.ProjectRequest(
                        projIds != null && i < projIds.size() ? projIds.get(i) : null,
                        projNames.get(i),
                        projDescs != null && i < projDescs.size() ? projDescs.get(i) : "",
                        projUrls != null && i < projUrls.size() ? projUrls.get(i) : "",
                        projRepos != null && i < projRepos.size() ? projRepos.get(i) : "",
                        projTechs != null && i < projTechs.size() ? projTechs.get(i) : "",
                        i));
            }
        }

        List<ProfileRequest.ExperienceRequest> experiences = new ArrayList<>();
        if (expCompanies != null) {
            for (int i = 0; i < expCompanies.size(); i++) {
                experiences.add(new ProfileRequest.ExperienceRequest(
                        expIds != null && i < expIds.size() ? expIds.get(i) : null,
                        expCompanies.get(i),
                        expRoles != null && i < expRoles.size() ? expRoles.get(i) : "",
                        expStarts != null && i < expStarts.size() ? expStarts.get(i) : "",
                        expEnds != null && i < expEnds.size() ? expEnds.get(i) : "",
                        expDescs != null && i < expDescs.size() ? expDescs.get(i) : "",
                        i));
            }
        }

        List<ProfileRequest.BookRequest> books = new ArrayList<>();
        if (bookTitles != null) {
            for (int i = 0; i < bookTitles.size(); i++) {
                books.add(new ProfileRequest.BookRequest(
                        bookIds != null && i < bookIds.size() ? bookIds.get(i) : null,
                        bookTitles.get(i),
                        bookAuthors != null && i < bookAuthors.size() ? bookAuthors.get(i) : "",
                        bookYears != null && i < bookYears.size() ? bookYears.get(i) : "",
                        bookReviews != null && i < bookReviews.size() ? bookReviews.get(i) : "",
                        i));
            }
        }

        ProfileRequest req = new ProfileRequest(
                fullName, jobTitle, location, bio, skills,
                contactEmail, contactPhone, contactLinkedin, contactGithub, contactWebsite,
                publicVisible, projects, experiences, books);

        profileService.saveProfile(userDetails.getUsername(), req);
        redirectAttributes.addFlashAttribute("success", "Perfil salvo com sucesso!");
        return "redirect:/profile";
    }

    @PostMapping("/password")
    public String changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Senha atual incorreta.");
            return "redirect:/profile";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "As senhas não coincidem.");
            return "redirect:/profile";
        }
        if (newPassword.length() < 8) {
            redirectAttributes.addFlashAttribute("error", "A nova senha deve ter no mínimo 8 caracteres.");
            return "redirect:/profile";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Senha alterada com sucesso!");
        return "redirect:/profile";
    }
}
