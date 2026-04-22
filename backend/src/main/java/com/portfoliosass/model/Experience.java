package com.portfoliosass.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "experiences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false, length = 150)
    private String company;

    @Column(nullable = false, length = 150)
    private String role;

    @Column(length = 7)
    private String startDate; // formato: YYYY-MM

    @Column(length = 7)
    private String endDate; // formato: YYYY-MM ou vazio = atual

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "display_order")
    @Builder.Default
    private int displayOrder = 0;
}
