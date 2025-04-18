package org.rapid.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alert")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime timestamp;

    private String message;

    @Column(name = "is_showed")
    private boolean isShowed = false;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
