package com.portfoliosass.service;

import com.portfoliosass.dto.ProfileRequest;
import com.portfoliosass.model.Book;
import com.portfoliosass.model.Experience;
import com.portfoliosass.model.Profile;
import com.portfoliosass.model.Project;
import com.portfoliosass.model.User;
import com.portfoliosass.repository.ProfileRepository;
import com.portfoliosass.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<Profile> findByUsername(String username) {
        return profileRepository.findByUserUsername(username);
    }

    @Transactional
    public Profile getOrCreateProfile(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return profileRepository.findByUser(user).orElseGet(() -> {
            Profile p = Profile.builder().user(user).build();
            return Objects.requireNonNull(profileRepository.save(p));
        });
    }

    @Transactional
    public Profile saveProfile(String username, ProfileRequest req) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Profile profile = profileRepository.findByUser(user)
                .orElseGet(() -> Profile.builder().user(user).build());

        profile.setFullName(req.fullName());
        profile.setJobTitle(req.jobTitle());
        profile.setLocation(req.location());
        profile.setBio(req.bio());
        profile.setSkills(req.skills());
        profile.setContactEmail(req.contactEmail());
        profile.setContactPhone(req.contactPhone());
        profile.setContactLinkedin(req.contactLinkedin());
        profile.setContactGithub(req.contactGithub());
        profile.setContactWebsite(req.contactWebsite());
        profile.setPublicVisible(req.publicVisible());

        // Projetos
        profile.getProjects().clear();
        if (req.projects() != null) {
            List<Project> projects = new ArrayList<>();
            for (ProfileRequest.ProjectRequest pr : req.projects()) {
                if (pr.name() == null || pr.name().isBlank())
                    continue;
                Project proj = Project.builder()
                        .profile(profile)
                        .name(pr.name())
                        .description(pr.description())
                        .url(pr.url())
                        .repoUrl(pr.repoUrl())
                        .technologies(pr.technologies())
                        .displayOrder(pr.displayOrder())
                        .build();
                projects.add(proj);
            }
            profile.getProjects().addAll(projects);
        }

        // Experiências
        profile.getExperiences().clear();
        if (req.experiences() != null) {
            List<Experience> experiences = new ArrayList<>();
            for (ProfileRequest.ExperienceRequest er : req.experiences()) {
                if (er.company() == null || er.company().isBlank())
                    continue;
                Experience exp = Experience.builder()
                        .profile(profile)
                        .company(er.company())
                        .role(er.role())
                        .startDate(er.startDate())
                        .endDate(er.endDate())
                        .description(er.description())
                        .displayOrder(er.displayOrder())
                        .build();
                experiences.add(exp);
            }
            profile.getExperiences().addAll(experiences);
        }

        // Livros
        profile.getBooks().clear();
        if (req.books() != null) {
            List<Book> books = new ArrayList<>();
            for (ProfileRequest.BookRequest br : req.books()) {
                if (br.title() == null || br.title().isBlank())
                    continue;
                Book book = Book.builder()
                        .profile(profile)
                        .title(br.title())
                        .author(br.author())
                        .year(br.year())
                        .review(br.review())
                        .displayOrder(br.displayOrder())
                        .build();
                books.add(book);
            }
            profile.getBooks().addAll(books);
        }

        return Objects.requireNonNull(profileRepository.save(profile));
    }
}
