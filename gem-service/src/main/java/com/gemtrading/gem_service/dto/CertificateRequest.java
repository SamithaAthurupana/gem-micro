package com.gemtrading.gem_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateRequest {

    @NotBlank(message = "Certificate number is required")
    private String certificateNumber;

    @NotNull(message = "Gem Id is required")
    private Long gemId;

    @NotBlank(message = "issue by is required")
    private String issueBy;

    @NotNull(message = "issue date is required")
    private LocalDate issueDate;

    @NotNull(message = "issue date is required")
    private LocalDate expiryDate;

    private String remarks;

}
