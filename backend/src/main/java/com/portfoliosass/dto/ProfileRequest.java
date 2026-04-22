package com.portfoliosass.dto;

import java.util.List;

public record ProfileRequest(
                String fullName,
                String jobTitle,
                String location,
                String bio,
                String skills,
                String contactEmail,
                String contactPhone,
                String contactLinkedin,
                String contactGithub,
                String contactWebsite,
                boolean publicVisible,
                List<ProjectRequest> projects,
                List<ExperienceRequest> experiences,
                List<BookRequest> books) {
        public record ProjectRequest(
                        Long id,
                        String name,
                        String description,
                        String url,
                        String repoUrl,
                        String technologies,
                        int displayOrder) {
        }

        public record ExperienceRequest(
                        Long id,
                        String company,
                        String role,
                        String startDate,
                        String endDate,
                        String description,
                        int displayOrder) {
        }

        public record BookRequest(
                        Long id,
                        String title,
                        String author,
                        String year,
                        String review,
                        int displayOrder) {
        }
}
