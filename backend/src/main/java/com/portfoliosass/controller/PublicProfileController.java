package com.portfoliosass.controller;

import com.portfoliosass.model.Profile;
import com.portfoliosass.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/u")
@RequiredArgsConstructor
public class PublicProfileController {

    private final ProfileService profileService;

    @GetMapping("/{username}")
    public String publicProfile(@PathVariable String username, Model model) {
        Profile profile = profileService.findByUsername(username)
                .filter(Profile::isPublicVisible)
                .orElse(null);

        if (profile == null) {
            model.addAttribute("username", username);
            return "public-profile-not-found";
        }

        model.addAttribute("profile", profile);
        model.addAttribute("owner", profile.getUser().getUsername());
        return "public-profile";
    }
}
