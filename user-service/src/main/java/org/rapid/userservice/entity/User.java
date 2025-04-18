package org.rapid.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.rapid.userservice.model.Role;

import java.util.List;

@Entity
@Table(name = "user")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role = Role.FREE;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "overview_layout")
    private String overviewLayout = null;

    @Column(name = "comparison_layout")
    private String comparisonLayout = null;

    @Column(name = "forecast_layout")
    private String forecastLayout = null;

    @Column(name = "github_token")
    private String githubToken = null;

    @Column(name = "profile_picture")
    private byte[] profilePicture = null;

    @Column(name = "is_reports_enable")
    private boolean isReportsEnable = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private AlertLimits alertLimits;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Payment> payments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Alert> alerts;
}
