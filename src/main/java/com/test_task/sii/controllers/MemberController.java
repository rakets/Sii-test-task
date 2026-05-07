package com.test_task.sii.controllers;

import com.test_task.sii.dto.MemberDTO;
import com.test_task.sii.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{gymId}/{planId}/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/new")
    public ResponseEntity<MemberDTO> addNewMember(@Valid @RequestBody MemberDTO request,
                                                  @PathVariable("gymId") Long gymId,
                                                  @PathVariable("planId") Long planId) {
        MemberDTO newMember = memberService.saveNewMember(request, gymId, planId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMember);
    }
}
