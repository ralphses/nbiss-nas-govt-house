package com.clicks.nbissnasgovthouse.utils;

import com.clicks.nbissnasgovthouse.dtos.AttackDto;
import com.clicks.nbissnasgovthouse.dtos.ThreatDto;
import com.clicks.nbissnasgovthouse.model.Attack;
import com.clicks.nbissnasgovthouse.model.Threat;
import com.clicks.nbissnasgovthouse.repository.AttackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DtoMapper {

    private final AttackRepository attackRepository;

    public ThreatDto threatDto(Threat threat) {
        List<Attack> attacks = attackRepository.findByThreat_Name(threat.getName());

        Map<String, Long> levelCountMap = attacks
                .stream()
                .collect(Collectors.groupingBy(attack -> attack.getLevel().name(), Collectors.counting()));

        String level = levelCountMap
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse("");

        return new ThreatDto(
                threat.getName(),
                attacks.size(),
                level,
                attacks.stream().map(this::attackDto).toList()
        );
    }


    public AttackDto attackDto (Attack attack) {
        return new AttackDto(
                attack.getOrigin(),
                attack.getTimestamp().format(DateTimeFormatter.ofPattern("dd MMMM, yyyy h:ma")));
    }
}
