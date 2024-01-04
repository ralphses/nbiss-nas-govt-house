package com.clicks.nbissnasgovthouse.controllers;

import com.clicks.nbissnasgovthouse.dtos.AttackListDto;
import com.clicks.nbissnasgovthouse.dtos.ThreatDto;
import com.clicks.nbissnasgovthouse.service.AttackService;
import com.clicks.nbissnasgovthouse.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("api/v1/attack")
public class AttackController {

    private final AttackService attackService;

    @GetMapping
    public ResponseEntity<CustomResponse> getAttacks(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate) {

        List<AttackListDto> attacks = attackService.getAttacks(startDate, endDate);

        return ResponseEntity.ok(new CustomResponse(true, attacks));
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomResponse> getAttacks(@PathVariable Long id) {

        ThreatDto attack = attackService.getAttack(id);
        return ResponseEntity.ok(new CustomResponse(true, attack));
    }

    @GetMapping("/threats/{page}")
    public ResponseEntity<CustomResponse> getThreats(@PathVariable Integer page) {

        Map<String, Object> threats = attackService.getAllThreats(page);
        return ResponseEntity.ok(new CustomResponse(true, threats));
    }
}
