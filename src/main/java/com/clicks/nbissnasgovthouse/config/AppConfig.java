package com.clicks.nbissnasgovthouse.config;

import com.clicks.nbissnasgovthouse.model.Attack;
import com.clicks.nbissnasgovthouse.model.Threat;
import com.clicks.nbissnasgovthouse.repository.AttackRepository;
import com.clicks.nbissnasgovthouse.repository.ThreatRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.clicks.nbissnasgovthouse.enums.AttackLevel.values;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.stream;
import static java.util.concurrent.ThreadLocalRandom.current;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final ThreatRepository threatRepository;
    private final AttackRepository attackRepository;

    private List<Threat> threats() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Read JSON file and convert to List of SecurityThreat objects
        List<Threat> threats = objectMapper.readValue(new File("src/main/resources/threats.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Threat.class));

        return threats.stream().map(threatRepository::save).toList();
    }

    @Bean
    CommandLineRunner commandLineRunner() {

        List<LocalDateTime> dateTimes = List.of(
                now().plusMinutes(current().nextInt(0, 80)),
                now().minusMinutes(current().nextInt(0, 80)),
                now().plusHours(current().nextInt(0, 8)),
                now().minusHours(current().nextInt(0, 8)),
                now().plusDays(current().nextInt(0, 8)),
                now().minusDays(current().nextInt(0, 8)),
                now().minusYears(current().nextInt(0, 8)),
                now().plusWeeks(current().nextInt(0, 8)),
                now().minusWeeks(current().nextInt(0, 8))
        );
        return args -> threats()
                .forEach(threat -> {
                    int random = current().nextInt(3, 10);
                    IntStream.range(0, random).forEach(count -> {
                        String ip = current().ints(0, 256)
                                .limit(4)
                                .mapToObj(Integer::toString)
                                .collect(Collectors.joining("."));
                        attackRepository.save(
                                Attack.builder()
                                        .threat(threat)
                                        .origin(ip)
                                        .level(stream(values()).toList().get(current().nextInt(0, values().length)))
                                        .timestamp(dateTimes.get(current().nextInt(0, dateTimes.size())))
                                        .build());
                    });
                });
    }

}
