package com.portfoliosass.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 150)
    private String author;

    @Column(length = 4)
    private String year;

    @Column(columnDefinition = "TEXT")
    private String review;

    @Column(name = "display_order")
    @Builder.Default
    private int displayOrder = 0;
}
