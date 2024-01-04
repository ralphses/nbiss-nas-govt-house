package com.clicks.nbissnasgovthouse.service;

import com.clicks.nbissnasgovthouse.dtos.AttackListDto;
import com.clicks.nbissnasgovthouse.dtos.ThreatDto;
import com.clicks.nbissnasgovthouse.model.Attack;
import com.clicks.nbissnasgovthouse.model.Threat;
import com.clicks.nbissnasgovthouse.repository.AttackRepository;
import com.clicks.nbissnasgovthouse.repository.ThreatRepository;
import com.clicks.nbissnasgovthouse.utils.DtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@Transactional
@RequiredArgsConstructor
public class AttackService {

    private final AttackRepository attackRepository;
    private final ThreatRepository threatRepository;
    private final DtoMapper mapper;

    public List<AttackListDto> getAttacks(String startDate, String endDate) {

        // Create Optional instances for startDate and endDate to handle nullability
        Optional<String> startDateOptional = ofNullable(startDate);
        Predicate<Attack> filterPredicate = getAttackPredicate(endDate, startDateOptional);

        // Retrieve all attacks, apply the filtering, and collect the result
        return attackRepository.findAll()
                .stream()
                .filter(filterPredicate)
                .collect(Collectors.groupingBy(attack -> attack.getThreat().getName(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new AttackListDto(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Predicate<Attack> getAttackPredicate(String endDate, Optional<String> startDateOptional) {

        Optional<String> endDateOptional = ofNullable(endDate);

        // Define a Predicate to filter attacks based on the date range
        return attack -> {
            LocalDateTime timestamp = attack.getTimestamp();
            // Check if attack timestamp is after startDate (if present) and before endDate (if present)
            return (startDateOptional.isEmpty() || timestamp.isAfter(convertStringToLocalDateTime(startDateOptional.get())))
                    && (endDateOptional.isEmpty() || timestamp.isBefore(convertStringToLocalDateTime(endDateOptional.get())));
        };
    }


    public ThreatDto getAttack(Long id) {
        return threatRepository.findById(id).map(mapper::threatDto).orElseThrow(IllegalStateException::new);
    }

    public Map<String, Object> getAllThreats(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 8);
        Page<Threat> threatDtos = threatRepository.findAll(pageRequest);
        return Map.of("totalPages", threatDtos.getTotalPages(), "threats", threatDtos.get().map(mapper::threatDto));
    }

    public static LocalDateTime convertStringToLocalDateTime(String input) {
        // Find the index of "G"
        int index = input.indexOf('G');

        // Check if "G" is found, and extract the substring up to that index
        if (index != -1) {
            String substring = input.substring(0, index).trim();

            // Define the input format for the substring
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss");

            // Parse the substring to LocalDateTime
            return LocalDateTime.parse(substring, formatter);
        }

        // If "G" is not found, return null or throw an exception, depending on your requirements
        return LocalDateTime.now();
    }
}
