package com.test_task.sii.controllers;

import com.test_task.sii.dto.MembershipPlanDTO;
import com.test_task.sii.service.MembershipPlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/{gymId}/membership-plan")
public class MembershipPlanController {
    private final MembershipPlanService membershipPlanService;

    public MembershipPlanController(MembershipPlanService membershipPlanService) {
        this.membershipPlanService = membershipPlanService;
    }

    @PostMapping("/new")
    public ResponseEntity<MembershipPlanDTO> newPlanForGym(@Valid @RequestBody MembershipPlanDTO request,
                                                           @PathVariable("gymId") Long gymId) {
        MembershipPlanDTO newMembershipPlanDTO = membershipPlanService.saveNewMembershipPlan(request, gymId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMembershipPlanDTO);
    }

    @GetMapping("/all-plans")
    public ResponseEntity<List<MembershipPlanDTO>> getAllPlans(@PathVariable("gymId") Long gymId) {
        List<MembershipPlanDTO> response = membershipPlanService.getAllPlansForGym(gymId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
