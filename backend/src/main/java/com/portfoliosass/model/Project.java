package com.portfoliosass.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "projects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 200)
    private String url;

    @Column(length = 200)
    private String repoUrl;

    @Column(length = 200)
    private String technologies;

    @Column(name = "display_order")
    @Builder.Default
    private int displayOrder = 0;
}
