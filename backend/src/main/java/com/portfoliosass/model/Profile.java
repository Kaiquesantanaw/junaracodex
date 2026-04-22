package com.portfoliosass.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 100)
    private String fullName;

    @Column(length = 100)
    private String jobTitle;

    @Column(length = 100)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 200)
    private String contactEmail;

    @Column(length = 100)
    private String contactPhone;

    @Column(length = 200)
    private String contactLinkedin;

    @Column(length = 200)
    private String contactGithub;

    @Column(length = 200)
    private String contactWebsite;

    // Habilidades como texto separado por vírgula
    @Column(columnDefinition = "TEXT")
    private String skills;

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Experience> experiences = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

    @Builder.Default
    @Column(name = "public_visible", nullable = false)
    private boolean publicVisible = true;
}
