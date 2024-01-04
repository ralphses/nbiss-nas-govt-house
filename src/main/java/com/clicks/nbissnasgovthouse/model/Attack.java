package com.clicks.nbissnasgovthouse.model;

import com.clicks.nbissnasgovthouse.enums.AttackLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attack {

    @Id
    @GeneratedValue
    private Long id;

    private String origin;
    private LocalDateTime timestamp;
    private AttackLevel level;

    @ManyToOne
    private Threat threat;
}
