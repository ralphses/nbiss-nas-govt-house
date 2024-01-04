package com.clicks.nbissnasgovthouse.dtos;

import java.util.List;

public record ThreatDto(
        String name,
        int noOfOccurrence,
        String avgLevel,
        List<AttackDto> attacks
        ) {
}
