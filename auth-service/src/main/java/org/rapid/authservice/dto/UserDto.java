package org.rapid.authservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import org.rapid.authservice.model.Role;

import java.time.LocalDateTime;

@Builder
public record UserDto(
        String username,
        String email,
        String firstName,
        String lastName,
        Role role,
        LocalDateTime createdAt
) {
}
