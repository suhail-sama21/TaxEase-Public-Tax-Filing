package com.cognizant.taxease.dto.requestdto;

import com.cognizant.taxease.entity.entityEnum.VerificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentVerificationRequestDto {

    @NotNull(message = "Verification status is required")
    private VerificationStatus verificationStatus;
}
