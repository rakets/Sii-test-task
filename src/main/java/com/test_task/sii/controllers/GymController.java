package com.test_task.sii.controllers;

import com.test_task.sii.dto.GymDTO;
import com.test_task.sii.dto.ReportDTO;
import com.test_task.sii.entity.Gym;
import com.test_task.sii.service.GymService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/gyms")
public class GymController {
    private final GymService gymService;

    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    @PostMapping("/new")
    public ResponseEntity<GymDTO> newGym(@Valid @RequestBody GymDTO request){
        GymDTO newGym = gymService.createNewGym(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGym);
    }

    @GetMapping("/all-gyms")
    public ResponseEntity<List<GymDTO>> getAllGyms() {
        List<GymDTO> gymList = gymService.getAllGyms();
        return ResponseEntity.status(HttpStatus.OK).body(gymList);
    }

    @GetMapping("/report")
    public ResponseEntity<List<ReportDTO>> getTotalReport() {
        List<ReportDTO> response = gymService.getTotalReport();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
